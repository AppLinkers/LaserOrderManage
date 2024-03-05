package com.laser.ordermanage.user.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record ChangePasswordRequest (

    @NotEmpty(message = "비밀번호는 필수 입력값입니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[~`!@#$%^&*()-])(?=.*[0-9]).{8,}$", message = "비밀번호는 8 자리 이상 영문, 숫자, 특수문자를 사용하세요.")
    String password

) {}
