package com.laser.ordermanage.ingredient.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateIngredientRequest(

        @NotEmpty(message = "재질은 필수 입력값입니다.")
        @Pattern(regexp = "^.{0,20}$", message = "재질의 최대 글자수는 20자입니다.")
        String texture,

        @NotNull(message = "두께는 필수 입력값입니다.")
        Double thickness,

        @NotNull(message = "너비는 필수 입력값입니다.")
        Integer width,

        @NotNull(message = "높이는 필수 입력값입니다.")
        Integer height,

        @NotNull(message = "무게 필수 입력값입니다.")
        Double weight,

        IngredientPriceRequest price,

        Integer optimalStock
) {
}
