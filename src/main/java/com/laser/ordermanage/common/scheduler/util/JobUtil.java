package com.laser.ordermanage.common.scheduler.util;

import com.laser.ordermanage.common.scheduler.dto.request.JobRequest;
import com.laser.ordermanage.common.scheduler.job.type.JobType;
import org.quartz.*;

public class JobUtil {

    private JobUtil() {
    }

    public static Trigger createTrigger(JobRequest jobRequest) {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobRequest.name(), jobRequest.group());

        if (jobRequest.jobType().equals(JobType.ONE_TIME)) {
            return TriggerBuilder.newTrigger()
                    .withIdentity(triggerKey)
                    .startAt(jobRequest.startAt())
                    .build();
        } else {
            return TriggerBuilder.newTrigger()
                    .withIdentity(triggerKey)
                    .withSchedule(CronScheduleBuilder.cronSchedule("0 0 0 1 * ?"))
                    .startNow()
                    .build();
        }
    }

    public static JobDetail createJob(JobRequest jobRequest, Class<? extends Job> jobClass) {
        JobKey jobKey = JobKey.jobKey(jobRequest.name(), jobRequest.group());

        JobDetail job = JobBuilder.newJob(jobClass)
                .withIdentity(jobKey)
                .build();

        return job;
    }

}
