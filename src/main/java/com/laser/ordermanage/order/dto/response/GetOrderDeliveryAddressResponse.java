package com.laser.ordermanage.order.dto.response;

import com.querydsl.core.annotations.QueryProjection;

public record GetOrderDeliveryAddressResponse(
        Long id,
        String name,
        String zipCode,
        String address,
        String detailAddress,
        String receiver,
        String phone1,
        String phone2
) {
    @QueryProjection
    public GetOrderDeliveryAddressResponse(Long id, String name, String zipCode, String address, String detailAddress, String receiver, String phone1, String phone2) {
        this.id = id;
        this.name = name;
        this.zipCode = zipCode;
        this.address = address;
        this.detailAddress = detailAddress;
        this.receiver = receiver;
        this.phone1 = phone1;
        this.phone2 = phone2;
    }
}
