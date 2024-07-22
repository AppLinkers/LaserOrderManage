package com.laser.ordermanage.customer.dto.request;

import com.laser.ordermanage.order.domain.type.Ingredient;

public class CustomerCreateDrawingRequestBuilder {
    public static CustomerCreateDrawingRequest build() {
        return new CustomerCreateDrawingRequest("https://ordermanage.s3.ap-northeast-2.amazonaws.com/output.png", "test.dwg", 140801L, "dwg", "https://ordermanage.s3.ap-northeast-2.amazonaws.com/test.dwg", 3, Ingredient.SS400.getValue(), 10);
    }

    public static CustomerCreateDrawingRequest nullCountBuild() {
        return new CustomerCreateDrawingRequest("https://ordermanage.s3.ap-northeast-2.amazonaws.com/output.png", "test.dwg", 140801L, "dwg", "https://ordermanage.s3.ap-northeast-2.amazonaws.com/test.dwg", null, Ingredient.SS400.getValue(), 10);
    }

    public static CustomerCreateDrawingRequest invalidCountBuild() {
        return new CustomerCreateDrawingRequest("https://ordermanage.s3.ap-northeast-2.amazonaws.com/output.png", "test.dwg", 140801L, "dwg", "https://ordermanage.s3.ap-northeast-2.amazonaws.com/test.dwg", 101, Ingredient.SS400.getValue(), 10);
    }

    public static CustomerCreateDrawingRequest nullIngredientBuild() {
        return new CustomerCreateDrawingRequest("https://ordermanage.s3.ap-northeast-2.amazonaws.com/output.png", "test.dwg", 140801L, "dwg", "https://ordermanage.s3.ap-northeast-2.amazonaws.com/test.dwg", 3, null, 10);
    }

    public static CustomerCreateDrawingRequest emptyIngredientBuild() {
        return new CustomerCreateDrawingRequest("https://ordermanage.s3.ap-northeast-2.amazonaws.com/output.png", "test.dwg", 140801L, "dwg", "https://ordermanage.s3.ap-northeast-2.amazonaws.com/test.dwg", 3, "", 10);
    }

    public static CustomerCreateDrawingRequest nullThicknessBuild() {
        return new CustomerCreateDrawingRequest("https://ordermanage.s3.ap-northeast-2.amazonaws.com/output.png", "test.dwg", 140801L, "dwg", "https://ordermanage.s3.ap-northeast-2.amazonaws.com/test.dwg", 3, Ingredient.SS400.getValue(), null);
    }

    public static CustomerCreateDrawingRequest invalidThicknessBuild() {
        return new CustomerCreateDrawingRequest("https://ordermanage.s3.ap-northeast-2.amazonaws.com/output.png", "test.dwg", 140801L, "dwg", "https://ordermanage.s3.ap-northeast-2.amazonaws.com/test.dwg", 3, Ingredient.SS400.getValue(), 20);
    }
}
