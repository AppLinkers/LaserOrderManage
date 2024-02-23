package com.laser.ordermanage.ingredient.dto.response;

import lombok.Builder;

@Builder
public record GetIngredientTotalStockResponse(
        Integer count,
        Double weight
) { }
