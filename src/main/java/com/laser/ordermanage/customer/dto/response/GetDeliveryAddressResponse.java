package com.laser.ordermanage.customer.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class GetDeliveryAddressResponse {

    private final Long id;

    private final String name;

    private final String zipCode;

    private final String address;

    private final String detailAddress;

    private final String receiver;

    private final String phone1;

    private final String phone2;

    private final Boolean isDefault;

    private final Boolean isDeleted;

    @QueryProjection
    public GetDeliveryAddressResponse(Long id, String name, String zipCode, String address, String detailAddress, String receiver, String phone1, String phone2, Boolean isDefault, Boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.zipCode = zipCode;
        this.address = address;
        this.detailAddress = detailAddress;
        this.receiver = receiver;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.isDefault = isDefault;
        this.isDeleted = isDeleted;
    }
}
