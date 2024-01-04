package com.laser.ordermanage.common.scheduler.job;

import com.laser.ordermanage.factory.service.FactoryOrderService;
import com.laser.ordermanage.order.domain.Order;
import com.laser.ordermanage.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ChangeStageToCompletedJob implements Job {

    private final OrderService orderService;
    private final FactoryOrderService factoryOrderService;

    @Override
    public void execute(JobExecutionContext context) {
        Long orderId = Long.valueOf(context.getJobDetail().getKey().getName());

        Order order = orderService.getOrderById(orderId);

        if (!order.enableChangeStageToCompleted()) {
            return;
        }

        factoryOrderService.changeStageToCompleted(order);

        factoryOrderService.sendEmailForChangeStageToCompleted(order);
    }
}
