package com.laser.ordermanage.customer.dto.response;

import com.laser.ordermanage.common.entity.embedded.Address;
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
    public CustomerGetDeliveryAddressResponse(Long id, String name, Address address, String receiver, String phone1, String phone2, Boolean isDefault) {
        this(
                id,
                name,
                address.getZipCode(),
                address.getAddress(),
                address.getDetailAddress(),
                receiver,
                phone1,
                phone2,
                isDefault
        );
    }
}
