package com.laser.ordermanage.ingredient.dto.response;

import java.time.LocalDate;
import java.util.List;

public class GetIngredientStatusResponseBuilder {
    public static GetIngredientStatusResponse build() {
        GetIngredientPriceResponse averagePrice = new GetIngredientPriceResponse(3000, 3200);
        GetIngredientTotalStockResponse totalStock = new GetIngredientTotalStockResponse(402, 29000.5);
        List<GetIngredientResponse> ingredientList = GetIngredientResponseBuilder.buildList();
        LocalDate date = LocalDate.of(2024, 4, 1);

        return GetIngredientStatusResponse.builder()
                .averagePrice(averagePrice)
                .totalStock(totalStock)
                .ingredientList(ingredientList)
                .date(date)
                .build();
    }
}
