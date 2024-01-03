package com.laser.ordermanage.user.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record UpdateUserAccountRequest (

    @NotEmpty(message = "연락처는 필수 입력값입니다.")
    @Pattern(regexp = "^\\d{3}\\d{3,4}\\d{4}$", message = "연락처 형식에 맞지 않습니다.")
    String phone,

    @NotEmpty(message = "우편번호는 필수 입력값입니다.")
    @Pattern(regexp = "^\\d{5}", message = "우편번호는 5자리 정수입니다.")
    String zipCode,

    @NotEmpty(message = "기본 주소는 필수 입력값입니다.")
    String address,

    String detailAddress

) {}
