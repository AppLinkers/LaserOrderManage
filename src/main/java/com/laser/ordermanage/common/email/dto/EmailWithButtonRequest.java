package com.laser.ordermanage.common.email.dto;

import lombok.Builder;

@Builder
public record EmailWithButtonRequest(
        String recipient,
        String subject,
        String title,
        String content,
        String buttonText,
        String buttonUrl
) {
}
