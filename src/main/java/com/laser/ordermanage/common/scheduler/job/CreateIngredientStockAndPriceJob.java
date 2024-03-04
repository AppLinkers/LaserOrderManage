package com.laser.ordermanage.common.scheduler.job;

import com.laser.ordermanage.ingredient.service.IngredientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Slf4j
@RequiredArgsConstructor
@Component
public class CreateIngredientStockAndPriceJob implements Job {

    private final IngredientService ingredientService;

    @Override
    public void execute(JobExecutionContext context) {
        log.info(context.getFireTime().toInstant().atZone(ZoneId.systemDefault()).toString());
//        ingredientService.createIngredientStockAndPriceMonthly(YearMonth.from(context.getFireTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
    }
}
