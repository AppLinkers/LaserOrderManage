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

    public static UpdateIngredientRequest nullPurchasePriceBuild() {
        IngredientPriceRequest price = new IngredientPriceRequest(null, 1000);
        return new UpdateIngredientRequest(price, 100);
    }

    public static UpdateIngredientRequest invalidPurchasePriceBuild() {
        IngredientPriceRequest price = new IngredientPriceRequest(100001, 1000);
        return new UpdateIngredientRequest(price, 100);
    }

    public static UpdateIngredientRequest nullSellPriceBuild() {
        IngredientPriceRequest price = new IngredientPriceRequest(1000, null);
        return new UpdateIngredientRequest(price, 100);
    }

    public static UpdateIngredientRequest invalidSellPriceBuild() {
        IngredientPriceRequest price = new IngredientPriceRequest(1000, 100001);
        return new UpdateIngredientRequest(price, 100);
    }
}
