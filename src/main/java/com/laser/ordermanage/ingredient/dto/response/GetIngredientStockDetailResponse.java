package com.laser.ordermanage.ingredient.dto.response;

public record GetIngredientStockDetailResponse (
        Number previousDay,
        Number incoming,
        Number production,
        Number currentDay,
        Number optimal
) { }
