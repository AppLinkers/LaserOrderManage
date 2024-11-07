package com.laser.ordermanage.ingredient.domain;

public class IngredientPriceBuilder {
    public static IngredientPrice build() {
        Ingredient ingredient = IngredientBuilder.build();
        return IngredientPrice.builder()
                .ingredient(ingredient)
                .purchase(1000)
                .sell(1000)
                .build();
    }
}
