package com.laser.ordermanage.order.dto.response;


import com.laser.ordermanage.order.domain.Order;
import lombok.Builder;

@Builder
public record DeleteOrderResponse (
        String name,
        String customerUserEmail
) {
    public static DeleteOrderResponse fromEntity(Order order) {
        return DeleteOrderResponse.builder()
                .name(order.getName())
                .customerUserEmail(order.getCustomer().getUser().getEmail())
                .build();
    }
}
