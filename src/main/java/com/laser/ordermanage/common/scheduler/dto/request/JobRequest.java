package com.laser.ordermanage.common.scheduler.dto.request;

import com.laser.ordermanage.common.scheduler.job.type.JobType;
import lombok.Builder;

import java.util.Date;

@Builder
public record JobRequest (
        String name,
        String group,
        JobType jobType,
        Date startAt
) {
}
