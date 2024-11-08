package com.laser.ordermanage.ingredient.domain;

import org.assertj.core.api.Assertions;

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

    public static IngredientStock buildOfPrevious() {
        Ingredient ingredient = IngredientBuilder.build();
        return IngredientStock.builder()
                .ingredient(ingredient)
                .incoming(0)
                .production(40)
                .stock(50)
                .optimal(40)
                .build();
    }

    public static void assertIngredientStock(IngredientStock actualIngredientStock, IngredientStock expectedIngredientStock) {
        IngredientBuilder.assertIngredient(actualIngredientStock.getIngredient(), expectedIngredientStock.getIngredient());
        Assertions.assertThat(actualIngredientStock.getIncoming()).isEqualTo(expectedIngredientStock.getIncoming());
        Assertions.assertThat(actualIngredientStock.getProduction()).isEqualTo(expectedIngredientStock.getProduction());
        Assertions.assertThat(actualIngredientStock.getStock()).isEqualTo(expectedIngredientStock.getStock());
        Assertions.assertThat(actualIngredientStock.getOptimal()).isEqualTo(expectedIngredientStock.getOptimal());
    }
}
