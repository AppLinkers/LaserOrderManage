package com.laser.ordermanage.ingredient.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record IngredientPriceRequest(

        @NotNull(message = "구매 단가는 필수 입력값입니다.")
        @Min(value = 1, message = "구매단가 1 이상, 100,000 이하의 정수 입니다.")
        @Max(value = 100000, message = "구매단가 1 이상, 100,000 이하의 정수 입니다.")
        Integer purchase,

        @NotNull(message = "판매 단가는 필수 입력값입니다.")
        @Min(value = 1, message = "판매단가는 1 이상, 100,000 이하의 정수 입니다.")
        @Max(value = 100000, message = "판매단가는 1 이상, 100,000 이하의 정수 입니다.")
        Integer sell
) {
}
