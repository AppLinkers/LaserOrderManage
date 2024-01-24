package com.laser.ordermanage.factory.dto.response;

import com.laser.ordermanage.common.entity.embedded.Address;
import com.querydsl.core.annotations.QueryProjection;

public record FactoryGetUserAccountResponse(
        String email,
        String companyName,
        String representative,
        String phone,
        String fax,
        String zipCode,
        String address,
        String detailAddress,
        Boolean emailNotification
) {
    @QueryProjection
    public FactoryGetUserAccountResponse(String email, String companyName, String representative, String phone, String fax, Address address, Boolean emailNotification) {
        this(
                email,
                companyName,
                representative,
                phone,
                fax,
                address.getZipCode(),
                address.getAddress(),
                address.getDetailAddress(),
                emailNotification
        );
    }
}
