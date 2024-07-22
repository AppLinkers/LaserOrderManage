package com.laser.ordermanage.customer.dto.request;

public class CustomerCreateOrderDeliveryAddressRequestBuilder {
    public static CustomerCreateOrderDeliveryAddressRequest build() {
        return new CustomerCreateOrderDeliveryAddressRequest("고객 1 배송지 1", "11111", "배송지 1 기본 주소", "배송지 1 상세 주소", "배송지 1 수신자", "01011111111", "01011112222");
    }

    public static CustomerCreateOrderDeliveryAddressRequest nullNameBuild() {
        return new CustomerCreateOrderDeliveryAddressRequest(null, "11111", "배송지 1 기본 주소", "배송지 1 상세 주소", "배송지 1 수신자", "01011111111", "01011112222");
    }

    public static CustomerCreateOrderDeliveryAddressRequest emptyNameBuild() {
        return new CustomerCreateOrderDeliveryAddressRequest("", "11111", "배송지 1 기본 주소", "배송지 1 상세 주소", "배송지 1 수신자", "01011111111", "01011112222");
    }

    public static CustomerCreateOrderDeliveryAddressRequest invalidNameBuild() {
        return new CustomerCreateOrderDeliveryAddressRequest("고객 1 배송지 1".repeat(4), "11111", "배송지 1 기본 주소", "배송지 1 상세 주소", "배송지 1 수신자", "01011111111", "01011112222");
    }

    public static CustomerCreateOrderDeliveryAddressRequest nullZipCodeBuild() {
        return new CustomerCreateOrderDeliveryAddressRequest("고객 1 배송지 1", null, "배송지 1 기본 주소", "배송지 1 상세 주소", "배송지 1 수신자", "01011111111", "01011112222");
    }

    public static CustomerCreateOrderDeliveryAddressRequest emptyZipCodeBuild() {
        return new CustomerCreateOrderDeliveryAddressRequest("고객 1 배송지 1", "", "배송지 1 기본 주소", "배송지 1 상세 주소", "배송지 1 수신자", "01011111111", "01011112222");
    }

    public static CustomerCreateOrderDeliveryAddressRequest invalidZipCodeBuild() {
        return new CustomerCreateOrderDeliveryAddressRequest("고객 1 배송지 1", "invalid zipCode", "배송지 1 기본 주소", "배송지 1 상세 주소", "배송지 1 수신자", "01011111111", "01011112222");
    }

    public static CustomerCreateOrderDeliveryAddressRequest nullAddressBuild() {
        return new CustomerCreateOrderDeliveryAddressRequest("고객 1 배송지 1", "11111", null, "배송지 1 상세 주소", "배송지 1 수신자", "01011111111", "01011112222");
    }

    public static CustomerCreateOrderDeliveryAddressRequest emptyAddressBuild() {
        return new CustomerCreateOrderDeliveryAddressRequest("고객 1 배송지 1", "11111", "", "배송지 1 상세 주소", "배송지 1 수신자", "01011111111", "01011112222");
    }

    public static CustomerCreateOrderDeliveryAddressRequest invalidDetailAddressBuild() {
        return new CustomerCreateOrderDeliveryAddressRequest("고객 1 배송지 1", "11111", "배송지 1 기본 주소", "배송지 1 상세 주소".repeat(7), "배송지 1 수신자", "01011111111", "01011112222");
    }

    public static CustomerCreateOrderDeliveryAddressRequest nullReceiverBuild() {
        return new CustomerCreateOrderDeliveryAddressRequest("고객 1 배송지 1", "11111", "배송지 1 기본 주소", "배송지 1 상세 주소", null, "01011111111", "01011112222");
    }

    public static CustomerCreateOrderDeliveryAddressRequest emptyReceiverBuild() {
        return new CustomerCreateOrderDeliveryAddressRequest("고객 1 배송지 1", "11111", "배송지 1 기본 주소", "배송지 1 상세 주소", "", "01011111111", "01011112222");
    }

    public static CustomerCreateOrderDeliveryAddressRequest invalidReceiverBuild() {
        return new CustomerCreateOrderDeliveryAddressRequest("고객 1 배송지 1", "11111", "배송지 1 기본 주소", "배송지 1 상세 주소", "배송지 1 수신자".repeat(4), "01011111111", "01011112222");
    }

    public static CustomerCreateOrderDeliveryAddressRequest nullPhone1Build() {
        return new CustomerCreateOrderDeliveryAddressRequest("고객 1 배송지 1", "11111", "배송지 1 기본 주소", "배송지 1 상세 주소", "배송지 1 수신자", null, "01011112222");
    }

    public static CustomerCreateOrderDeliveryAddressRequest emptyPhone1Build() {
        return new CustomerCreateOrderDeliveryAddressRequest("고객 1 배송지 1", "11111", "배송지 1 기본 주소", "배송지 1 상세 주소", "배송지 1 수신자", "", "01011112222");
    }

    public static CustomerCreateOrderDeliveryAddressRequest invalidPhone1Build() {
        return new CustomerCreateOrderDeliveryAddressRequest("고객 1 배송지 1", "11111", "배송지 1 기본 주소", "배송지 1 상세 주소", "배송지 1 수신자", "invalid phone 1", "01011112222");
    }

    public static CustomerCreateOrderDeliveryAddressRequest invalidPhone2Build() {
        return new CustomerCreateOrderDeliveryAddressRequest("고객 1 배송지 1", "11111", "배송지 1 기본 주소", "배송지 1 상세 주소", "배송지 1 수신자", "01011111111", "invalid phone 2");
    }
}
