package com.laser.ordermanage.order.dto.response;

import com.laser.ordermanage.common.entity.embedded.Address;
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
    public GetOrderDeliveryAddressResponse(Long id, String name, Address address, String receiver, String phone1, String phone2) {
        this(
                id,
                name,
                address.getZipCode(),
                address.getAddress(),
                address.getDetailAddress(),
                receiver,
                phone1,
                phone2
        );
    }
}
