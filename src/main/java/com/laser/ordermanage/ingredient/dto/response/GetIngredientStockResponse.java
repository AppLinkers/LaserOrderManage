package com.laser.ordermanage.ingredient.dto.response;

import java.time.LocalDate;
import java.util.List;

public record GetIngredientStockResponse (
    GetIngredientPriceResponse averagePrice,
    GetIngredientTotalStockResponse totalStock,
    List<GetIngredientResponse> ingredientList,
    LocalDate date
) { }
