package com.laser.ordermanage.customer.dto.request;

import com.laser.ordermanage.order.domain.type.Ingredient;

public class CustomerCreateDrawingRequestBuilder {
    public static CustomerCreateDrawingRequest build() {
        return new CustomerCreateDrawingRequest("https://ordermanage.s3.ap-northeast-2.amazonaws.com/output.png", "test.dwg", 140801L, "dwg", "https://ordermanage.s3.ap-northeast-2.amazonaws.com/test.dwg", 3, Ingredient.SS400.getValue(), 10);
    }
}
