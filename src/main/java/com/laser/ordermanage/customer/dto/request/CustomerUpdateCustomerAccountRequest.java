package com.laser.ordermanage.customer.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record CustomerUpdateCustomerAccountRequest (

    @NotEmpty(message = "업체 이름은 필수 입력값입니다.")
    @Pattern(regexp = "^.{0,20}$", message = "업체 이름의 최대 글자수는 20자입니다.")
    String companyName

) {}
