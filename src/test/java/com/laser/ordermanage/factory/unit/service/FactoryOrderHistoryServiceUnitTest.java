package com.laser.ordermanage.factory.unit.service;

import com.laser.ordermanage.common.ServiceUnitTest;
import com.laser.ordermanage.common.paging.PageResponse;
import com.laser.ordermanage.factory.dto.response.*;
import com.laser.ordermanage.factory.service.FactoryOrderHistoryService;
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

public class FactoryOrderHistoryServiceUnitTest extends ServiceUnitTest {

    @InjectMocks
    FactoryOrderHistoryService factoryOrderHistoryService;

    @Mock
    private OrderRepository orderRepository;

    /**
     * 견적 대기 단계 및 재 발행 거래 목록 조회 성공
     */
    @Test
    public void getOrderIsNewAndIsReIssueHistory_성공() {
        // given
        final Pageable pageable = PageRequest.of(0, 10);

        final List<FactoryGetOrderIsNewAndIsReIssueHistoryResponse> expectedOrderList = FactoryGetOrderIsNewAndIsReIssueHistoryResponseBuilder.build();
        final Page<FactoryGetOrderIsNewAndIsReIssueHistoryResponse> expectedOrderPage =  new PageImpl<>(expectedOrderList, pageable, expectedOrderList.size());
        final PageResponse<FactoryGetOrderIsNewAndIsReIssueHistoryResponse> expectedResponse = new PageResponse<>(expectedOrderPage);

        // stub
        when(orderRepository.findIsNewAndIsReIssueByFactory(pageable, null, null)).thenReturn(expectedOrderPage);

        // when
        final PageResponse<FactoryGetOrderIsNewAndIsReIssueHistoryResponse> actualResponse = factoryOrderHistoryService.getOrderIsNewAndIsReIssueHistory(pageable, null, null);

        // then
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 견적 대기 단계 및 신규 발행 거래 목록 조회 성공
     */
    @Test
    public void getOrderIsNewAndIsNewIssueHistory_성공() {
        // given
        final Pageable pageable = PageRequest.of(0, 10);

        final List<FactoryGetOrderIsNewAndIsNewIssueHistoryResponse> expectedOrderList = FactoryGetOrderIsNewAndIsNewIssueHistoryResponseBuilder.build();
        final Page<FactoryGetOrderIsNewAndIsNewIssueHistoryResponse> expectedOrderPage = new PageImpl<>(expectedOrderList, pageable, expectedOrderList.size());
        final PageResponse<FactoryGetOrderIsNewAndIsNewIssueHistoryResponse> expectedResponse = new PageResponse<>(expectedOrderPage);

        // stub
        when(orderRepository.findIsNewAndIsNewIssueByFactory(pageable, null, null, null)).thenReturn(expectedOrderPage);

        // when
        final PageResponse<FactoryGetOrderIsNewAndIsNewIssueHistoryResponse> actualResponse = factoryOrderHistoryService.getOrderIsNewAndIsNewIssueHistory(pageable, null, null, null);

        // then
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 거래 목록 조회 성공
     */
    @Test
    public void getOrderHistory_성공() {
        // given
        final Pageable pageable = PageRequest.of(0, 10);

        final List<FactoryGetOrderHistoryResponse> expectedOrderList = FactoryGetOrderHistoryResponseBuilder.buildOfIsCompletedTrue();
        final Page<FactoryGetOrderHistoryResponse> expectedOrderPage = new PageImpl<>(expectedOrderList, pageable, expectedOrderList.size());
        final PageResponse<FactoryGetOrderHistoryResponse> expectedResponse = new PageResponse<>(expectedOrderPage);

        // stub
        when(orderRepository.findByFactory(pageable, Boolean.TRUE, null, null, null, null, null)).thenReturn(expectedOrderPage);

        // when
        final PageResponse<FactoryGetOrderHistoryResponse> actualResponse = factoryOrderHistoryService.getOrderHistory(pageable, Boolean.TRUE, null, null, null, null, null);

        // then
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }
}
