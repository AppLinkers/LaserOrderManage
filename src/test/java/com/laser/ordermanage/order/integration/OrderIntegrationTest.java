package com.laser.ordermanage.order.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.laser.ordermanage.common.IntegrationTest;
import com.laser.ordermanage.common.paging.ListResponse;
import com.laser.ordermanage.common.security.jwt.setup.JwtBuilder;
import com.laser.ordermanage.order.domain.type.Stage;
import com.laser.ordermanage.order.dto.request.CreateCommentRequest;
import com.laser.ordermanage.order.dto.request.CreateCommentRequestBuilder;
import com.laser.ordermanage.order.dto.response.GetCommentResponse;
import com.laser.ordermanage.order.dto.response.GetCommentResponseBuilder;
import com.laser.ordermanage.order.dto.response.GetOrderDetailResponse;
import com.laser.ordermanage.order.dto.response.GetOrderDetailResponseBuilder;
import com.laser.ordermanage.order.exception.OrderErrorCode;
import com.laser.ordermanage.user.exception.UserErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderIntegrationTest extends IntegrationTest {

    @Autowired
    private JwtBuilder jwtBuilder;

    /**
     * 거래의 상세 정보 조회 성공
     */
    @Test
    public void 거래_상세_정보_조회_성공() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuild();
        final String orderId = "1";
        final GetOrderDetailResponse expectedResponse = GetOrderDetailResponseBuilder.build();

        // when
        final ResultActions resultActions = requestGetOrderDetail(accessToken, orderId);

        // then
        final String responseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final GetOrderDetailResponse actualResponse = objectMapper.readValue(responseString, GetOrderDetailResponse.class);

        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 거래의 상세 정보 조회 실패
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보 (Access Token) 를 추가하지 않음
     */
    @Test
    public void 거래_상세_정보_조회_실패_Header_Authorization_존재() throws Exception {
        // given
        final String orderId = "1";

        // when
        final ResultActions resultActions = requestGetOrderDetailWithOutAccessToken(orderId);

        // then
        assertError(UserErrorCode.MISSING_JWT, resultActions);
    }

    /**
     * 거래의 상세 정보 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보 (Access Token) 에 권한 정보가 없음
     */
    @Test
    public void 거래_상세_정보_조회_실패_Unauthorized_Access_Token() throws Exception {
        // given
        final String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessJwtBuild();
        final String orderId = "1";

        // when
        final ResultActions resultActions = requestGetOrderDetail(unauthorizedAccessToken, orderId);

        // then
        assertError(UserErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 거래의 상세 정보 조회 실패
     * - 실패 사유 : 요청 시, Header 에 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    public void 거래_상세_정보_조회_실패_Token_Type() throws Exception {
        // given
        final String refreshToken = jwtBuilder.refreshJwtBuild();
        final String orderId = "1";

        // when
        final ResultActions resultActions = requestGetOrderDetail(refreshToken, orderId);

        // then
        assertError(UserErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 거래의 상세 정보 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    public void 거래_상세_정보_조회_실패_Expired_Access_Token() throws Exception {
        // given
        final String expiredAccessToken = jwtBuilder.expiredAccessJwtBuild();
        final String orderId = "1";

        // when
        final ResultActions resultActions = requestGetOrderDetail(expiredAccessToken, orderId);

        // then
        assertError(UserErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 거래의 상세 정보 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    public void 거래_상세_정보_조회_실패_Invalid_Token() throws Exception {
        // given
        final String invalidToken = jwtBuilder.invalidJwtBuild();
        final String orderId = "1";

        // when
        final ResultActions resultActions = requestGetOrderDetail(invalidToken, orderId);

        // then
        assertError(UserErrorCode.INVALID_JWT, resultActions);
    }

    /**
     * 거래의 상세 정보 조회 실패
     * - 실패 사유 : 거래에 대한 접근 권한이 없음
     */
    @Test
    public void 거래_상세_정보_조회_실패_거래_접근_권한() throws Exception {
        // given
        final String accessTokenOfUser2 = jwtBuilder.accessJwtBuildOfUser2();
        final String orderId = "1";

        // when
        final ResultActions resultActions = requestGetOrderDetail(accessTokenOfUser2, orderId);

        // then
        assertError(OrderErrorCode.DENIED_ACCESS_TO_ORDER, resultActions);
    }

    /**
     * 거래의 댓글 목록 조회 성공
     */
    @Test
    public void 거래_댓글_목록_조회_성공() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuild();
        final String orderId = "1";
        final ListResponse<GetCommentResponse> expectedResponse = new ListResponse<>(GetCommentResponseBuilder.buildListForOrder1());

        // when
        final ResultActions resultActions = requestGetOrderComment(accessToken, orderId);

        // then
        final String responseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final ListResponse<GetCommentResponse> actualResponse = objectMapper.readValue(responseString, new TypeReference<ListResponse<GetCommentResponse>>() {});

        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 거래의 댓글 목록 조회 실패
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보 (Access Token) 를 추가하지 않음
     */
    @Test
    public void 거래_댓글_목록_조회_실패_Header_Authorization_존재() throws Exception {
        // given
        final String orderId = "1";

        // when
        final ResultActions resultActions = requestGetOrderCommentWithOutAccessToken(orderId);

        // then
        assertError(UserErrorCode.MISSING_JWT, resultActions);
    }

    /**
     * 거래의 댓글 목록 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보 (Access Token) 에 권한 정보가 없음
     */
    @Test
    public void 거래_댓글_목록_조회_실패_Unauthorized_Access_Token() throws Exception {
        // given
        final String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessJwtBuild();
        final String orderId = "1";

        // when
        final ResultActions resultActions = requestGetOrderComment(unauthorizedAccessToken, orderId);

        // then
        assertError(UserErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 거래의 댓글 목록 조회 실패
     * - 실패 사유 : 요청 시, Header 에 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    public void 거래_댓글_목록_조회_실패_Token_Type() throws Exception {
        // given
        final String refreshToken = jwtBuilder.refreshJwtBuild();
        final String orderId = "1";

        // when
        final ResultActions resultActions = requestGetOrderComment(refreshToken, orderId);

        // then
        assertError(UserErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 거래의 댓글 목록 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    public void 거래_댓글_목록_조회_실패_Expired_Access_Token() throws Exception {
        // given
        final String expiredAccessToken = jwtBuilder.expiredAccessJwtBuild();
        final String orderId = "1";

        // when
        final ResultActions resultActions = requestGetOrderComment(expiredAccessToken, orderId);

        // then
        assertError(UserErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 거래의 댓글 목록 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    public void 거래_댓글_목록_조회_실패_Invalid_Token() throws Exception {
        // given
        final String invalidToken = jwtBuilder.invalidJwtBuild();
        final String orderId = "1";

        // when
        final ResultActions resultActions = requestGetOrderComment(invalidToken, orderId);

        // then
        assertError(UserErrorCode.INVALID_JWT, resultActions);
    }

    /**
     * 거래의 댓글 목록 조회 실패
     * - 실패 사유 : 거래에 대한 접근 권한이 없음
     */
    @Test
    public void 거래_댓글_목록_조회_실패_거래_접근_권한() throws Exception {
        // given
        final String accessTokenOfUser2 = jwtBuilder.accessJwtBuildOfUser2();
        final String orderId = "1";

        // when
        final ResultActions resultActions = requestGetOrderComment(accessTokenOfUser2, orderId);

        // then
        assertError(OrderErrorCode.DENIED_ACCESS_TO_ORDER, resultActions);
    }

    /**
     * 거래에 댓글 작성 성공 (고객)
     */
    @Test
    public void 거래_댓글_작성_성공_By_Customer() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuild();
        final String orderId = "1";
        final CreateCommentRequest request = CreateCommentRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateComment(accessToken, orderId, request);

        // then
        resultActions.andExpect(status().isOk());

        // when after create comment
        final ResultActions resultActionsAfterCreateComment = requestGetOrderComment(accessToken, orderId);

        // then after create comment
        resultActionsAfterCreateComment
                .andExpect(status().isOk())
                .andExpect(jsonPath("totalElements").value(3));
    }

    /**
     * 거래에 댓글 작성 성공 (공장)
     */
    @Test
    public void 거래_댓글_작성_성공_By_Factory() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfFactory();
        final String orderId = "1";
        final CreateCommentRequest request = CreateCommentRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateComment(accessToken, orderId, request);

        // then
        resultActions.andExpect(status().isOk());

        // when after create comment
        final ResultActions resultActionsAfterCreateComment = requestGetOrderComment(accessToken, orderId);

        // then after create comment
        resultActionsAfterCreateComment
                .andExpect(status().isOk())
                .andExpect(jsonPath("totalElements").value(3));
    }

    /**
     * 거래에 댓글 작성 실패
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보 (Access Token) 를 추가하지 않음
     */
    @Test
    public void 거래_댓글_작성_실패_Header_Authorization_존재() throws Exception {
        // given
        final String orderId = "1";
        final CreateCommentRequest request = CreateCommentRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateCommentWithOutAccessToken(orderId, request);

        // then
        assertError(UserErrorCode.MISSING_JWT, resultActions);
    }

    /**
     * 거래에 댓글 작성 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보 (Access Token) 에 권한 정보가 없음
     */
    @Test
    public void 거래_댓글_작성_실패_Unauthorized_Access_Token() throws Exception {
        // given
        final String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessJwtBuild();
        final String orderId = "1";
        final CreateCommentRequest request = CreateCommentRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateComment(unauthorizedAccessToken, orderId, request);

        // then
        assertError(UserErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 거래에 댓글 작성 실패
     * - 실패 사유 : 요청 시, Header 에 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    public void 거래_댓글_작성_실패_Token_Type() throws Exception {
        // given
        final String refreshToken = jwtBuilder.refreshJwtBuild();
        final String orderId = "1";
        final CreateCommentRequest request = CreateCommentRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateComment(refreshToken, orderId, request);

        // then
        assertError(UserErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 거래에 댓글 작성 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    public void 거래_댓글_작성_실패_Expired_Access_Token() throws Exception {
        // given
        final String expiredAccessToken = jwtBuilder.expiredAccessJwtBuild();
        final String orderId = "1";
        final CreateCommentRequest request = CreateCommentRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateComment(expiredAccessToken, orderId, request);

        // then
        assertError(UserErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 거래에 댓글 작성 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    public void 거래_댓글_작성_실패_Invalid_Token() throws Exception {
        // given
        final String invalidToken = jwtBuilder.invalidJwtBuild();
        final String orderId = "1";
        final CreateCommentRequest request = CreateCommentRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateComment(invalidToken, orderId, request);

        // then
        assertError(UserErrorCode.INVALID_JWT, resultActions);
    }

    /**
     * 거래에 댓글 작성 실패
     * - 실패 사유 : 거래에 대한 접근 권한이 없음
     */
    @Test
    public void 거래_댓글_작성_실패_거래_접근_권한() throws Exception {
        // given
        final String accessTokenOfUser2 = jwtBuilder.accessJwtBuildOfUser2();
        final String orderId = "1";
        final CreateCommentRequest request = CreateCommentRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateComment(accessTokenOfUser2, orderId, request);

        // then
        assertError(OrderErrorCode.DENIED_ACCESS_TO_ORDER, resultActions);
    }

    /**
     * 거래 삭제 성공 (고객)
     */
    @Test
    public void 거래_삭제_성공_By_Customer() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuild();
        final String orderId = "5";

        // when
        final ResultActions resultActions = requestDeleteOrder(accessToken, orderId);

        // then
        resultActions.andExpect(status().isOk());

        // when after delete order
        final ResultActions resultActionsAfterDeleteOrder = requestGetOrderDetail(accessToken, orderId);

        // then
        assertError(OrderErrorCode.NOT_FOUND_ORDER, resultActionsAfterDeleteOrder);
    }

    /**
     * 거래 삭제 성공 (공장)
     */
    @Test
    public void 거래_삭제_성공_By_Factory() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfFactory();
        final String orderId = "5";

        // when
        final ResultActions resultActions = requestDeleteOrder(accessToken, orderId);

        // then
        resultActions.andExpect(status().isOk());

        // when after delete order
        final ResultActions resultActionsAfterDeleteOrder = requestGetOrderDetail(accessToken, orderId);

        // then
        assertError(OrderErrorCode.NOT_FOUND_ORDER, resultActionsAfterDeleteOrder);
    }

    /**
     * 거래 삭제 실패
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보 (Access Token) 를 추가하지 않음
     */
    @Test
    public void 거래_삭제_실패_Header_Authorization_존재() throws Exception {
        // given
        final String orderId = "5";

        // when
        final ResultActions resultActions = requestDeleteOrderWithOutAccessToken(orderId);

        // then
        assertError(UserErrorCode.MISSING_JWT, resultActions);
    }

    /**
     * 거래 삭제 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보 (Access Token) 에 권한 정보가 없음
     */
    @Test
    public void 거래_삭제_실패_Unauthorized_Access_Token() throws Exception {
        // given
        final String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessJwtBuild();
        final String orderId = "1";

        // when
        final ResultActions resultActions = requestDeleteOrder(unauthorizedAccessToken, orderId);

        // then
        assertError(UserErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 거래 삭제 실패
     * - 실패 사유 : 요청 시, Header 에 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    public void 거래_삭제_실패_Token_Type() throws Exception {
        // given
        final String refreshToken = jwtBuilder.refreshJwtBuild();
        final String orderId = "1";

        // when
        final ResultActions resultActions = requestDeleteOrder(refreshToken, orderId);

        // then
        assertError(UserErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 거래 삭제 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    public void 거래_삭제_실패_Expired_Access_Token() throws Exception {
        // given
        final String expiredAccessToken = jwtBuilder.expiredAccessJwtBuild();
        final String orderId = "1";

        // when
        final ResultActions resultActions = requestDeleteOrder(expiredAccessToken, orderId);

        // then
        assertError(UserErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 거래 삭제 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    public void 거래_삭제_실패_Invalid_Token() throws Exception {
        // given
        final String invalidToken = jwtBuilder.invalidJwtBuild();
        final String orderId = "1";

        // when
        final ResultActions resultActions = requestDeleteOrder(invalidToken, orderId);

        // then
        assertError(UserErrorCode.INVALID_JWT, resultActions);
    }

    /**
     * 거래 삭제 실패
     * - 실패 사유 : 거래에 대한 접근 권한이 없음
     */
    @Test
    public void 거래_삭제_실패_거래_접근_권한() throws Exception {
        // given
        final String accessTokenOfUser2 = jwtBuilder.accessJwtBuildOfUser2();
        final String orderId = "5";

        // when
        final ResultActions resultActions = requestDeleteOrder(accessTokenOfUser2, orderId);

        // then
        assertError(OrderErrorCode.DENIED_ACCESS_TO_ORDER, resultActions);
    }

    /**
     * 거래 삭제 실패
     * - 실패 사유 : 거래의 단계가 삭제 가능 단계(견적 대기, 견적 승인)가 아님
     */
    @Test
    public void 거래_삭제_실패_거래삭제_가능단계() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuild();
        final String orderId = "1";

        // when
        final ResultActions resultActions = requestDeleteOrder(accessToken, orderId);

        // then
        assertErrorWithMessage(OrderErrorCode.INVALID_ORDER_STAGE, resultActions, Stage.COMPLETED.getValue());
    }

    private ResultActions requestGetOrderDetail(String accessToken, String orderId) throws Exception {
        return mvc.perform(get("/order/{order-id}/detail", orderId)
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }

    private ResultActions requestGetOrderDetailWithOutAccessToken(String orderId) throws Exception{
        return mvc.perform(get("/order/{order-id}/detail", orderId))
                .andDo(print());
    }

    private ResultActions requestGetOrderComment(String accessToken, String orderId) throws Exception {
        return mvc.perform(get("/order/{order-id}/comment", orderId)
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }

    private ResultActions requestGetOrderCommentWithOutAccessToken(String orderId) throws Exception {
        return mvc.perform(get("/order/{order-id}/comment", orderId))
                .andDo(print());
    }

    private ResultActions requestCreateComment(String accessToken, String orderId, CreateCommentRequest request) throws Exception {
        return mvc.perform(post("/order/{order-id}/comment", orderId)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }

    private ResultActions requestCreateCommentWithOutAccessToken(String orderId, CreateCommentRequest request) throws Exception {
        return mvc.perform(post("/order/{order-id}/comment", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }

    private ResultActions requestDeleteOrder(String accessToken, String orderId) throws Exception {
        return mvc.perform(delete("/order/{order-id}", orderId)
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }

    private ResultActions requestDeleteOrderWithOutAccessToken(String orderId) throws Exception {
        return mvc.perform(delete("/order/{order-id}", orderId))
                .andDo(print());
    }
}
