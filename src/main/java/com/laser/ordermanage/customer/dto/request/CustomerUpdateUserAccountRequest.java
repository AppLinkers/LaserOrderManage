package com.laser.ordermanage.customer.dto.request;

import com.laser.ordermanage.user.dto.request.UpdateUserAccountRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record CustomerUpdateUserAccountRequest (

    @NotEmpty(message = "이름은 필수 입력값입니다.")
    @Pattern(regexp = "^.{0,10}$", message = "이름의 최대 글자수는 10자입니다.")
    String name,

    @Valid
    UpdateUserAccountRequest user,

    @NotEmpty(message = "업체 이름은 필수 입력값입니다.")
    @Pattern(regexp = "^.{0,20}$", message = "업체 이름의 최대 글자수는 20자입니다.")
    String companyName

) {}
