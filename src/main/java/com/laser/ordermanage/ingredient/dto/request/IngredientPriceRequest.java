package com.laser.ordermanage.ingredient.dto.request;

import jakarta.validation.constraints.NotNull;

public record IngredientPriceRequest(

        @NotNull(message = "구매 단가는 필수 입력값입니다.")
        Integer purchase,

        @NotNull(message = "판매 단가는 필수 입력값입니다.")
        Integer sell
) {
}
