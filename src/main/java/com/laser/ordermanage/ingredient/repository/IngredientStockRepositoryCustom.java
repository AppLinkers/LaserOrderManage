package com.laser.ordermanage.ingredient.repository;

import com.laser.ordermanage.ingredient.domain.IngredientStock;

import java.time.LocalDate;
import java.util.List;

public interface IngredientStockRepositoryCustom {
    IngredientStock findPreviousByIngredientIdAndDate(Long ingredientId, LocalDate date);

    void saveAll(List<IngredientStock> ingredientStockList);
}
