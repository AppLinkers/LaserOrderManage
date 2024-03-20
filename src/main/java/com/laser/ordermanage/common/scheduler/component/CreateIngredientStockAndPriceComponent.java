package com.laser.ordermanage.common.scheduler.component;

import com.laser.ordermanage.common.scheduler.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CreateIngredientStockAndPriceComponent {

    private final ScheduleService scheduleService;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReadyEvent() {
        scheduleService.removeJobForCreateIngredientStockAndPrice();
        scheduleService.createJobForCreateIngredientStockAndPrice();
    }
}
