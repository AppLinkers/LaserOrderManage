package com.laser.ordermanage.factory.service;

import com.laser.ordermanage.common.dto.response.PageRes;
import com.laser.ordermanage.factory.dto.response.GetFactoryOrderRes;
import com.laser.ordermanage.factory.dto.response.GetNewIssueNewOrderRes;
import com.laser.ordermanage.factory.dto.response.GetReIssueNewOrderRes;
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
    public PageRes<GetReIssueNewOrderRes> getOrderIsNewAndIsReIssueHistory(Pageable pageable, Boolean hasQuotation, Boolean isUrgent) {
        Page<GetReIssueNewOrderRes> reIssueNewOrderPage = orderRepository.findIsNewAndIsReIssueByFactory(pageable, hasQuotation, isUrgent);

        return new PageRes<>(reIssueNewOrderPage);
    }

    @Transactional(readOnly = true)
    public PageRes<GetNewIssueNewOrderRes> getOrderIsNewAndIsNewIssueHistory(Pageable pageable, Boolean hasQuotation, Boolean isNewCustomer, Boolean isUrgent) {
        Page<GetNewIssueNewOrderRes> newIssueNewOrderPage = orderRepository.findIsNewAndIsNewIssueByFactory(pageable, hasQuotation, isNewCustomer, isUrgent);

        return new PageRes<>(newIssueNewOrderPage);
    }

    @Transactional(readOnly = true)
    public PageRes<GetFactoryOrderRes> getOrderHistory(Pageable pageable, Boolean isCompleted, Boolean isUrgent, String dateCriterion, LocalDate startDate, LocalDate endDate, String query) {
        Page<GetFactoryOrderRes> factoryOrderPage = orderRepository.findByFactory(pageable, isCompleted, isUrgent, dateCriterion, startDate, endDate, query);

        return new PageRes<>(factoryOrderPage);
    }
}
