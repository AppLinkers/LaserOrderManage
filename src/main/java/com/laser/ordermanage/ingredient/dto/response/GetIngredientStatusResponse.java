package com.laser.ordermanage.ingredient.dto.response;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record GetIngredientStatusResponse (
    GetIngredientPriceResponse averagePrice,
    GetIngredientTotalStockResponse totalStock,
    List<GetIngredientResponse> ingredientList,
    LocalDate date
) { }
