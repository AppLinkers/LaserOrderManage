package com.laser.ordermanage.customer.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class CreateCustomerDeliveryAddressRequest {

    @NotEmpty(message = "배송지 이름은 필수 입력값입니다.")
    @Pattern(regexp = "^.{0,20}$", message = "배송지 이름의 최대 글자수는 20자입니다.")
    private String deliveryName;

    @NotEmpty(message = "우편번호는 필수 입력값입니다.")
    @Pattern(regexp = "^\\d{5}", message = "우편번호는 5자리 정수입니다.")
    private String zipCode;

    @NotEmpty(message = "기본 주소는 필수 입력값입니다.")
    private String address;

    private String detailAddress;

    @NotEmpty(message = "수신자는 필수 입력값입니다.")
    @Pattern(regexp = "^.{0,10}$", message = "수신자의 최대 글자수는 10자입니다.")
    private String receiver;

    @NotEmpty(message = "연락처는 필수 입력값입니다.")
    @Pattern(regexp = "^\\d{3}\\d{3,4}\\d{4}$", message = "연락처 형식에 맞지 않습니다.")
    private String phone1;

    @Pattern(regexp = "^\\d{3}\\d{3,4}\\d{4}$", message = "연락처 형식에 맞지 않습니다.")
    private String phone2;

    @NotNull
    private Boolean isDefault;

}
