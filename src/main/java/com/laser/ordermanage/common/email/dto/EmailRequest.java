package com.laser.ordermanage.common.email.dto;

import lombok.Builder;

@Builder
public record EmailRequest(
        String recipient,
        String subject,
        String text
) { }
