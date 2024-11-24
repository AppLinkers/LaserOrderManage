package com.laser.ordermanage.ingredient.dto.request;

import com.laser.ordermanage.ingredient.domain.Ingredient;
import com.laser.ordermanage.ingredient.domain.IngredientStock;
import jakarta.validation.Valid;

public record UpdateIngredientRequest (

        @Valid
        IngredientPriceRequest price,

        Integer optimalStock
) {
        public IngredientStock toIngredientStockEntity(Ingredient ingredient, IngredientStock previousIngredientStock) {
                return  IngredientStock.builder()
                        .ingredient(ingredient)
                        .incoming(0)
                        .production(0)
                        .stock(previousIngredientStock.getStock())
                        .optimal(optimalStock)
                        .build();
        }
}
