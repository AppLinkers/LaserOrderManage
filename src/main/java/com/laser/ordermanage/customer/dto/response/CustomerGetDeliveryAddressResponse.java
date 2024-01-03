package com.laser.ordermanage.customer.dto.response;

import com.querydsl.core.annotations.QueryProjection;

public record CustomerGetDeliveryAddressResponse(
        Long id,
        String name,
        String zipCode,
        String address,
        String detailAddress,
        String receiver,
        String phone1,
        String phone2,
        Boolean isDefault
) {
    @QueryProjection
    public CustomerGetDeliveryAddressResponse(Long id, String name, String zipCode, String address, String detailAddress, String receiver, String phone1, String phone2, Boolean isDefault) {
        this.id = id;
        this.name = name;
        this.zipCode = zipCode;
        this.address = address;
        this.detailAddress = detailAddress;
        this.receiver = receiver;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.isDefault = isDefault;
    }
}
