package com.laser.ordermanage.customer.dto.request;

import com.laser.ordermanage.common.entity.embedded.Address;
import com.laser.ordermanage.order.domain.OrderDeliveryAddress;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CustomerCreateOrderDeliveryAddressRequest (

    @NotEmpty(message = "배송지 이름은 필수 입력값입니다.")
    @Pattern(regexp = "^.{0,20}$", message = "배송지 이름의 최대 글자수는 20자입니다.")
    String name,

    @NotNull(message = "우편번호는 필수 입력값입니다.")
    @Pattern(regexp = "^\\d{5}", message = "우편번호는 5자리 정수입니다.")
    String zipCode,

    @NotEmpty(message = "기본 주소는 필수 입력값입니다.")
    String address,

    @Pattern(regexp = "^.{0,30}$", message = "상세 주소의 최대 글자수는 30자입니다.")
    String detailAddress,

    @NotEmpty(message = "수신자는 필수 입력값입니다.")
    @Pattern(regexp = "^.{0,10}$", message = "수신자의 최대 글자수는 10자입니다.")
    String receiver,

    @NotNull(message = "연락처는 필수 입력값입니다.")
    @Pattern(regexp = "^\\d{3}\\d{3,4}\\d{4}$", message = "연락처 형식에 맞지 않습니다.")
    String phone1,

    @Pattern(regexp = "^\\d{3}\\d{3,4}\\d{4}$", message = "연락처 형식에 맞지 않습니다.")
    String phone2

) {
    public OrderDeliveryAddress toEntity() {
        return OrderDeliveryAddress.builder()
                .name(name)
                .address(Address.builder()
                        .zipCode(zipCode)
                        .address(address)
                        .detailAddress(detailAddress)
                        .build())
                .receiver(receiver)
                .phone1(phone1)
                .phone2(phone2)
                .build();
    }
}
