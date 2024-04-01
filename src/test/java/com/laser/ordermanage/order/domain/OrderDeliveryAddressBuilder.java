package com.laser.ordermanage.order.domain;

import com.laser.ordermanage.common.entity.embedded.Address;
import org.assertj.core.api.Assertions;

public class
OrderDeliveryAddressBuilder {
    public static OrderDeliveryAddress build() {
        Address address = Address.builder()
                .zipCode("11111")
                .address("배송지 1 기본 주소")
                .detailAddress("배송지 1 상세 주소")
                .build();

        return OrderDeliveryAddress.builder()
                .name("고객 1 배송지 1")
                .address(address)
                .receiver("배송지 1 수신자")
                .phone1("01011111111")
                .phone2("01011112222")
                .build();
    }

    public static OrderDeliveryAddress build2() {
        Address address = Address.builder()
                .zipCode("22222")
                .address("배송지 2 기본 주소")
                .detailAddress("배송지 2 상세 주소")
                .build();

        return OrderDeliveryAddress.builder()
                .name("고객 1 배송지 2")
                .address(address)
                .receiver("배송지 2 수신자")
                .phone1("01022221111")
                .phone2("01022222222")
                .build();
    }

    public static void assertOrderDeliveryAddress(OrderDeliveryAddress actualOrderDeliveryAddress, OrderDeliveryAddress expectedOrderDeliveryAddress) {
        Assertions.assertThat(actualOrderDeliveryAddress.getName()).isEqualTo(expectedOrderDeliveryAddress.getName());
        Assertions.assertThat(actualOrderDeliveryAddress.getAddress().getZipCode()).isEqualTo(expectedOrderDeliveryAddress.getAddress().getZipCode());
        Assertions.assertThat(actualOrderDeliveryAddress.getAddress().getAddress()).isEqualTo(expectedOrderDeliveryAddress.getAddress().getAddress());
        Assertions.assertThat(actualOrderDeliveryAddress.getAddress().getDetailAddress()).isEqualTo(expectedOrderDeliveryAddress.getAddress().getDetailAddress());
        Assertions.assertThat(actualOrderDeliveryAddress.getReceiver()).isEqualTo(expectedOrderDeliveryAddress.getReceiver());
        Assertions.assertThat(actualOrderDeliveryAddress.getPhone1()).isEqualTo(expectedOrderDeliveryAddress.getPhone1());
        Assertions.assertThat(actualOrderDeliveryAddress.getPhone2()).isEqualTo(expectedOrderDeliveryAddress.getPhone2());
    }
}
