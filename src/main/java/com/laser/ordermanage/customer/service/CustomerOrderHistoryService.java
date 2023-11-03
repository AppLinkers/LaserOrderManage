package com.laser.ordermanage.customer.service;

import com.laser.ordermanage.common.paging.PageResponse;
import com.laser.ordermanage.customer.dto.response.CustomerGetOrderHistoryResponse;
import com.laser.ordermanage.order.repository.OrderRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CustomerOrderHistoryService {

    private final OrderRepositoryCustom orderRepository;

    @Transactional(readOnly = true)
    public PageResponse<CustomerGetOrderHistoryResponse> getOrderHistory(String userName, Pageable pageable, List<String> stageRequestList, List<String> manufacturingRequestList, String query) {
        Page<CustomerGetOrderHistoryResponse> customerOrderPage = orderRepository.findByCustomer(userName, pageable, stageRequestList, manufacturingRequestList, query);

        return new PageResponse<>(customerOrderPage);
    }
}
