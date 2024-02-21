package com.laser.ordermanage.ingredient.dto.response;

public record GetIngredientResponse (
        Long id,
        String texture,
        Double thickness,
        Integer width,
        Integer height,
        GetIngredientStockDetailResponse stock,
        GetIngredientPriceResponse price
) { }
