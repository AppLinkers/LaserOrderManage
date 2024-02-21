package com.laser.ordermanage.ingredient.dto.response;

public record GetIngredientTotalStockResponse(
        Integer count,
        Double weight
) { }
