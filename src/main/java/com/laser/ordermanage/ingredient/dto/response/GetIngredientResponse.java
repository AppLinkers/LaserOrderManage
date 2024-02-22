package com.laser.ordermanage.ingredient.dto.response;

public record GetIngredientResponse (
        Long id,
        String texture,
        Double thickness,
        Integer width,
        Integer height,
        GetIngredientStockDetailResponse stockCount,
        GetIngredientStockDetailResponse stockWeight,
        GetIngredientPriceResponse price
) { }
