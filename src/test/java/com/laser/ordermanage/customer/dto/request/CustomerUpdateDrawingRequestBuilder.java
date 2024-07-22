package com.laser.ordermanage.customer.dto.request;

import com.laser.ordermanage.order.domain.type.Ingredient;

public class CustomerUpdateDrawingRequestBuilder {
    public static CustomerUpdateDrawingRequest build() {
        return new CustomerUpdateDrawingRequest(10, Ingredient.AC.getValue(), 15);
    }

    public static CustomerUpdateDrawingRequest nullCountBuild() {
        return new CustomerUpdateDrawingRequest(null, Ingredient.AC.getValue(), 15);
    }

    public static CustomerUpdateDrawingRequest invalidCountBuild() {
        return new CustomerUpdateDrawingRequest(101, Ingredient.AC.getValue(), 15);
    }

    public static CustomerUpdateDrawingRequest nullIngredientBuild() {
        return new CustomerUpdateDrawingRequest(10, null, 15);
    }

    public static CustomerUpdateDrawingRequest emptyIngredientBuild() {
        return new CustomerUpdateDrawingRequest(10, "", 15);
    }

    public static CustomerUpdateDrawingRequest nullThicknessBuild() {
        return new CustomerUpdateDrawingRequest(10, Ingredient.AC.getValue(), null);
    }

    public static CustomerUpdateDrawingRequest invalidThicknessBuild() {
        return new CustomerUpdateDrawingRequest(10, Ingredient.AC.getValue(), 20);
    }
}
