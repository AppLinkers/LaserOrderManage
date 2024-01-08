package com.laser.ordermanage.user.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record VerifyEmailRequest (

    @NotEmpty(message = "이메일은 필수 입력값입니다.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
    String email,

    @NotEmpty(message = "이메일 인증코드는 필수 입력값입니다.")
    @Pattern(regexp = "\\d{6}", message = "이메일 인증코드는 6자리 정수입니다.")
    String code

) {}
