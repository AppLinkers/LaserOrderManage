package com.laser.ordermanage.ingredient.repository;

import com.laser.ordermanage.ingredient.dto.response.GetIngredientStockResponse;

import java.time.LocalDate;
import java.util.Optional;

public interface IngredientRepositoryCustom {
    GetIngredientStockResponse findIngredientStockByFactoryAndDate(String email, LocalDate date, String unit);

    Optional<String> findUserEmailById(Long ingredientId);
}
