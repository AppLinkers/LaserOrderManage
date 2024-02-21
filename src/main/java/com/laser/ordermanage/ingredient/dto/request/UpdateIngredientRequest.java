package com.laser.ordermanage.ingredient.dto.request;

public record UpdateIngredientRequest (
        UpdateIngredientStockRequest stock,

        IngredientPriceRequest price,

        Integer optimalStock
) {
}
