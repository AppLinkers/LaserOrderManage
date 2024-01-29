package com.laser.ordermanage.order.dto.response;


import lombok.Builder;

@Builder
public record DeleteOrderResponse (
        String name,
        String customerUserEmail
) {
}
