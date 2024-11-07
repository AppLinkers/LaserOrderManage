package com.laser.ordermanage.ingredient.domain;

public class IngredientStockBuilder {
    public static IngredientStock build() {
        Ingredient ingredient = IngredientBuilder.build();
        return IngredientStock.builder()
                .ingredient(ingredient)
                .incoming(0)
                .production(0)
                .stock(70)
                .optimal(60)
                .build();
    }
}
