package com.laser.ordermanage.factory.service;

import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.exception.ErrorCode;
import com.laser.ordermanage.factory.dto.request.FactoryUpdateOrderIsUrgentRequest;
import com.laser.ordermanage.order.domain.Order;
import com.laser.ordermanage.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class FactoryOrderService {

    private final OrderService orderService;

    @Transactional
    public void updateOrderIsUrgent(Long orderId, FactoryUpdateOrderIsUrgentRequest request) {
        Order order = orderService.getOrderById(orderId);

        if (!order.enableUpdateIsUrgent()) {
            throw new CustomCommonException(ErrorCode.INVALID_ORDER_STAGE, order.getStage().getValue());
        }

        order.updateIsUrgent(request.getIsUrgent());
    }

}
