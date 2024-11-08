package com.laser.ordermanage.ingredient.unit.domain;

import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.ingredient.domain.IngredientStock;
import com.laser.ordermanage.ingredient.domain.IngredientStockBuilder;
import com.laser.ordermanage.ingredient.dto.request.UpdateIngredientStockRequest;
import com.laser.ordermanage.ingredient.dto.request.UpdateIngredientStockRequestBuilder;
import com.laser.ordermanage.ingredient.exception.IngredientErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class IngredientStockUnitTest {

    @Test
    public void validate_성공() {
        // given
        final IngredientStock ingredientStock = IngredientStockBuilder.build();
        final UpdateIngredientStockRequest request = UpdateIngredientStockRequestBuilder.build();

        // when
        IngredientStock.validate(ingredientStock, request);

        // then
    }

    @Test
    public void validate_실패_INVALID_INGREDIENT_STOCK() {
        // given
        final IngredientStock ingredientStock = IngredientStockBuilder.build();
        final UpdateIngredientStockRequest request = UpdateIngredientStockRequestBuilder.invalidBuild();

        // when & then
        Assertions.assertThatThrownBy(() -> IngredientStock.validate(ingredientStock, request))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(IngredientErrorCode.INVALID_INGREDIENT_STOCK.getMessage());
    }

    @Test
    public void updateStock() {
        // given
        final IngredientStock ingredientStock = IngredientStockBuilder.build();
        final UpdateIngredientStockRequest request = UpdateIngredientStockRequestBuilder.build();

        // when
        ingredientStock.updateStock(request);

        // then
        Assertions.assertThat(ingredientStock.getIncoming()).isEqualTo(request.incoming());
        Assertions.assertThat(ingredientStock.getProduction()).isEqualTo(request.production());
        Assertions.assertThat(ingredientStock.getStock()).isEqualTo(request.currentDay());
    }

    @Test
    public void updateOptimalStock() {
        // given
        final IngredientStock ingredientStock = IngredientStockBuilder.build();
        final Integer optimalRequest = 100;

        // when
        ingredientStock.updateOptimalStock(optimalRequest);

        // then
        Assertions.assertThat(ingredientStock.getOptimal()).isEqualTo(optimalRequest);
    }
}
