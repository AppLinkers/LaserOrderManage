package com.laser.ordermanage.order.repository;

import com.laser.ordermanage.customer.dto.response.GetOrderRes;
import com.laser.ordermanage.factory.dto.response.GetNewIssueNewOrderRes;
import com.laser.ordermanage.factory.dto.response.GetReIssueNewOrderRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderRepositoryCustom {

    Page<GetOrderRes> findByCustomer(String userName, Pageable pageable, List<String> stageRequestList, List<String> manufacturingRequestList, String query);

    Page<GetReIssueNewOrderRes> findReIssueNewByFactory(String username, Pageable pageable, Boolean hasQuotation, Boolean isUrgent);

    Page<GetNewIssueNewOrderRes> findNewIssueNewByFactory(String userName, Pageable pageable, Boolean hasQuotation, Boolean isNewCustomer, Boolean isUrgent);
}
