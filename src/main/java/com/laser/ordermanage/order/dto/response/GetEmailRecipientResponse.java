package com.laser.ordermanage.order.dto.response;

import lombok.Builder;

@Builder
public record GetEmailRecipientResponse (
        Boolean emailNotification,
        String email
) {
}
