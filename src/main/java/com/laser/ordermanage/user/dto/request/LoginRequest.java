package com.laser.ordermanage.user.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Getter
public class LoginRequest {

    @NotEmpty(message = "이메일은 필수 입력값입니다.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
    private String email;

    @NotEmpty(message = "비밀번호는 필수 입력값입니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[~`!@#$%^&*()-])(?=.*[0-9]).{8,}$", message = "비밀번호는 8 자리 이상 영문, 숫자, 특수문자를 사용하세요.")
    private String password;

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(email, password);
    }

    @Builder
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
