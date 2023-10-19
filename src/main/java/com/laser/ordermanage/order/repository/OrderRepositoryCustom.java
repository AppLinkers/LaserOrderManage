package com.laser.ordermanage.order.repository;

import com.laser.ordermanage.customer.dto.response.GetOrderRes;
import com.laser.ordermanage.factory.dto.response.GetOrderReIssueRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderRepositoryCustom {

    Page<GetOrderRes> findByCustomer(String userName, Pageable pageable, List<String> stageRequestList, List<String> manufacturingRequestList, String query);

    Page<GetOrderReIssueRes> findNewReIssueByFactory(String username, Pageable pageable, Boolean hasQuotation, Boolean isUrgent);
}
