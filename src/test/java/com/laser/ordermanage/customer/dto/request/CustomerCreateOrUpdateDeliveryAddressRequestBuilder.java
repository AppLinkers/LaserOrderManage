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
}
