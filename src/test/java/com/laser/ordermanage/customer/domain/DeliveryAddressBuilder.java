package com.laser.ordermanage.customer.domain;

import com.laser.ordermanage.common.entity.embedded.Address;
import org.assertj.core.api.Assertions;

public class DeliveryAddressBuilder {
    public static DeliveryAddress build() {
        Customer customer = CustomerBuilder.build();
        customer.disableNewCustomer();

        Address address = Address.builder()
                .zipCode("11111")
                .address("배송지 1 기본 주소")
                .detailAddress("배송지 1 상세 주소")
                .build();

        return DeliveryAddress.builder()
                .customer(customer)
                .name("고객 1 배송지 1")
                .address(address)
                .receiver("배송지 1 수신자")
                .phone1("01011111111")
                .phone2("01011112222")
                .build();
    }

    public static DeliveryAddress build2() {
        Customer customer = CustomerBuilder.build();
        customer.disableNewCustomer();

        Address address = Address.builder()
                .zipCode("22222")
                .address("배송지 2 기본 주소")
                .detailAddress("배송지 2 상세 주소")
                .build();

        return DeliveryAddress.builder()
                .customer(customer)
                .name("고객 1 배송지 2")
                .address(address)
                .receiver("배송지 2 수신자")
                .phone1("01022221111")
                .phone2("01022222222")
                .build();
    }

    public static void assertDeliveryAddress(DeliveryAddress actualDeliveryAddress, DeliveryAddress expectedDeliveryAddress) {
        CustomerBuilder.assertCustomer(actualDeliveryAddress.getCustomer(), expectedDeliveryAddress.getCustomer());
        Assertions.assertThat(actualDeliveryAddress.getName()).isEqualTo(expectedDeliveryAddress.getName());
        Assertions.assertThat(actualDeliveryAddress.getAddress().getZipCode()).isEqualTo(expectedDeliveryAddress.getAddress().getZipCode());
        Assertions.assertThat(actualDeliveryAddress.getAddress().getAddress()).isEqualTo(expectedDeliveryAddress.getAddress().getAddress());
        Assertions.assertThat(actualDeliveryAddress.getAddress().getDetailAddress()).isEqualTo(expectedDeliveryAddress.getAddress().getDetailAddress());
        Assertions.assertThat(actualDeliveryAddress.getReceiver()).isEqualTo(expectedDeliveryAddress.getReceiver());
        Assertions.assertThat(actualDeliveryAddress.getPhone1()).isEqualTo(expectedDeliveryAddress.getPhone1());
        Assertions.assertThat(actualDeliveryAddress.getPhone2()).isEqualTo(expectedDeliveryAddress.getPhone2());

    }
}
