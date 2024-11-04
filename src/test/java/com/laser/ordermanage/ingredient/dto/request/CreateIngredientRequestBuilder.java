package com.laser.ordermanage.ingredient.dto.request;

public class CreateIngredientRequestBuilder {
    public static CreateIngredientRequest build() {
        IngredientPriceRequest price = new IngredientPriceRequest(1000, 1000);
        return new CreateIngredientRequest("SS 400", 1.0, 100, 100, 10.0, price, 10);
    }

}
