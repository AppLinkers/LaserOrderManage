package com.laser.ordermanage.ingredient.unit.domain;

import com.laser.ordermanage.ingredient.domain.Ingredient;
import com.laser.ordermanage.ingredient.domain.IngredientBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class IngredientUnitTest {

    @Test
    public void delete() {
        // given
        final Ingredient ingredient = IngredientBuilder.build();

        // when
        ingredient.delete();

        // then
        Assertions.assertThat(ingredient.getDeletedAt()).isNotNull();
    }

    @Test
    public void isDeleted_True() {
        // given
        final Ingredient ingredient = IngredientBuilder.build();
        ingredient.delete();

        // when & then
        Assertions.assertThat(ingredient.isDeleted()).isTrue();
    }

    @Test
    public void isDeleted_False() {
        // given
        final Ingredient ingredient = IngredientBuilder.build();

        // when & then
        Assertions.assertThat(ingredient.isDeleted()).isFalse();
    }
}
