package com.laser.ordermanage.order.unit.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.laser.ordermanage.common.APIUnitTest;
import com.laser.ordermanage.common.exception.CommonErrorCode;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.paging.ListResponse;
import com.laser.ordermanage.order.api.OrderAPI;
import com.laser.ordermanage.order.domain.type.Stage;
import com.laser.ordermanage.order.dto.request.CreateCommentRequest;
import com.laser.ordermanage.order.dto.request.CreateCommentRequestBuilder;
import com.laser.ordermanage.order.dto.response.*;
import com.laser.ordermanage.order.exception.OrderErrorCode;
import com.laser.ordermanage.order.service.OrderEmailService;
import com.laser.ordermanage.order.service.OrderService;
import com.laser.ordermanage.user.exception.UserErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderAPI.class)
public class OrderAPIUnitTest extends APIUnitTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private OrderService orderService;

    @MockBean
    private OrderEmailService orderEmailService;

    @BeforeEach
    public void setUp() {
        mvc = buildMockMvc(context);
    }


    /**
     * 거래의 상세 정보 조회 성공
     */
    @Test
    @WithMockUser
    public void 거래_상세_정보_조회_성공() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final GetOrderDetailResponse expectedResponse = GetOrderDetailResponseBuilder.build();

        // stub
        when(orderService.getOrderDetail(any())).thenReturn(expectedResponse);

        // when
        final ResultActions resultActions = requestGetOrderDetail(accessToken, orderId);

        // then
        final String responseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final GetOrderDetailResponse actualResponse = objectMapper.registerModule(new JavaTimeModule()).readValue(responseString, GetOrderDetailResponse.class);

        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 거래의 상세 정보 조회 실패
     * - 실패 사유 : order-id 파라미터 타입
     */
    @Test
    @WithMockUser
    public void 거래_상세_정보_조회_실패_order_id_파라미터_타입() throws Exception {
        // given
        final String accessToken = "access-token";
        final String invalidOrderId = "invalid-order-id";

        // when
        final ResultActions resultActions = requestGetOrderDetail(accessToken, invalidOrderId);

        // then
        assertErrorWithMessage(CommonErrorCode.MISMATCH_PARAMETER_TYPE, resultActions, "order-id");
    }

    /**
     * 거래의 상세 정보 조회 실패
     * - 실패 사유 : 거래에 대한 접근 권한이 없음
     */
    @Test
    @WithMockUser
    public void 거래_상세_정보_조회_실패_거래접근권한() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";

        // stub
        doThrow(new CustomCommonException(OrderErrorCode.DENIED_ACCESS_TO_ORDER)).when(orderService).checkAuthorityCustomerOfOrderOrFactory(any(), any());

        // when
        final ResultActions resultActions = requestGetOrderDetail(accessToken, orderId);

        // then
        assertError(OrderErrorCode.DENIED_ACCESS_TO_ORDER, resultActions);
    }

    /**
     * 거래의 댓글 목록 조회 성공
     */
    @Test
    @WithMockUser
    public void 거래_댓글_목록_조회_성공() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final ListResponse<GetCommentResponse> expectedResponse = new ListResponse<>(GetCommentResponseBuilder.buildListForOrder1());

        // stub
        when(orderService.getCommentByOrder(any())).thenReturn(expectedResponse);
        // when
        final ResultActions resultActions = requestGetOrderComment(accessToken, orderId);

        // then
        final String responseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final ListResponse<GetCommentResponse> actualResponse = objectMapper.registerModule(new JavaTimeModule()).readValue(responseString, new TypeReference<ListResponse<GetCommentResponse>>() {});

        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 거래의 댓글 목록 조회 실패
     * - 실패 사유 : order-id 파라미터 타입
     */
    @Test
    @WithMockUser
    public void 거래_댓글_목록_조회_실패_order_id_파라미터_타입() throws Exception {
        // given
        final String accessToken = "access-token";
        final String invalidOrderId = "invalid-order-id";

        // when
        final ResultActions resultActions = requestGetOrderComment(accessToken, invalidOrderId);

        // then
        assertErrorWithMessage(CommonErrorCode.MISMATCH_PARAMETER_TYPE, resultActions, "order-id");
    }

    /**
     * 거래의 댓글 목록 조회 실패
     * - 실패 사유 : 거래에 대한 접근 권한이 없음
     */
    @Test
    @WithMockUser
    public void 거래_댓글_목록_조회_실패_거래접근권한() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";

        // stub
        doThrow(new CustomCommonException(OrderErrorCode.DENIED_ACCESS_TO_ORDER)).when(orderService).checkAuthorityCustomerOfOrderOrFactory(any(), any());

        // when
        final ResultActions resultActions = requestGetOrderComment(accessToken, orderId);

        // then
        assertError(OrderErrorCode.DENIED_ACCESS_TO_ORDER, resultActions);
    }

    /**
     * 거래에 댓글 작성 성공
     */
    @Test
    @WithMockUser(authorities = {"AUTHORITY_ADMIN"})
    public void 거래_댓글_작성_성공() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final CreateCommentRequest request = CreateCommentRequestBuilder.build();

        // stub
        when(orderService.createOrderComment(any(), any(), any())).thenReturn(Long.valueOf(orderId));

        // when
        final ResultActions resultActions = requestCreateComment(accessToken, orderId, request);

        // then
        resultActions.andExpect(status().isOk());
    }

    /**
     * 거래에 댓글 작성 실패
     * - 실패 사유 : 관리자 권한(Authority Admin)이 없음
     */
    @Test
    @WithMockUser
    public void 거래_댓글_작성_실패_사용자_권한() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final CreateCommentRequest request = CreateCommentRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateComment(accessToken, orderId, request);

        // then
        assertError(UserErrorCode.DENIED_ACCESS, resultActions);
    }

    /**
     * 거래에 댓글 작성 실패
     * - 실패 사유 : order-id 파라미터 타입
     */
    @Test
    @WithMockUser
    public void 거래_댓글_작성_실패_order_id_파라미터_타입() throws Exception {
        // given
        final String accessToken = "access-token";
        final String invalidOrderId = "invalid-order-id";
        final CreateCommentRequest request = CreateCommentRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateComment(accessToken, invalidOrderId, request);

        // then
        assertErrorWithMessage(CommonErrorCode.MISMATCH_PARAMETER_TYPE, resultActions, "order-id");
    }

    /**
     * 거래에 댓글 작성 실패
     * - 실패 사유 : 댓글 내용 필드 null
     */
    @Test
    @WithMockUser
    public void 거래_댓글_작성_실패_댓글_내용_필드_null() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final CreateCommentRequest request = CreateCommentRequestBuilder.nullContentBuild();

        // when
        final ResultActions resultActions = requestCreateComment(accessToken, orderId, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "댓글 내용은 필수 입력값입니다.");
    }

    /**
     * 거래에 댓글 작성 실패
     * - 실패 사유 : 댓글 내용 필드 empty
     */
    @Test
    @WithMockUser
    public void 거래_댓글_작성_실패_댓글_내용_필드_empty() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final CreateCommentRequest request = CreateCommentRequestBuilder.emptyContentBuild();

        // when
        final ResultActions resultActions = requestCreateComment(accessToken, orderId, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "댓글 내용은 필수 입력값입니다.");
    }

    /**
     * 거래에 댓글 작성 실패
     * - 실패 사유 : 댓글 내용 필드 유효성
     */
    @Test
    @WithMockUser
    public void 거래_댓글_작성_실패_댓글_내용_필드_유효성() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final CreateCommentRequest request = CreateCommentRequestBuilder.invalidContentBuild();

        // when
        final ResultActions resultActions = requestCreateComment(accessToken, orderId, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "댓글 내용의 최대 글자수는 200자입니다.");
    }

    /**
     * 거래에 댓글 작성 실패
     * - 실패 사유 : 거래에 대한 접근 권한이 없음
     */
    @Test
    @WithMockUser(authorities = {"AUTHORITY_ADMIN"})
    public void 거래_댓글_작성_실패_거래접근권한() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final CreateCommentRequest request = CreateCommentRequestBuilder.build();

        // stub
        doThrow(new CustomCommonException(OrderErrorCode.DENIED_ACCESS_TO_ORDER)).when(orderService).checkAuthorityCustomerOfOrderOrFactory(any(), any());

        // when
        final ResultActions resultActions = requestCreateComment(accessToken, orderId, request);

        // then
        assertError(OrderErrorCode.DENIED_ACCESS_TO_ORDER, resultActions);
    }

    /**
     * 거래에 댓글 작성 실패
     * - 실패 사유 : orderId 에 해당하는 거래가 존재하지 않음
     */
    @Test
    @WithMockUser(authorities = "AUTHORITY_ADMIN")
    public void 거래_댓글_작성_실패_존재하지_않는_거래() throws Exception {
        // given
        final String accessToken = "access-token";
        final String unknownOrderId = "0";
        final CreateCommentRequest request = CreateCommentRequestBuilder.build();

        // stub
        when(orderService.createOrderComment(any(), any(), any())).thenThrow(new CustomCommonException(OrderErrorCode.NOT_FOUND_ORDER));

        // when
        final ResultActions resultActions = requestCreateComment(accessToken, unknownOrderId, request);

        // then
        assertError(OrderErrorCode.NOT_FOUND_ORDER, resultActions);
    }

    /**
     * 거래 삭제 성공
     */
    @Test
    @WithMockUser(authorities = "AUTHORITY_ADMIN")
    public void 거래_삭제_성공() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "5";
        final DeleteOrderResponse deleteOrderResponse = DeleteOrderResponseBuilder.build();

        // stub
        when(orderService.deleteOrder(any())).thenReturn(deleteOrderResponse);

        // when
        final ResultActions resultActions = requestDeleteOrder(accessToken, orderId);

        // then
        resultActions.andExpect(status().isOk());
    }

    /**
     * 거래 삭제 실패
     * - 실패 사유 : 관리자 권한(Authority Admin)이 없음
     */
    @Test
    @WithMockUser
    public void 거래_삭제_실패_사용자_권한() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";

        // when
        final ResultActions resultActions = requestDeleteOrder(accessToken, orderId);

        // then
        assertError(UserErrorCode.DENIED_ACCESS, resultActions);
    }

    /**
     * 거래 삭제 실패
     * - 실패 사유 : order-id 파라미터 타입
     */
    @Test
    @WithMockUser
    public void 거래_삭제_실패_order_id_파라미터_타입() throws Exception {
        // given
        final String accessToken = "access-token";
        final String invalidOrderId = "invalid-order-id";

        // when
        final ResultActions resultActions = requestDeleteOrder(accessToken, invalidOrderId);

        // then
        assertErrorWithMessage(CommonErrorCode.MISMATCH_PARAMETER_TYPE, resultActions, "order-id");
    }

    /**
     * 거래 삭제 실패
     * - 실패 사유 : 거래에 대한 접근 권한이 없음
     */
    @Test
    @WithMockUser(authorities = "AUTHORITY_ADMIN")
    public void 거래_삭제_실패_거래접근권한() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";

        // stub
        doThrow(new CustomCommonException(OrderErrorCode.DENIED_ACCESS_TO_ORDER)).when(orderService).checkAuthorityCustomerOfOrderOrFactory(any(), any());

        // when
        final ResultActions resultActions = requestDeleteOrder(accessToken, orderId);

        // then
        assertError(OrderErrorCode.DENIED_ACCESS_TO_ORDER, resultActions);
    }

    /**
     * 거래 삭제 실패
     * - 실패 사유 : 거래의 단계가 삭제 가능 단계(견적 대기, 견적 승인)가 아님
     */
    @Test
    @WithMockUser(authorities = "AUTHORITY_ADMIN")
    public void 거래_삭제_실패_거래삭제_가능단계() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";

        // stub
        doThrow(new CustomCommonException(OrderErrorCode.INVALID_ORDER_STAGE, Stage.COMPLETED.getValue())).when(orderService).deleteOrder(any());

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

    private ResultActions requestGetOrderComment(String accessToken, String orderId) throws Exception {
        return mvc.perform(get("/order/{order-id}/comment", orderId)
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }

    private ResultActions requestCreateComment(String accessToken, String orderId, CreateCommentRequest request) throws Exception {
        return mvc.perform(post("/order/{order-id}/comment", orderId)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }

    private ResultActions requestDeleteOrder(String accessToken, String orderId) throws Exception {
        return mvc.perform(delete("/order/{order-id}", orderId)
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }

}
