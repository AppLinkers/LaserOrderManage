package com.laser.ordermanage.ingredient.repository;

import com.laser.ordermanage.ingredient.domain.IngredientPrice;

import java.util.List;

public interface IngredientPriceRepositoryCustom {
    void saveAll(List<IngredientPrice> ingredientPriceList);
}
