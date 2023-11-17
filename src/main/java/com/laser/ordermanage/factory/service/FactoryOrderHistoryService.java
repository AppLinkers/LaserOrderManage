package com.laser.ordermanage.factory.service;

import com.laser.ordermanage.common.paging.PageResponse;
import com.laser.ordermanage.factory.dto.response.FactoryGetOrderHistoryResponse;
import com.laser.ordermanage.factory.dto.response.FactoryGetOrderIsNewAndIsNewIssueHistoryResponse;
import com.laser.ordermanage.factory.dto.response.FactoryGetOrderIsNewAndIsReIssueHistoryResponse;
import com.laser.ordermanage.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class FactoryOrderHistoryService {

    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public PageResponse<FactoryGetOrderIsNewAndIsReIssueHistoryResponse> getOrderIsNewAndIsReIssueHistory(Pageable pageable, Boolean hasQuotation, Boolean isUrgent) {
        Page<FactoryGetOrderIsNewAndIsReIssueHistoryResponse> factoryGetOrderIsNewAndIsReIssueHistoryResponsePage = orderRepository.findIsNewAndIsReIssueByFactory(pageable, hasQuotation, isUrgent);

        return new PageResponse<>(factoryGetOrderIsNewAndIsReIssueHistoryResponsePage);
    }

    @Transactional(readOnly = true)
    public PageResponse<FactoryGetOrderIsNewAndIsNewIssueHistoryResponse> getOrderIsNewAndIsNewIssueHistory(Pageable pageable, Boolean hasQuotation, Boolean isNewCustomer, Boolean isUrgent) {
        Page<FactoryGetOrderIsNewAndIsNewIssueHistoryResponse> factoryGetOrderIsNewAndIsNewIssueHistoryResponsePage = orderRepository.findIsNewAndIsNewIssueByFactory(pageable, hasQuotation, isNewCustomer, isUrgent);

        return new PageResponse<>(factoryGetOrderIsNewAndIsNewIssueHistoryResponsePage);
    }

    @Transactional(readOnly = true)
    public PageResponse<FactoryGetOrderHistoryResponse> getOrderHistory(Pageable pageable, Boolean isCompleted, Boolean isUrgent, String dateCriterion, LocalDate startDate, LocalDate endDate, String query) {
        Page<FactoryGetOrderHistoryResponse> factoryGetOrderHistoryResponsePage = orderRepository.findByFactory(pageable, isCompleted, isUrgent, dateCriterion, startDate, endDate, query);

        return new PageResponse<>(factoryGetOrderHistoryResponsePage);
    }
}
