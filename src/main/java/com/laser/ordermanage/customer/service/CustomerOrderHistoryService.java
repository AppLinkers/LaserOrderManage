package com.laser.ordermanage.customer.service;

import com.laser.ordermanage.common.paging.PageResponse;
import com.laser.ordermanage.customer.dto.response.CustomerGetDrawingResponse;
import com.laser.ordermanage.customer.dto.response.CustomerGetOrderCreateInformationResponse;
import com.laser.ordermanage.customer.dto.response.CustomerGetOrderHistoryResponse;
import com.laser.ordermanage.customer.dto.response.CustomerGetOrderIsCompletedHistoryResponse;
import com.laser.ordermanage.order.repository.OrderRepository;
import com.laser.ordermanage.order.service.DrawingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CustomerOrderHistoryService {

    private final DrawingService drawingService;

    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public PageResponse<CustomerGetOrderHistoryResponse> getOrderHistory(String userName, Pageable pageable, List<String> stageRequestList, List<String> manufacturingRequestList, String query) {
        Page<CustomerGetOrderHistoryResponse> customerGetOrderHistoryResponsePage = orderRepository.findByCustomer(userName, pageable, stageRequestList, manufacturingRequestList, query);

        return new PageResponse<>(customerGetOrderHistoryResponsePage);
    }

    @Transactional
    public PageResponse<CustomerGetOrderIsCompletedHistoryResponse> getOrderIsCompletedHistory(String userName, Pageable pageable, String query) {
        Page<CustomerGetOrderIsCompletedHistoryResponse> customerGetOrderIsCompletedHistoryResponsePage = orderRepository.findIsCompletedByCustomer(userName, pageable, query);

        return new PageResponse<>(customerGetOrderIsCompletedHistoryResponsePage);
    }

    public CustomerGetOrderCreateInformationResponse getOrderCreateInformation(String userName, Long orderId) {
        CustomerGetOrderCreateInformationResponse customerGetOrderCreateInformationResponse = orderRepository.findCreateInformationByCustomerAndOrder(userName, orderId);

        List<CustomerGetDrawingResponse> customerGetDrawingResponseList = drawingService.getDrawingByCustomerAndOrder(userName, orderId);
        customerGetOrderCreateInformationResponse.setDrawingList(customerGetDrawingResponseList);

        return customerGetOrderCreateInformationResponse;
    }
}
