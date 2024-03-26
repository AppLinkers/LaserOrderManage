package com.laser.ordermanage.customer.domain;

import com.laser.ordermanage.common.entity.embedded.Address;

public class DeliveryAddressBuilder {
    public static DeliveryAddress build() {
        Address address = Address.builder()
                .zipCode("11111")
                .address("배송지 1 기본 주소")
                .detailAddress("배송지 1 상세 주소")
                .build();

        return DeliveryAddress.builder()
                .name("고객 1 배송지 1")
                .address(address)
                .receiver("배송지 1 수신자")
                .phone1("01011111111")
                .phone2("01011112222")
                .build();
    }

    public static DeliveryAddress build2() {
        Address address = Address.builder()
                .zipCode("22222")
                .address("배송지 2 기본 주소")
                .detailAddress("배송지 2 상세 주소")
                .build();

        return DeliveryAddress.builder()
                .name("고객 1 배송지 2")
                .address(address)
                .receiver("배송지 2 수신자")
                .phone1("01022221111")
                .phone2("01022222222")
                .build();
    }
}
