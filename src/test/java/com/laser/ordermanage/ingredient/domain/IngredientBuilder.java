package com.laser.ordermanage.ingredient.domain;

import com.laser.ordermanage.factory.domain.Factory;
import com.laser.ordermanage.factory.domain.FactoryBuilder;

public class IngredientBuilder {
    public static Ingredient build() {
        Factory factory = FactoryBuilder.build();

        return Ingredient.builder()
                .factory(factory)
                .texture("SS 400")
                .thickness(1.6)
                .width(4)
                .height(8)
                .weight(37.3)
                .build();
    }
}
