package com.laser.ordermanage.order.dto.response;

import lombok.Builder;

@Builder
public record GetEmailReceiverResponse (
        Boolean emailNotification,
        String email
) {
}
