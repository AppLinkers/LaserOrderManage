package com.laser.ordermanage.ingredient.domain;

import org.assertj.core.api.Assertions;

public class IngredientPriceBuilder {
    public static IngredientPrice build() {
        Ingredient ingredient = IngredientBuilder.build();
        return IngredientPrice.builder()
                .ingredient(ingredient)
                .purchase(900)
                .sell(1000)
                .build();
    }

    public static void assertIngredientPrice(IngredientPrice actualIngredientPrice, IngredientPrice expectedIngredientPrice) {
        IngredientBuilder.assertIngredient(actualIngredientPrice.getIngredient(), expectedIngredientPrice.getIngredient());
        Assertions.assertThat(actualIngredientPrice.getPurchase()).isEqualTo(expectedIngredientPrice.getPurchase());
        Assertions.assertThat(actualIngredientPrice.getSell()).isEqualTo(expectedIngredientPrice.getSell());
    }
}
