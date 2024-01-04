package com.laser.ordermanage.factory.scheduler.job;

import com.laser.ordermanage.customer.service.CustomerOrderService;
import com.laser.ordermanage.order.domain.Order;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ChangeStageToCompletedJob implements Job {

    private final CustomerOrderService customerOrderService;

    @Override
    public void execute(JobExecutionContext context) {
        Long orderId = Long.valueOf(context.getJobDetail().getKey().getName());

        Order order = customerOrderService.changeStageToCompleted(orderId);

        customerOrderService.sendEmailForChangeStageToCompleted(order);
    }
}
