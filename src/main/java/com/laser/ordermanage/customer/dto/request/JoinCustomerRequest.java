package com.laser.ordermanage.customer.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public class JoinCustomerRequest {

    @NotNull(message = "이메일은 필수 입력값입니다.")
    @Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$", message = "이메일 형식에 맞지 않습니다.")
    String email;

    @NotEmpty(message = "이름은 필수 입력값입니다.")
    @Pattern(regexp = "^.{0,10}$", message = "이름의 최대 글자수는 10자입니다.")
    String name;

    @Pattern(regexp = "^.{0,20}$", message = "회사 이름의 최대 글자수는 20자입니다.")
    String companyName;

    @NotNull(message = "연락처는 필수 입력값입니다.")
    @Pattern(regexp = "^\\d{3}\\d{3,4}\\d{4}$", message = "연락처 형식에 맞지 않습니다.")
    String phone;

    @NotNull(message = "우편번호는 필수 입력값입니다.")
    @Pattern(regexp = "^\\d{5}", message = "우편번호는 5자리 정수입니다.")
    String zipCode;

    @NotEmpty(message = "기본 주소는 필수 입력값입니다.")
    String address;

    @Pattern(regexp = "^.{0,30}$", message = "상세 주소의 최대 글자수는 30자입니다.")
    String detailAddress;

}
