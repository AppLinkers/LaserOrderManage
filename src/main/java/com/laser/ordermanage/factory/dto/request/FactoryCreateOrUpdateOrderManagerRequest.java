package com.laser.ordermanage.factory.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record FactoryCreateOrUpdateOrderManagerRequest (

    @NotEmpty(message = "거래 담당자 이름은 필수 입력값입니다.")
    @Pattern(regexp = "^.{0,10}$", message = "거래 담당자 이름의 최대 글자수는 10자입니다.")
    String name,

    @NotEmpty(message = "연락처는 필수 입력값입니다.")
    @Pattern(regexp = "^\\d{3}\\d{3,4}\\d{4}$", message = "연락처 형식에 맞지 않습니다.")
    String phone

) {}
