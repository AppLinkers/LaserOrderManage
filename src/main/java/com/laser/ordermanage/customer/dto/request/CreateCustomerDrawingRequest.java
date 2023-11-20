package com.laser.ordermanage.customer.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class CreateCustomerDrawingRequest {

    private String thumbnailImgUrl;

    private String fileName;

    private Long fileSize;

    private String fileType;

    private String fileUrl;


    @Min(value = 1, message = "수량은 1 이상, 100 이하의 정수 입니다.")
    @Max(value = 100, message = "수량은 1 이상, 100 이하의 정수 입니다.")
    private Integer count;

    @NotEmpty(message = "재료 선택은 필수 사항입니다.")
    private String ingredient;
}
