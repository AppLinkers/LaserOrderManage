package com.laser.ordermanage.ingredient.dto.request;

public class UpdateIngredientRequestBuilder {
    public static UpdateIngredientRequest build() {
        IngredientPriceRequest price = new IngredientPriceRequest(1000, 1000);
        return new UpdateIngredientRequest(price, 100);
    }

    public static UpdateIngredientRequest build2() {
        IngredientPriceRequest price = new IngredientPriceRequest(2000, 2000);
        return new UpdateIngredientRequest(price, 200);
    }
}
