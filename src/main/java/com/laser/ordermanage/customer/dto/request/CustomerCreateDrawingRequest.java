package com.laser.ordermanage.customer.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CustomerCreateDrawingRequest(

        String thumbnailUrl,

        String fileName,

        Long fileSize,

        String fileType,

        String fileUrl,

        @NotNull(message = "수량은 필수 입력값입니다.")
        @Min(value = 1, message = "수량은 1 이상, 100 이하의 정수 입니다.")
        @Max(value = 100, message = "수량은 1 이상, 100 이하의 정수 입니다.")
        Integer count,

        @NotEmpty(message = "재료 선택은 필수 사항입니다.")
        String ingredient,

        @NotNull(message = "두께는 필수 입력값입니다.")
        @Min(value = 1, message = "두께는 1 이상, 19 이하의 정수 입니다.")
        @Max(value = 19, message = "두께는 1 이상, 19 이하의 정수 입니다.")
        Integer thickness

) { }
