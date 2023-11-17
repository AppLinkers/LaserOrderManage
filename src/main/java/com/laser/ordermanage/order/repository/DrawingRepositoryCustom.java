package com.laser.ordermanage.order.repository;

import com.laser.ordermanage.customer.dto.response.CustomerGetDrawingResponse;

import java.util.List;

public interface DrawingRepositoryCustom {

    List<CustomerGetDrawingResponse> findByCustomerAndOrder(String userName, Long orderId);
}
