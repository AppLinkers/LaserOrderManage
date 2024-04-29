package com.laser.ordermanage.customer.dto.request;

public class CustomerCreateOrUpdateDeliveryAddressRequestBuilder {
    public static CustomerCreateOrUpdateDeliveryAddressRequest createBuild() {
        return CustomerCreateOrUpdateDeliveryAddressRequest.builder()
                .name("배송지 이름")
                .zipCode("11111")
                .address("기본 주소")
                .detailAddress("상세 주소")
                .receiver("수신자")
                .phone1("01011111111")
                .phone2("01011112222")
                .isDefault(Boolean.TRUE)
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

    public static CustomerCreateOrUpdateDeliveryAddressRequest defaultDeliveryAddressDisableUpdateBuild() {
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

    public static CustomerCreateOrUpdateDeliveryAddressRequest nullNameBuild() {
        return CustomerCreateOrUpdateDeliveryAddressRequest.builder()
                .name(null)
                .zipCode("11111")
                .address("기본 주소")
                .detailAddress("상세 주소")
                .receiver("수신자")
                .phone1("01011111111")
                .phone2("01011112222")
                .isDefault(Boolean.FALSE)
                .build();
    }

    public static CustomerCreateOrUpdateDeliveryAddressRequest emptyNameBuild() {
        return CustomerCreateOrUpdateDeliveryAddressRequest.builder()
                .name("")
                .zipCode("11111")
                .address("기본 주소")
                .detailAddress("상세 주소")
                .receiver("수신자")
                .phone1("01011111111")
                .phone2("01011112222")
                .isDefault(Boolean.FALSE)
                .build();
    }

    public static CustomerCreateOrUpdateDeliveryAddressRequest invalidNameBuild() {
        return CustomerCreateOrUpdateDeliveryAddressRequest.builder()
                .name("배송지 이름".repeat(4))
                .zipCode("11111")
                .address("기본 주소")
                .detailAddress("상세 주소")
                .receiver("수신자")
                .phone1("01011111111")
                .phone2("01011112222")
                .isDefault(Boolean.FALSE)
                .build();
    }

    public static CustomerCreateOrUpdateDeliveryAddressRequest nullZipCodeBuild() {
        return CustomerCreateOrUpdateDeliveryAddressRequest.builder()
                .name("배송지 이름")
                .zipCode(null)
                .address("기본 주소")
                .detailAddress("상세 주소")
                .receiver("수신자")
                .phone1("01011111111")
                .phone2("01011112222")
                .isDefault(Boolean.FALSE)
                .build();
    }

    public static CustomerCreateOrUpdateDeliveryAddressRequest invalidZipCodeBuild() {
        return CustomerCreateOrUpdateDeliveryAddressRequest.builder()
                .name("배송지 이름")
                .zipCode("invalid zipCode")
                .address("기본 주소")
                .detailAddress("상세 주소")
                .receiver("수신자")
                .phone1("01011111111")
                .phone2("01011112222")
                .isDefault(Boolean.FALSE)
                .build();
    }

    public static CustomerCreateOrUpdateDeliveryAddressRequest nullAddressBuild() {
        return CustomerCreateOrUpdateDeliveryAddressRequest.builder()
                .name("배송지 이름")
                .zipCode("11111")
                .address(null)
                .detailAddress("상세 주소")
                .receiver("수신자")
                .phone1("01011111111")
                .phone2("01011112222")
                .isDefault(Boolean.FALSE)
                .build();
    }

    public static CustomerCreateOrUpdateDeliveryAddressRequest emptyAddressBuild() {
        return CustomerCreateOrUpdateDeliveryAddressRequest.builder()
                .name("배송지 이름")
                .zipCode("11111")
                .address("")
                .detailAddress("상세 주소")
                .receiver("수신자")
                .phone1("01011111111")
                .phone2("01011112222")
                .isDefault(Boolean.FALSE)
                .build();
    }

    public static CustomerCreateOrUpdateDeliveryAddressRequest invalidDetailAddress() {
        return CustomerCreateOrUpdateDeliveryAddressRequest.builder()
                .name("배송지 이름")
                .zipCode("11111")
                .address("기본 주소")
                .detailAddress("상세 주소".repeat(7))
                .receiver("수신자")
                .phone1("01011111111")
                .phone2("01011112222")
                .isDefault(Boolean.FALSE)
                .build();
    }

    public static CustomerCreateOrUpdateDeliveryAddressRequest nullReceiverBuild() {
        return CustomerCreateOrUpdateDeliveryAddressRequest.builder()
                .name("배송지 이름")
                .zipCode("11111")
                .address("기본 주소")
                .detailAddress("상세 주소")
                .receiver(null)
                .phone1("01011111111")
                .phone2("01011112222")
                .isDefault(Boolean.FALSE)
                .build();
    }

    public static CustomerCreateOrUpdateDeliveryAddressRequest emptyReceiverBuild() {
        return CustomerCreateOrUpdateDeliveryAddressRequest.builder()
                .name("배송지 이름")
                .zipCode("11111")
                .address("기본 주소")
                .detailAddress("상세 주소")
                .receiver("")
                .phone1("01011111111")
                .phone2("01011112222")
                .isDefault(Boolean.FALSE)
                .build();
    }

    public static CustomerCreateOrUpdateDeliveryAddressRequest invalidReceiverBuild() {
        return CustomerCreateOrUpdateDeliveryAddressRequest.builder()
                .name("배송지 이름")
                .zipCode("11111")
                .address("기본 주소")
                .detailAddress("상세 주소")
                .receiver("수신자".repeat(4))
                .phone1("01011111111")
                .phone2("01011112222")
                .isDefault(Boolean.FALSE)
                .build();
    }

    public static CustomerCreateOrUpdateDeliveryAddressRequest nullPhone1Build() {
        return CustomerCreateOrUpdateDeliveryAddressRequest.builder()
                .name("배송지 이름")
                .zipCode("11111")
                .address("기본 주소")
                .detailAddress("상세 주소")
                .receiver("수신자")
                .phone1(null)
                .phone2("01011112222")
                .isDefault(Boolean.FALSE)
                .build();
    }

    public static CustomerCreateOrUpdateDeliveryAddressRequest invalidPhone1Build() {
        return CustomerCreateOrUpdateDeliveryAddressRequest.builder()
                .name("배송지 이름")
                .zipCode("11111")
                .address("기본 주소")
                .detailAddress("상세 주소")
                .receiver("수신자")
                .phone1("invalid phone 1")
                .phone2("01011112222")
                .isDefault(Boolean.FALSE)
                .build();
    }

    public static CustomerCreateOrUpdateDeliveryAddressRequest invalidPhone2Build() {
        return CustomerCreateOrUpdateDeliveryAddressRequest.builder()
                .name("배송지 이름")
                .zipCode("11111")
                .address("기본 주소")
                .detailAddress("상세 주소")
                .receiver("수신자")
                .phone1("01011111111")
                .phone2("invalid phone 2")
                .isDefault(Boolean.FALSE)
                .build();
    }

    public static CustomerCreateOrUpdateDeliveryAddressRequest nullIsDefaultBuild() {
        return CustomerCreateOrUpdateDeliveryAddressRequest.builder()
                .name("배송지 이름")
                .zipCode("11111")
                .address("기본 주소")
                .detailAddress("상세 주소")
                .receiver("수신자")
                .phone1("01011111111")
                .phone2("01011112222")
                .isDefault(null)
                .build();
    }
}
