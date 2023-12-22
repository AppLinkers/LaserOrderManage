package com.laser.ordermanage.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class GetUserEmailRequest {

    @Pattern(regexp = "^.{0,20}$", message = "이름(상호)의 최대 글자수는 20자입니다.")
    @NotBlank(message = "이름(상호)는 필수 입력값입니다.")
    private String name;

    @NotEmpty(message = "연락처는 필수 입력값입니다.")
    @Pattern(regexp = "^\\d{3}\\d{3,4}\\d{4}$", message = "연락처 형식에 맞지 않습니다.")
    private String phone;
}
