package com.laser.ordermanage.customer.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record CustomerCreateOrderRequest (

    @NotEmpty(message = "거래 이름은 필수 입력값입니다.")
    @Pattern(regexp = "^.{0,20}$", message = "거래 이름의 최대 글자수는 20자입니다.")
    String name,

    List<String> manufacturing,

    List<String> postProcessing,

    @Valid
    @NotEmpty(message = "도면은 최소한 한개 이상이어야 합니다.")
    List<CustomerCreateDrawingRequest> drawingList,

    String request,

    @Valid
    CustomerCreateOrderDeliveryAddressRequest deliveryAddress,

    @NotNull(message = "신규 발급 유무는 필수 사항입니다.")
    Boolean isNewIssue

) {
    @JsonIgnore
    public String getOrderImgUrl() {
        return this.drawingList.get(0).thumbnailUrl();
    }
}
