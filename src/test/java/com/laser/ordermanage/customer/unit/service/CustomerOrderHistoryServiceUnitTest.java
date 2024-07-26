package com.laser.ordermanage.customer.unit.service;

import com.laser.ordermanage.common.ServiceUnitTest;
import com.laser.ordermanage.common.paging.PageResponse;
import com.laser.ordermanage.customer.dto.response.*;
import com.laser.ordermanage.customer.service.CustomerOrderHistoryService;
import com.laser.ordermanage.order.repository.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.mockito.Mockito.when;

public class CustomerOrderHistoryServiceUnitTest extends ServiceUnitTest {

    @InjectMocks
    CustomerOrderHistoryService customerOrderHistoryService;

    @Mock
    private OrderRepository orderRepository;

    /**
     * 고객 회원의 거래 목록 조회 성공
     */
    @Test
    public void getOrderHistory_성공() {
        // given
        final String email = "user@gmail.com";
        final Pageable pageable = PageRequest.of(0, 10);
        final List<CustomerGetOrderHistoryResponse> expectedOrderList = CustomerGetOrderHistoryResponseBuilder.buildListOfCustomer1();
        final Page<CustomerGetOrderHistoryResponse> expectedOrderPage = new PageImpl<>(expectedOrderList, pageable, expectedOrderList.size());
        final PageResponse<CustomerGetOrderHistoryResponse> expectedResponse = new PageResponse<>(expectedOrderPage);

        // stub
        when(orderRepository.findByCustomer(email, pageable, null, null, null)).thenReturn(expectedOrderPage);

        // when
        final PageResponse<CustomerGetOrderHistoryResponse> actualResponse = customerOrderHistoryService.getOrderHistory(email, pageable, null, null, null);

        // then
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 고객 회원의 거래 완료 단계인 거래 목록 조회 성공
     */
    @Test
    public void getOrderIsCompletedHistory_성공() {
        // given
        final String email = "user@gmail.com";
        final Pageable pageable = PageRequest.of(0, 10);
        final List<CustomerGetOrderIsCompletedHistoryResponse> expectedOrderList = CustomerGetOrderIsCompletedHistoryResponseBuilder.build();
        final Page<CustomerGetOrderIsCompletedHistoryResponse> expectedOrderPage = new PageImpl<>(expectedOrderList, pageable, expectedOrderList.size());
        final PageResponse<CustomerGetOrderIsCompletedHistoryResponse> expectedResponse = new PageResponse<>(expectedOrderPage);

        // stub
        when(orderRepository.findIsCompletedByCustomer(email, pageable, null)).thenReturn(expectedOrderPage);

        // when
        final PageResponse<CustomerGetOrderIsCompletedHistoryResponse> actualResponse = customerOrderHistoryService.getOrderIsCompletedHistory(email, pageable, null);

        // then
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 고객 회원의 특정 거래의 생성 정보 조회 성공
     */
    @Test
    public void getOrderCreateInformation_성공() {
        // given
        final Long orderId = 1L;
        final CustomerGetOrderCreateInformationResponse expectedResponse = CustomerGetOrderCreateInformationResponseBuilder.build();

        // stub
        when(orderRepository.findCreateInformationByOrder(orderId)).thenReturn(expectedResponse);

        // when
        final CustomerGetOrderCreateInformationResponse actualResponse = customerOrderHistoryService.getOrderCreateInformation(orderId);

        // then
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }
}
