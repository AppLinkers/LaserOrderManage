package com.laser.ordermanage.customer.dto.request;

public class CustomerCreateOrUpdateDeliveryAddressRequestBuilder {
    public static CustomerCreateOrUpdateDeliveryAddressRequest build() {
        return CustomerCreateOrUpdateDeliveryAddressRequest.builder()
                .name("배송지 이름")
                .zipCode("11111")
                .address("기본 주소")
                .detailAddress("상세 주소")
                .receiver("수신자")
                .phone1("01011111111")
                .phone2("01011112222")
                .isDefault(Boolean.FALSE)
                .build();
    }

    public static CustomerCreateOrUpdateDeliveryAddressRequest updateBuild() {
        return CustomerCreateOrUpdateDeliveryAddressRequest.builder()
                .name("배송지 수정 이름")
                .zipCode("22222")
                .address("수정 기본 주소")
                .detailAddress("수정 상세 주소")
                .receiver("수정 수신자")
                .phone1("01022221111")
                .phone2("01022222222")
                .isDefault(Boolean.TRUE)
                .build();
    }

    public static CustomerCreateOrUpdateDeliveryAddressRequest DefaultDeliveryAddressDisableUpdateBuild() {
        return CustomerCreateOrUpdateDeliveryAddressRequest.builder()
                .name("고객 1 배송지")
                .zipCode("11111")
                .address("배송지 1 기본 주소")
                .detailAddress("배송지 1 상세 주소")
                .receiver("배송지 1 수신자")
                .phone1("01011111111")
                .phone2("01011112222")
                .isDefault(Boolean.FALSE)
                .build();
    }
}
