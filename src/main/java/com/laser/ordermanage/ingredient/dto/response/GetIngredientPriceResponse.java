package com.laser.ordermanage.ingredient.dto.response;

import com.querydsl.core.annotations.QueryProjection;

public record GetIngredientPriceResponse (
        Integer purchase,
        Integer sell
) {
    @QueryProjection
    public GetIngredientPriceResponse(Integer purchase, Integer sell) {
        this.purchase = purchase;
        this.sell = sell;
    }
}
