package com.laser.ordermanage.factory.service;

import com.laser.ordermanage.common.paging.PageResponse;
import com.laser.ordermanage.factory.dto.response.FactoryGetOrderHistoryResponse;
import com.laser.ordermanage.factory.dto.response.FactoryGetOrderIsNewAndIsNewIssueHistoryResponse;
import com.laser.ordermanage.factory.dto.response.FactoryGetOrderIsNewAndIsReIssueHistoryResponse;
import com.laser.ordermanage.order.repository.OrderRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class FactoryOrderHistoryService {

    private final OrderRepositoryCustom orderRepository;

    @Transactional(readOnly = true)
    public PageResponse<FactoryGetOrderIsNewAndIsReIssueHistoryResponse> getOrderIsNewAndIsReIssueHistory(Pageable pageable, Boolean hasQuotation, Boolean isUrgent) {
        Page<FactoryGetOrderIsNewAndIsReIssueHistoryResponse> reIssueNewOrderPage = orderRepository.findIsNewAndIsReIssueByFactory(pageable, hasQuotation, isUrgent);

        return new PageResponse<>(reIssueNewOrderPage);
    }

    @Transactional(readOnly = true)
    public PageResponse<FactoryGetOrderIsNewAndIsNewIssueHistoryResponse> getOrderIsNewAndIsNewIssueHistory(Pageable pageable, Boolean hasQuotation, Boolean isNewCustomer, Boolean isUrgent) {
        Page<FactoryGetOrderIsNewAndIsNewIssueHistoryResponse> newIssueNewOrderPage = orderRepository.findIsNewAndIsNewIssueByFactory(pageable, hasQuotation, isNewCustomer, isUrgent);

        return new PageResponse<>(newIssueNewOrderPage);
    }

    @Transactional(readOnly = true)
    public PageResponse<FactoryGetOrderHistoryResponse> getOrderHistory(Pageable pageable, Boolean isCompleted, Boolean isUrgent, String dateCriterion, LocalDate startDate, LocalDate endDate, String query) {
        Page<FactoryGetOrderHistoryResponse> factoryOrderPage = orderRepository.findByFactory(pageable, isCompleted, isUrgent, dateCriterion, startDate, endDate, query);

        return new PageResponse<>(factoryOrderPage);
    }
}
