package com.laser.ordermanage.common.scheduler.dto.request;

import lombok.Builder;

import java.util.Date;

@Builder
public record JobRequest (
        String name,
        String group,
        Date startAt
) {
}
