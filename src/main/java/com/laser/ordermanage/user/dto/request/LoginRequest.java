package com.laser.ordermanage.user.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Builder
public record LoginRequest (

    @Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$", message = "이메일 형식에 맞지 않습니다.")
    String email,

    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[~`!@#$%^&*()-])(?=.*[0-9]).{8,}$", message = "비밀번호는 8 자리 이상 영문, 숫자, 특수문자를 사용하세요.")
    String password

) {

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(email, password);
    }
}
