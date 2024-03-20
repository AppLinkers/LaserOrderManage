package com.laser.ordermanage.order.unit.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.laser.ordermanage.common.APIUnitTest;
import com.laser.ordermanage.common.exception.CommonErrorCode;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.paging.ListResponse;
import com.laser.ordermanage.order.api.OrderAPI;
import com.laser.ordermanage.order.dto.request.CreateCommentRequest;
import com.laser.ordermanage.order.dto.request.CreateCommentRequestBuilder;
import com.laser.ordermanage.order.dto.response.GetCommentResponse;
import com.laser.ordermanage.order.dto.response.GetCommentResponseBuilder;
import com.laser.ordermanage.order.dto.response.GetOrderDetailResponse;
import com.laser.ordermanage.order.dto.response.GetOrderDetailResponseBuilder;
import com.laser.ordermanage.order.exception.OrderErrorCode;
import com.laser.ordermanage.order.service.OrderEmailService;
import com.laser.ordermanage.order.service.OrderService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        doNothing().when(orderService).checkAuthorityCustomerOfOrderOrFactory(any(), any());
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
        final String accessToken = "accessToken";
        final String orderId = "1";
        final ListResponse<GetCommentResponse> expectedResponse = new ListResponse<>(GetCommentResponseBuilder.buildCommentListForOrder1());

        // stub
        doNothing().when(orderService).checkAuthorityCustomerOfOrderOrFactory(any(), any());
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
    @WithMockUser
    public void 거래_댓글_작성_성공() throws Exception {
        // given
        final String accessToken = "accessToken";
        final String orderId = "1";
        final CreateCommentRequest request = CreateCommentRequestBuilder.build();

        // stub
        doNothing().when(orderService).checkAuthorityCustomerOfOrderOrFactory(any(), any());
        when(orderService.createOrderComment(any(), any(), any())).thenReturn(Long.valueOf(orderId));
        doNothing().when(orderEmailService).sendEmailForCreateOrderComment(any());

        // when
        final ResultActions resultActions = requestCreateComment(accessToken, orderId, request);

        // then
        resultActions.andExpect(status().isOk());
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
    @WithMockUser
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

}
