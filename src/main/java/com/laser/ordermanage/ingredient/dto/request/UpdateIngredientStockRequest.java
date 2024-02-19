package com.laser.ordermanage.ingredient.dto.request;

import jakarta.validation.constraints.NotNull;

public record UpdateIngredientStockRequest(

        @NotNull(message = "입고는 필수 입력값입니다.")
        Integer incoming,

        @NotNull(message = "생산은 필수 입력값입니다.")
        Integer production,

        @NotNull(message = "당일 재고는 필수 입력값입니다.")
        Integer currentDay
) {
}
