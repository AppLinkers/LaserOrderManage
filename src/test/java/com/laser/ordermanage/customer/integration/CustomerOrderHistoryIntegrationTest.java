package com.laser.ordermanage.customer.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.laser.ordermanage.common.IntegrationTest;
import com.laser.ordermanage.common.paging.PageResponse;
import com.laser.ordermanage.common.security.jwt.setup.JwtBuilder;
import com.laser.ordermanage.customer.dto.response.*;
import com.laser.ordermanage.order.exception.OrderErrorCode;
import com.laser.ordermanage.user.exception.UserErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CustomerOrderHistoryIntegrationTest extends IntegrationTest {

    @Autowired
    private JwtBuilder jwtBuilder;

    private final static Pageable pageable = PageRequest.of(0, 10);

    /**
     * 고객 회원의 거래 목록 조회 성공
     */
    @Test
    public void 고객_회원의_거래_목록_조회_성공() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfCustomer();
        final List<CustomerGetOrderHistoryResponse> expectedOrderList = CustomerGetOrderHistoryResponseBuilder.buildListOfCustomer1();
        final PageResponse<CustomerGetOrderHistoryResponse> expectedResponse = new PageResponse<>(new PageImpl<>(expectedOrderList, pageable, expectedOrderList.size()));

        // when
        final ResultActions resultActions = requestGetOrderHistory(accessToken);

        // then
        final String responseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final PageResponse<CustomerGetOrderHistoryResponse> actualResponse = objectMapper.readValue(responseString, new TypeReference<PageResponse<CustomerGetOrderHistoryResponse>>() {});

        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 고객 회원의 거래 목록 조회 실패
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보 (Access Token) 를 추가하지 않음
     */
    @Test
    public void 고객_회원의_거래_목록_조회_실패_Header_Authorization_존재() throws Exception {
        // given

        // when
        final ResultActions resultActions = requestGetOrderHistoryWithOutAccessToken();

        // then
        assertError(UserErrorCode.MISSING_JWT, resultActions);
    }


    /**
     * 고객 회원의 거래 목록 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보 (Access Token) 에 권한 정보가 없음
     */
    @Test
    public void 고객_회원의_거래_목록_조회_실패_Unauthorized_Access_Token() throws Exception {
        // given
        final String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessJwtBuild();

        // when
        final ResultActions resultActions = requestGetOrderHistory(unauthorizedAccessToken);

        // then
        assertError(UserErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 고객 회원의 거래 목록 조회 실패
     * - 실패 사유 : 요청 시, Header 에 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    public void 고객_회원의_거래_목록_조회_실패_Token_Type() throws Exception {
        // given
        final String refreshToken = jwtBuilder.refreshJwtBuildOfCustomer();

        // when
        final ResultActions resultActions = requestGetOrderHistory(refreshToken);

        // then
        assertError(UserErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 고객 회원의 거래 목록 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    public void 고객_회원의_거래_목록_조회_실패_Expired_Access_Token() throws Exception {
        // given
        final String expiredAccessToken = jwtBuilder.expiredAccessJwtBuild();

        // when
        final ResultActions resultActions = requestGetOrderHistory(expiredAccessToken);

        // then
        assertError(UserErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 고객 회원의 거래 목록 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    public void 고객_회원의_거래_목록_조회_실패_Invalid_Token() throws Exception {
        // given
        final String invalidToken = jwtBuilder.invalidJwtBuild();

        // when
        final ResultActions resultActions = requestGetOrderHistory(invalidToken);

        // then
        assertError(UserErrorCode.INVALID_JWT, resultActions);
    }

    /**
     * 고객 회원의 거래 완료 단계인 거래 목록 조회 성공
     */
    @Test
    public void 고객_회원의_거래_완료_목록_조회_성공() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfCustomer();
        final List<CustomerGetOrderIsCompletedHistoryResponse> expectedOrderList = CustomerGetOrderIsCompletedHistoryResponseBuilder.build();
        final PageResponse<CustomerGetOrderIsCompletedHistoryResponse> expectedResponse = new PageResponse<>(new PageImpl<>(expectedOrderList, pageable, expectedOrderList.size()));

        // when
        final ResultActions resultActions = requestGetOrderIsCompletedHistory(accessToken);

        // then
        final String responseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final PageResponse<CustomerGetOrderIsCompletedHistoryResponse> actualResponse = objectMapper.readValue(responseString, new TypeReference<PageResponse<CustomerGetOrderIsCompletedHistoryResponse>>() {});

        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }


    /**
     * 고객 회원의 거래 완료 단계인 거래 목록 조회 실패
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보 (Access Token) 를 추가하지 않음
     */
    @Test
    public void 고객_회원의_거래_완료_목록_조회_실패_Header_Authorization_존재() throws Exception {
        // given

        // when
        final ResultActions resultActions = requestGetOrderIsCompletedHistoryWithOutAccessToken();

        // then
        assertError(UserErrorCode.MISSING_JWT, resultActions);
    }

    /**
     * 고객 회원의 거래 완료 단계인 거래 목록 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보 (Access Token) 에 권한 정보가 없음
     */
    @Test
    public void 고객_회원의_거래_완료_목록_조회_실패_Unauthorized_Access_Token() throws Exception {
        // given
        final String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessJwtBuild();

        // when
        final ResultActions resultActions = requestGetOrderIsCompletedHistory(unauthorizedAccessToken);

        // then
        assertError(UserErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 고객 회원의 거래 완료 단계인 거래 목록 조회 실패
     * - 실패 사유 : 요청 시, Header 에 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    public void 고객_회원의_거래_완료_목록_조회_실패_Token_Type() throws Exception {
        // given
        final String refreshToken = jwtBuilder.refreshJwtBuildOfCustomer();

        // when
        final ResultActions resultActions = requestGetOrderIsCompletedHistory(refreshToken);

        // then
        assertError(UserErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 고객 회원의 거래 완료 단계인 거래 목록 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    public void 고객_회원의_거래_완료_목록_조회_실패_Expired_Access_Token() throws Exception {
        // given
        final String expiredAccessToken = jwtBuilder.expiredAccessJwtBuild();

        // when
        final ResultActions resultActions = requestGetOrderIsCompletedHistory(expiredAccessToken);

        // then
        assertError(UserErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 고객 회원의 거래 완료 단계인 거래 목록 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    public void 고객_회원의_거래_완료_목록_조회_실패_Invalid_Token() throws Exception {
        // given
        final String invalidToken = jwtBuilder.invalidJwtBuild();

        // when
        final ResultActions resultActions = requestGetOrderIsCompletedHistory(invalidToken);

        // then
        assertError(UserErrorCode.INVALID_JWT, resultActions);
    }

    /**
     * 고객 회원의 특정 거래의 생성 정보 조회 성공
     */
    @Test
    public void 고객_회원의_특정_거래의_생성_정보_조회_성공() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfCustomer();
        final String orderId = "1";
        final CustomerGetOrderCreateInformationResponse expectedResponse = CustomerGetOrderCreateInformationResponseBuilder.build();

        // when
        final ResultActions resultActions = requestGetOrderCreateInformation(accessToken, orderId);

        // then
        final String responseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final CustomerGetOrderCreateInformationResponse actualResponse = objectMapper.readValue(responseString, new TypeReference<CustomerGetOrderCreateInformationResponse>() {});

        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 고객 회원의 특정 거래의 생성 정보 조회 실패
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보 (Access Token) 를 추가하지 않음
     */
    @Test
    public void 고객_회원의_특정_거래의_생성_정보_조회_실패_Header_Authorization_존재() throws Exception {
        // given
        final String orderId = "1";

        // when
        final ResultActions resultActions = requestGetOrderCreateInformationWithOutAccessToken(orderId);

        // then
        assertError(UserErrorCode.MISSING_JWT, resultActions);
    }

    /**
     * 고객 회원의 특정 거래의 생성 정보 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보 (Access Token) 에 권한 정보가 없음
     */
    @Test
    public void 고객_회원의_특정_거래의_생성_정보_조회_실패_Unauthorized_Access_Token() throws Exception {
        // given
        final String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessJwtBuild();
        final String orderId = "1";

        // when
        final ResultActions resultActions = requestGetOrderCreateInformation(unauthorizedAccessToken, orderId);

        // then
        assertError(UserErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 고객 회원의 특정 거래의 생성 정보 조회 실패
     * - 실패 사유 : 요청 시, Header 에 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    public void 고객_회원의_특정_거래의_생성_정보_조회_실패_Token_Type() throws Exception {
        // given
        final String refreshToken = jwtBuilder.refreshJwtBuildOfCustomer();
        final String orderId = "1";

        // when
        final ResultActions resultActions = requestGetOrderCreateInformation(refreshToken, orderId);

        // then
        assertError(UserErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 고객 회원의 특정 거래의 생성 정보 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    public void 고객_회원의_특정_거래의_생성_정보_조회_실패_Expired_Access_Token() throws Exception {
        // given
        final String expiredAccessToken = jwtBuilder.expiredAccessJwtBuild();
        final String orderId = "1";

        // when
        final ResultActions resultActions = requestGetOrderCreateInformation(expiredAccessToken, orderId);

        // then
        assertError(UserErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 고객 회원의 특정 거래의 생성 정보 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    public void 고객_회원의_특정_거래의_생성_정보_조회_실패_Invalid_Token() throws Exception {
        // given
        final String invalidToken = jwtBuilder.invalidJwtBuild();
        final String orderId = "1";

        // when
        final ResultActions resultActions = requestGetOrderCreateInformation(invalidToken, orderId);

        // then
        assertError(UserErrorCode.INVALID_JWT, resultActions);
    }

    /**
     * 고객 회원의 특정 거래의 생성 정보 조회 실패
     * - 실패 사유 : 거래에 대한 접근 권한이 없음
     */
    @Test
    public void 고객_회원의_특정_거래의_생성_정보_조회_실패_거래_접근_권한() throws Exception {
        // given
        final String accessTokenOfUser2 = jwtBuilder.accessJwtBuildOfSocialCustomer();
        final String orderId = "1";

        // when
        final ResultActions resultActions = requestGetOrderCreateInformation(accessTokenOfUser2, orderId);

        // then
        assertError(OrderErrorCode.DENIED_ACCESS_TO_ORDER, resultActions);
    }

    private ResultActions requestGetOrderHistory(String accessToken) throws Exception {
        return mvc.perform(get("/customer/order")
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }

    private ResultActions requestGetOrderHistoryWithOutAccessToken() throws Exception {
        return mvc.perform(get("/customer/order"))
                .andDo(print());
    }

    private ResultActions requestGetOrderIsCompletedHistory(String accessToken) throws Exception {
        return mvc.perform(get("/customer/order/history")
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }

    private ResultActions requestGetOrderIsCompletedHistoryWithOutAccessToken() throws Exception {
        return mvc.perform(get("/customer/order/history"))
                .andDo(print());
    }

    private ResultActions requestGetOrderCreateInformation(String accessToken, String orderId) throws Exception {
        return mvc.perform(get("/customer/order/history/{orderId}", orderId)
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }

    private ResultActions requestGetOrderCreateInformationWithOutAccessToken(String orderId) throws Exception {
        return mvc.perform(get("/customer/order/history/{orderId}", orderId))
                .andDo(print());
    }
}
