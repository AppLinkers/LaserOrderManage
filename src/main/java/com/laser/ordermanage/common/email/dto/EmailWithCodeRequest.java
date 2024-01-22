package com.laser.ordermanage.common.email.dto;

import lombok.Builder;

@Builder
public record EmailWithCodeRequest(
        String recipient,
        String subject,
        String title,
        String content,
        String code
) {
}
