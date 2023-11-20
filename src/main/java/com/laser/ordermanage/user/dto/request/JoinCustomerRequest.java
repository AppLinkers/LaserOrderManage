package com.laser.ordermanage.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class JoinCustomerRequest {

    @NotEmpty(message = "이메일은 필수 입력값입니다.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
    private String email;

    @NotEmpty(message = "비밀번호는 필수 입력값입니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[~`!@#$%^&*()-])(?=.*[0-9]).{8,}$", message = "비밀번호는 8 자리 이상 영문, 숫자, 특수문자를 사용하세요.")
    private String password;

    @Pattern(regexp = "^.{0,10}$", message = "이름의 최대 글자수는 10자입니다.")
    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String name;

    @Pattern(regexp = "^.{0,20}$", message = "회사 이름의 최대 글자수는 20자입니다.")
    private String companyName;

    @NotEmpty(message = "연락처는 필수 입력값입니다.")
    @Pattern(regexp = "^\\d{3}\\d{3,4}\\d{4}$", message = "연락처 형식에 맞지 않습니다.")
    private String phone;

    @NotEmpty(message = "우편번호는 필수 입력값입니다.")
    @Pattern(regexp = "^\\d{5}", message = "우편번호는 5자리 정수입니다.")
    private String zipCode;

    @NotEmpty(message = "기본 주소는 필수 입력값입니다.")
    private String address;

    private String detailAddress;

}
