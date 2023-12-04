package com.laser.ordermanage.order.repository;

import com.laser.ordermanage.customer.dto.response.CustomerGetOrderCreateInformationResponse;
import com.laser.ordermanage.customer.dto.response.CustomerGetOrderHistoryResponse;
import com.laser.ordermanage.customer.dto.response.CustomerGetOrderIsCompletedHistoryResponse;
import com.laser.ordermanage.factory.dto.response.FactoryGetOrderHistoryResponse;
import com.laser.ordermanage.factory.dto.response.FactoryGetOrderIsNewAndIsNewIssueHistoryResponse;
import com.laser.ordermanage.factory.dto.response.FactoryGetOrderIsNewAndIsReIssueHistoryResponse;
import com.laser.ordermanage.order.dto.response.GetOrderDetailResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepositoryCustom {

    Page<CustomerGetOrderHistoryResponse> findByCustomer(String userName, Pageable pageable, List<String> stageRequestList, List<String> manufacturingRequestList, String query);

    Page<FactoryGetOrderIsNewAndIsReIssueHistoryResponse> findIsNewAndIsReIssueByFactory(Pageable pageable, Boolean hasQuotation, Boolean isUrgent);

    Page<FactoryGetOrderIsNewAndIsNewIssueHistoryResponse> findIsNewAndIsNewIssueByFactory(Pageable pageable, Boolean hasQuotation, Boolean isNewCustomer, Boolean isUrgent);

    Page<FactoryGetOrderHistoryResponse> findByFactory(Pageable pageable, Boolean isCompleted, Boolean isUrgent, String dateCriterion, LocalDate startDate, LocalDate endDate, String query);

    Page<CustomerGetOrderIsCompletedHistoryResponse> findIsCompletedByCustomer(String userName, Pageable pageable, String query);

    CustomerGetOrderCreateInformationResponse findCreateInformationByCustomerAndOrder(String userName, Long orderId);

    GetOrderDetailResponse findDetailByUserAndOrder(User user, Long orderId);
}
