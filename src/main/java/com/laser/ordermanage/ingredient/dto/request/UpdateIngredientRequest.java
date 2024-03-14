package com.laser.ordermanage.ingredient.dto.request;

import jakarta.validation.Valid;

public record UpdateIngredientRequest (

        @Valid
        IngredientPriceRequest price,

        Integer optimalStock
) {
}
