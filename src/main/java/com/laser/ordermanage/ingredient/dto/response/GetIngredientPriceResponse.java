package com.laser.ordermanage.ingredient.dto.response;

import lombok.Builder;

@Builder
public record GetIngredientPriceResponse (
        Integer purchase,
        Integer sell
) { }
