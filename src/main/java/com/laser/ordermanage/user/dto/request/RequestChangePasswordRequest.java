package com.laser.ordermanage.user.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record RequestChangePasswordRequest (

    @NotNull(message = "이메일은 필수 입력값입니다.")
    @Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$", message = "이메일 형식에 맞지 않습니다.")
    String email,

    @NotNull(message = "base URL 은 필수 입력값입니다.")
    @Pattern(regexp="^((http(s?))\\:\\/\\/)([0-9a-zA-Z\\-]+\\.)+[a-zA-Z]{2,6}(\\:[0-9]+)?(\\/\\S*)?$", message = "base URL 형식이 유효하지 않습니다.")
    String baseUrl

) { }
