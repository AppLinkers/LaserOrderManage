package com.laser.ordermanage.ingredient.dto.request;

public class CreateIngredientRequestBuilder {
    public static CreateIngredientRequest build() {
        IngredientPriceRequest price = new IngredientPriceRequest(1000, 1000);
        return new CreateIngredientRequest("SS 400", 1.0, 100, 100, 10.0, price, 10);
    }

    public static CreateIngredientRequest nullTextureBuild() {
        IngredientPriceRequest price = new IngredientPriceRequest(1000, 1000);
        return new CreateIngredientRequest(null, 1.0, 100, 100, 10.0, price, 10);
    }

    public static CreateIngredientRequest emptyTextureBuild() {
        IngredientPriceRequest price = new IngredientPriceRequest(1000, 1000);
        return new CreateIngredientRequest("", 1.0, 100, 100, 10.0, price, 10);
    }

    public static CreateIngredientRequest invalidTextureBuild() {
        IngredientPriceRequest price = new IngredientPriceRequest(1000, 1000);
        return new CreateIngredientRequest("SS 400".repeat(4), 1.0, 100, 100, 10.0, price, 10);
    }

    public static CreateIngredientRequest nullThicknessBuild() {
        IngredientPriceRequest price = new IngredientPriceRequest(1000, 1000);
        return new CreateIngredientRequest("SS 400", null, 100, 100, 10.0, price, 10);
    }

    public static CreateIngredientRequest invalidThicknessBuild() {
        IngredientPriceRequest price = new IngredientPriceRequest(1000, 1000);
        return new CreateIngredientRequest("SS 400", 100.1, 100, 100, 10.0, price, 10);
    }

    public static CreateIngredientRequest nullWidthBuild() {
        IngredientPriceRequest price = new IngredientPriceRequest(1000, 1000);
        return new CreateIngredientRequest("SS 400", 1.0, null, 100, 10.0, price, 10);
    }

    public static CreateIngredientRequest invalidWidthBuild() {
        IngredientPriceRequest price = new IngredientPriceRequest(1000, 1000);
        return new CreateIngredientRequest("SS 400", 1.0, 101, 100, 10.0, price, 10);
    }

    public static CreateIngredientRequest nullHeightBuild() {
        IngredientPriceRequest price = new IngredientPriceRequest(1000, 1000);
        return new CreateIngredientRequest("SS 400", 1.0, 100, null, 10.0, price, 10);
    }

    public static CreateIngredientRequest invalidHeightBuild() {
        IngredientPriceRequest price = new IngredientPriceRequest(1000, 1000);
        return new CreateIngredientRequest("SS 400", 1.0, 100, 101, 10.0, price, 10);
    }

    public static CreateIngredientRequest nullWeightBuild() {
        IngredientPriceRequest price = new IngredientPriceRequest(1000, 1000);
        return new CreateIngredientRequest("SS 400", 1.0, 100, 100, null, price, 10);
    }

    public static CreateIngredientRequest invalidWeightBuild() {
        IngredientPriceRequest price = new IngredientPriceRequest(1000, 1000);
        return new CreateIngredientRequest("SS 400", 1.0, 100, 100, 1000.1, price, 10);
    }

    public static CreateIngredientRequest nullPurchasePriceBuild() {
        IngredientPriceRequest price = new IngredientPriceRequest(null, 1000);
        return new CreateIngredientRequest("SS 400", 1.0, 100, 100, 10.0, price, 10);
    }

    public static CreateIngredientRequest invalidPurchasePriceBuild() {
        IngredientPriceRequest price = new IngredientPriceRequest(100001, 1000);
        return new CreateIngredientRequest("SS 400", 1.0, 100, 100, 10.0, price, 10);
    }

    public static CreateIngredientRequest nullSellPriceBuild() {
        IngredientPriceRequest price = new IngredientPriceRequest(1000, null);
        return new CreateIngredientRequest("SS 400", 1.0, 100, 100, 10.0, price, 10);
    }

    public static CreateIngredientRequest invalidSellPriceBuild() {
        IngredientPriceRequest price = new IngredientPriceRequest(1000, 100001);
        return new CreateIngredientRequest("SS 400", 1.0, 100, 100, 10.0, price, 10);
    }
}
