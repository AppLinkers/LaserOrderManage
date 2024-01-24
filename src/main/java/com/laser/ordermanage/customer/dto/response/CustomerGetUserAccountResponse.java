package com.laser.ordermanage.customer.dto.response;

import com.laser.ordermanage.common.entity.embedded.Address;
import com.querydsl.core.annotations.QueryProjection;

public record CustomerGetUserAccountResponse(
        String email,
        String name,
        String phone,
        String zipCode,
        String address,
        String detailAddress,
        String companyName,
        Boolean emailNotification
) {

    @QueryProjection
    public CustomerGetUserAccountResponse(String email, String name, String phone, Address address, String companyName, Boolean emailNotification) {
        this(
                email,
                name,
                phone,
                address.getZipCode(),
                address.getAddress(),
                address.getDetailAddress(),
                companyName,
                emailNotification
        );
    }
}
