package com.laser.ordermanage.order.unit.api;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.laser.ordermanage.common.APIUnitTest;
import com.laser.ordermanage.common.exception.CommonErrorCode;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.order.api.OrderAPI;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

        // 응답 본문을 객체로 변환
        final GetOrderDetailResponse actualResponse = objectMapper.registerModule(new JavaTimeModule()).readValue(responseString, GetOrderDetailResponse.class);

        // 응답 객체와 예상 객체 비교
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

    private ResultActions requestGetOrderDetail(String accessToken, String orderId) throws Exception {
        return mvc.perform(get("/order/{order-id}/detail", orderId)
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }

}
