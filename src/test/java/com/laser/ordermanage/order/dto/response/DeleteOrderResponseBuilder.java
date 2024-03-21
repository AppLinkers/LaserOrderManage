package com.laser.ordermanage.order.dto.response;

import com.laser.ordermanage.order.domain.Order;
import com.laser.ordermanage.order.domain.OrderBuilder;

public class DeleteOrderResponseBuilder {
    public static DeleteOrderResponse build() {
        Order order = OrderBuilder.build();

        return DeleteOrderResponse.builder()
                .name(order.getName())
                .customerUserEmail(order.getCustomer().getUser().getEmail())
                .build();
    }
}
