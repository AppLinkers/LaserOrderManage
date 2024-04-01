package com.laser.ordermanage.customer.dto.request;

import com.laser.ordermanage.order.domain.type.Ingredient;

public class CustomerUpdateDrawingRequestBuilder {
    public static CustomerUpdateDrawingRequest build() {
        return new CustomerUpdateDrawingRequest(10, Ingredient.AC.getValue(), 15);
    }
}
