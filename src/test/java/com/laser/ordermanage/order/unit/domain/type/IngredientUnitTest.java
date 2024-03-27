package com.laser.ordermanage.order.unit.domain.type;

import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.order.domain.type.Ingredient;
import com.laser.ordermanage.order.exception.OrderErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class IngredientUnitTest {

    @Test
    public void ofValue() {
        // given
        final Ingredient expectedIngredient = Ingredient.DOUBLE_HL;

        // when
        final Ingredient actualIngredient = Ingredient.ofValue(expectedIngredient.getValue());

        // then
        Assertions.assertThat(actualIngredient).isEqualTo(expectedIngredient);
    }

    @Test
    public void ofValue_실패_UNSUPPORTED_INGREDIENT() {
        // given
        final String invalidIngredient = "invalid-ingredient";

        // when & then
        Assertions.assertThatThrownBy(() -> Ingredient.ofValue(invalidIngredient))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(OrderErrorCode.UNSUPPORTED_INGREDIENT.getMessage());
    }
}
