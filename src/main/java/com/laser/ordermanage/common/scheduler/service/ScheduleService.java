package com.laser.ordermanage.common.scheduler.service;

import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.exception.ErrorCode;
import com.laser.ordermanage.common.scheduler.dto.request.JobRequest;
import com.laser.ordermanage.common.scheduler.util.JobUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ScheduleService {

    private final SchedulerFactoryBean schedulerFactoryBean;

    public boolean addJob(JobRequest jobRequest, Class<? extends Job> jobClass) {
        JobDetail jobDetail;
        Trigger trigger;

        try {
            trigger = JobUtil.createTrigger(jobRequest);
            jobDetail = JobUtil.createJob(jobRequest, jobClass);

            schedulerFactoryBean.getScheduler().scheduleJob(jobDetail, trigger);
            return true;
        } catch (SchedulerException e) {
            throw new CustomCommonException(ErrorCode.UNKNOWN_ERROR);
        }
    }

}
