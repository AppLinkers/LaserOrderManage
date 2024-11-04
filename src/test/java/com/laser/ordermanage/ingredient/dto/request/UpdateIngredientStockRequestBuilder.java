package com.laser.ordermanage.ingredient.dto.request;

public class UpdateIngredientStockRequestBuilder {
    public static UpdateIngredientStockRequest build() {
        return new UpdateIngredientStockRequest(5, 65, 10);
    }

    public static UpdateIngredientStockRequest build2() {
        return new UpdateIngredientStockRequest(25, 65, 30);
    }

    public static UpdateIngredientStockRequest invalidBuild() {
        return new UpdateIngredientStockRequest(5, 65, 0);
    }
}
