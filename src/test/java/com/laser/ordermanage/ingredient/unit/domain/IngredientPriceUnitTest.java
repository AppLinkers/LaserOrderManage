package com.laser.ordermanage.ingredient.unit.domain;

import com.laser.ordermanage.ingredient.domain.IngredientPrice;
import com.laser.ordermanage.ingredient.domain.IngredientPriceBuilder;
import com.laser.ordermanage.ingredient.dto.request.IngredientPriceRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class IngredientPriceUnitTest {

    @Test
    public void updatePrice() {
        // given
        final IngredientPrice ingredientPrice = IngredientPriceBuilder.build();
        final IngredientPriceRequest request = new IngredientPriceRequest(2000, 2000);

        // when
        ingredientPrice.updatePrice(request);

        // then
        Assertions.assertThat(ingredientPrice.getPurchase()).isEqualTo(request.purchase());
        Assertions.assertThat(ingredientPrice.getSell()).isEqualTo(request.sell());
    }
}
