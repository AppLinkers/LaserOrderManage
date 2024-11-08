package com.laser.ordermanage.ingredient.domain;

import com.laser.ordermanage.factory.domain.Factory;
import com.laser.ordermanage.factory.domain.FactoryBuilder;
import org.assertj.core.api.Assertions;

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

    public static void assertIngredient(Ingredient actualIngredient, Ingredient expectedIngredient) {
        FactoryBuilder.assertFactory(actualIngredient.getFactory(), expectedIngredient.getFactory());
        Assertions.assertThat(actualIngredient.getTexture()).isEqualTo(expectedIngredient.getTexture());
        Assertions.assertThat(actualIngredient.getThickness()).isEqualTo(expectedIngredient.getThickness());
        Assertions.assertThat(actualIngredient.getWidth()).isEqualTo(expectedIngredient.getWidth());
        Assertions.assertThat(actualIngredient.getHeight()).isEqualTo(expectedIngredient.getHeight());
        Assertions.assertThat(actualIngredient.getWeight()).isEqualTo(expectedIngredient.getWeight());
    }
}
