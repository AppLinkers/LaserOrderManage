package com.laser.ordermanage.order.service;

import com.laser.ordermanage.order.dto.response.GetOrderDetailResponse;
import com.laser.ordermanage.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public GetOrderDetailResponse getOrderDetail(User user, Long orderId) {
        return orderRepository.findDetailByUserAndOrder(user, orderId);
    }
}
