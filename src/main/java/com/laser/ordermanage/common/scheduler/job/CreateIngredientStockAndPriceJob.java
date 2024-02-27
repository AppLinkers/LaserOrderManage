package com.laser.ordermanage.common.scheduler.job;

import com.laser.ordermanage.ingredient.service.IngredientService;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.time.ZoneId;

@RequiredArgsConstructor
@Component
public class CreateIngredientStockAndPriceJob implements Job {

    private final IngredientService ingredientService;

    @Override
    public void execute(JobExecutionContext context) {
        ingredientService.createIngredientStockAndPriceMonthly(YearMonth.from(context.getFireTime().toInstant().atZone(ZoneId.systemDefault()).plusDays(1).toLocalDate()));
    }
}
