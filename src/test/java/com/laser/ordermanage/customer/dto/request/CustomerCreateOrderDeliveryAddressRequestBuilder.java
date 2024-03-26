package com.laser.ordermanage.customer.dto.request;

public class CustomerCreateOrderDeliveryAddressRequestBuilder {
    public static CustomerCreateOrderDeliveryAddressRequest build() {
        return new CustomerCreateOrderDeliveryAddressRequest("고객 1 배송지 1", "11111", "배송지 1 기본 주소", "배송지 1 상세 주소", "배송지 1 수신자", "01011111111", "01011112222");
    }
}
