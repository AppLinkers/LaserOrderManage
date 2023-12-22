package com.laser.ordermanage.user.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RequestPasswordChangeRequest {

    @NotEmpty(message = "이메일은 필수 입력값입니다.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
    private String email;

    @NotEmpty(message = "base URL 은 필수 입력값입니다.")
    @Pattern(regexp="^((http(s?))\\:\\/\\/)([0-9a-zA-Z\\-]+\\.)+[a-zA-Z]{2,6}(\\:[0-9]+)?(\\/\\S*)?$", message = "base URL 형식이 유효하지 않습니다.")
    private String baseUrl;

    @Builder
    public RequestPasswordChangeRequest(String email, String baseUrl) {
        this.email = email;
        this.baseUrl = baseUrl;
    }
}
