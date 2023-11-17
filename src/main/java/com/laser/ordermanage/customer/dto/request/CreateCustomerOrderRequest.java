package com.laser.ordermanage.customer.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.List;

@Getter
public class CreateCustomerOrderRequest {

    @NotEmpty(message = "거래 이름은 필수 입력값입니다.")
    @Pattern(regexp = "^.{0,20}$", message = "거래 이름의 최대 글자수는 20자입니다.")
    private String name;

    private List<String> manufacturing;

    private List<String> postProcessing;

    @Valid
    @Size(min = 1, message = "도면은 필수 사항입니다.")
    @NotNull
    private List<CreateCustomerDrawingRequest> drawingList;

    private String request;

    @NotNull(message = "주소지 선택은 필수 사항입니다.")
    private Long deliveryAddressId;

    @NotNull
    private Boolean isNewIssue;
}
