package com.laser.ordermanage.customer.unit.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.laser.ordermanage.common.APIUnitTest;
import com.laser.ordermanage.common.exception.CommonErrorCode;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.paging.PageResponse;
import com.laser.ordermanage.customer.api.CustomerOrderHistoryAPI;
import com.laser.ordermanage.customer.dto.response.*;
import com.laser.ordermanage.customer.service.CustomerOrderHistoryService;
import com.laser.ordermanage.customer.service.CustomerOrderService;
import com.laser.ordermanage.order.exception.OrderErrorCode;
import com.laser.ordermanage.user.exception.UserErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerOrderHistoryAPI.class)
public class CustomerOrderHistoryAPIUnitTest extends APIUnitTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private CustomerOrderService customerOrderService;

    @MockBean
    private CustomerOrderHistoryService customerOrderHistoryService;

    private final static Pageable pageable = PageRequest.of(0, 10);

    @BeforeEach
    public void setUp() {
        mvc = buildMockMvc(context);
    }

    /**
     * 고객 회원의 거래 목록 조회 성공
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_회원의_거래_목록_조회_성공() throws Exception {
        // given
        final String accessToken = "access-token";
        final List<CustomerGetOrderHistoryResponse> expectedOrderList = CustomerGetOrderHistoryResponseBuilder.buildListOfCustomer1();
        final PageResponse<CustomerGetOrderHistoryResponse> expectedResponse = new PageResponse<>(new PageImpl<>(expectedOrderList, pageable, expectedOrderList.size()));

        // stub
        when(customerOrderHistoryService.getOrderHistory(any(), any(), any(), any(), any())).thenReturn(expectedResponse);

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
     * - 실패 사유 : 공장 역할 (FACTORY)에 의한 요청
     */
    @Test
    @WithMockUser(roles = {"FACTORY"})
    public void 고객_회원의_거래_목록_조회_실패_역할() throws Exception {
        // given
        final String accessToken = "access-token";

        // when
        final ResultActions resultActions = requestGetOrderHistory(accessToken);

        // then
        assertError(UserErrorCode.DENIED_ACCESS, resultActions);
    }

    /**
     * 고객 회원의 거래 목록 조회 실패
     * - 실패 사유 : query 파라미터 유효성
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_회원의_거래_목록_조회_실패_query_파라미터_유효성() throws Exception {
        // given
        final String accessToken = "access-token";
        final String invalidQuery = "거래 이름".repeat(5);

        // when
        final ResultActions resultActions = requestGetOrderHistory(accessToken, invalidQuery);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_PARAMETER, resultActions, "거래 이름의 최대 글자수는 20자입니다.");
    }

    /**
     * 고객 회원의 거래 완료 단계인 거래 목록 조회 성공
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_회원의_거래_완료_목록_조회_성공() throws Exception {
        // given
        final String accessToken = "access-token";
        final List<CustomerGetOrderIsCompletedHistoryResponse> expectedOrderList = CustomerGetOrderIsCompletedHistoryResponseBuilder.build();
        final PageResponse<CustomerGetOrderIsCompletedHistoryResponse> expectedResponse = new PageResponse<>(new PageImpl<>(expectedOrderList, pageable, expectedOrderList.size()));

        // stub
        when(customerOrderHistoryService.getOrderIsCompletedHistory(any(), any(), any())).thenReturn(expectedResponse);

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
     * - 실패 사유 : 공장 역할 (FACTORY)에 의한 요청
     */
    @Test
    @WithMockUser(roles = {"FACTORY"})
    public void 고객_회원의_거래_완료_목록_조회_실패_역할() throws Exception {
        // given
        final String accessToken = "access-token";

        // when
        final ResultActions resultActions = requestGetOrderIsCompletedHistory(accessToken);

        // then
        assertError(UserErrorCode.DENIED_ACCESS, resultActions);
    }

    /**
     * 고객 회원의 거래 완료 단계인 거래 목록 조회 실패
     * - 실패 사유 : query 파라미터 유효성
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_회원의_거래_완료_목록_조회_실패_query_파라미터_유효성() throws Exception {
        // given
        final String accessToken = "access-token";
        final String invalidQuery = "거래 이름".repeat(5);

        // when
        final ResultActions resultActions = requestGetOrderIsCompletedHistory(accessToken, invalidQuery);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_PARAMETER, resultActions, "거래 이름의 최대 글자수는 20자입니다.");
    }

    /**
     * 고객 회원의 특정 거래의 생성 정보 조회 성공
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_회원의_특정_거래의_생성_정보_조회_성공() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";
        final CustomerGetOrderCreateInformationResponse expectedResponse = CustomerGetOrderCreateInformationResponseBuilder.build();

        // stub
        when(customerOrderHistoryService.getOrderCreateInformation((any()))).thenReturn(expectedResponse);

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
     * - 실패 사유 : 공장 역할 (FACTORY)에 의한 요청
     */
    @Test
    @WithMockUser(roles = {"FACTORY"})
    public void 고객_회원의_특정_거래의_생성_정보_조회_실패_역할() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";

        // when
        final ResultActions resultActions = requestGetOrderCreateInformation(accessToken, orderId);

        // then
        assertError(UserErrorCode.DENIED_ACCESS, resultActions);
    }

    /**
     * 고객 회원의 특정 거래의 생성 정보 조회 실패
     * - 실패 사유 : order-id 파라미터 타입
     */
    @Test
    @WithMockUser(roles = "CUSTOMER")
    public void 고객_회원의_특정_거래의_생성_정보_조회_실패_order_id_파라미터_타입() throws Exception {
        // given
        final String accessToken = "access-token";
        final String invalidOrderId = "invalid-order-id";

        // when
        final ResultActions resultActions = requestGetOrderCreateInformation(accessToken, invalidOrderId);

        // then
        assertErrorWithMessage(CommonErrorCode.MISMATCH_PARAMETER_TYPE, resultActions, "order-id");
    }

    /**
     * 고객 회원의 특정 거래의 생성 정보 조회 실패
     * - 실패 사유 : 거래에 대한 접근 권한이 없음
     */
    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void 고객_회원의_특정_거래의_생성_정보_조회_실패_거래_접근_권한() throws Exception {
        // given
        final String accessToken = "access-token";
        final String orderId = "1";

        // stub
        doThrow(new CustomCommonException(OrderErrorCode.DENIED_ACCESS_TO_ORDER)).when(customerOrderService).checkAuthorityOfOrder(any(), any());

        // when
        final ResultActions resultActions = requestGetOrderCreateInformation(accessToken, orderId);

        // then
        assertError(OrderErrorCode.DENIED_ACCESS_TO_ORDER, resultActions);
    }

    private ResultActions requestGetOrderHistory(String accessToken) throws Exception {
        return mvc.perform(get("/customer/order")
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }

    private ResultActions requestGetOrderHistory(String accessToken, String query) throws Exception {
        return mvc.perform(get("/customer/order")
                        .param("query", query)
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }

    private ResultActions requestGetOrderIsCompletedHistory(String accessToken) throws Exception {
        return mvc.perform(get("/customer/order/history")
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }

    private ResultActions requestGetOrderIsCompletedHistory(String accessToken, String query) throws Exception {
        return mvc.perform(get("/customer/order/history")
                        .param("query", query)
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }

    private ResultActions requestGetOrderCreateInformation(String accessToken, String orderId) throws Exception {
        return mvc.perform(get("/customer/order/history/{orderId}", orderId)
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }
}
