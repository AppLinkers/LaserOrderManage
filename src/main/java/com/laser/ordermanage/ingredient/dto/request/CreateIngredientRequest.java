package com.laser.ordermanage.ingredient.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

public record CreateIngredientRequest(

        @NotEmpty(message = "재질은 필수 입력값입니다.")
        @Pattern(regexp = "^.{0,20}$", message = "재질의 최대 글자수는 20자입니다.")
        String texture,

        @NotNull(message = "두께는 필수 입력값입니다.")
        @DecimalMin(value = "0.1", message = "두께는 0.1 이상, 100 이사의 소수 입니다.")
        @DecimalMax(value = "100.0", message = "두께는 0.1 이상, 100 이사의 소수 입니다.")
        Double thickness,

        @NotNull(message = "너비는 필수 입력값입니다.")
        @Min(value = 1, message = "너비는 1 이상, 100 이하의 정수 입니다.")
        @Max(value = 100, message = "너비는 1 이상, 100 이하의 정수 입니다.")
        Integer width,

        @NotNull(message = "높이는 필수 입력값입니다.")
        @Min(value = 1, message = "높이는 1 이상, 100 이하의 정수 입니다.")
        @Max(value = 100, message = "높이는 1 이상, 100 이하의 정수 입니다.")
        Integer height,

        @NotNull(message = "무게는 필수 입력값입니다.")
        @DecimalMin(value = "0.1", message = "무게는 0.1 이상, 1,000 이사의 소수 입니다.")
        @DecimalMax(value = "1000.0", message = "무게는 0.1 이상, 1,000 이사의 소수 입니다.")
        Double weight,

        @Valid
        IngredientPriceRequest price,

        Integer optimalStock
) {
}
