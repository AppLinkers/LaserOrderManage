package com.laser.ordermanage.common.scheduler.util;

import com.laser.ordermanage.common.scheduler.dto.request.JobRequest;
import org.quartz.*;

public class JobUtil {

    private JobUtil() {
    }

    public static Trigger createTrigger(JobRequest jobRequest) {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobRequest.name(), jobRequest.group());

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerKey)
                .startAt(jobRequest.startAt())
                .build();

        return trigger;
    }

    public static JobDetail createJob(JobRequest jobRequest, Class<? extends Job> jobClass) {
        JobKey jobKey = JobKey.jobKey(jobRequest.name(), jobRequest.group());

        JobDetail job = JobBuilder.newJob(jobClass)
                .withIdentity(jobKey)
                .build();

        return job;
    }

}
