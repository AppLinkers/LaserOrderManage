package com.laser.ordermanage.order.repository;

import com.laser.ordermanage.customer.dto.response.GetCustomerOrderRes;
import com.laser.ordermanage.factory.dto.response.GetFactoryOrderRes;
import com.laser.ordermanage.factory.dto.response.GetNewIssueNewOrderRes;
import com.laser.ordermanage.factory.dto.response.GetReIssueNewOrderRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepositoryCustom {

    Page<GetCustomerOrderRes> findByCustomer(String userName, Pageable pageable, List<String> stageRequestList, List<String> manufacturingRequestList, String query);

    Page<GetReIssueNewOrderRes> findReIssueNewByFactory(Pageable pageable, Boolean hasQuotation, Boolean isUrgent);

    Page<GetNewIssueNewOrderRes> findNewIssueNewByFactory(Pageable pageable, Boolean hasQuotation, Boolean isNewCustomer, Boolean isUrgent);

    Page<GetFactoryOrderRes> findByFactory(Pageable pageable, Boolean isCompleted, Boolean isUrgent, String dateCriterion, LocalDate startDate, LocalDate endDate, String query);
}
