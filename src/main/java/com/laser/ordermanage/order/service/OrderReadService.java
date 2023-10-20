package com.laser.ordermanage.order.service;

import com.laser.ordermanage.common.dto.response.PageRes;
import com.laser.ordermanage.customer.dto.response.GetCustomerOrderRes;
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
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderReadService {

    private final OrderRepositoryCustom orderRepositoryCustom;

    @Transactional(readOnly = true)
    public PageRes<GetCustomerOrderRes> readByCustomer(String userName, Pageable pageable, List<String> stageRequestList, List<String> manufacturingRequestList, String query) {
        Page<GetCustomerOrderRes> customerOrderPage = orderRepositoryCustom.findByCustomer(userName, pageable, stageRequestList, manufacturingRequestList, query);

        return new PageRes<>(customerOrderPage);
    }

    public PageRes<GetReIssueNewOrderRes> readReIssueNewByFactory(Pageable pageable, Boolean hasQuotation, Boolean isUrgent) {
        Page<GetReIssueNewOrderRes> reIssueNewOrderPage = orderRepositoryCustom.findReIssueNewByFactory(pageable, hasQuotation, isUrgent);

        return new PageRes<>(reIssueNewOrderPage);
    }

    public PageRes<GetNewIssueNewOrderRes> readNewIssueNewByFactory(Pageable pageable, Boolean hasQuotation, Boolean isNewCustomer, Boolean isUrgent) {
        Page<GetNewIssueNewOrderRes> newIssueNewOrderPage = orderRepositoryCustom.findNewIssueNewByFactory(pageable, hasQuotation, isNewCustomer, isUrgent);

        return new PageRes<>(newIssueNewOrderPage);
    }

    public PageRes<GetFactoryOrderRes> readByFactory(Pageable pageable, Boolean isCompleted, Boolean isUrgent, String dateCriterion, LocalDate startDate, LocalDate endDate, String query) {
        Page<GetFactoryOrderRes> factoryOrderPage = orderRepositoryCustom.findByFactory(pageable, isCompleted, isUrgent, dateCriterion, startDate, endDate, query);

        return new PageRes<>(factoryOrderPage);
    }
}
