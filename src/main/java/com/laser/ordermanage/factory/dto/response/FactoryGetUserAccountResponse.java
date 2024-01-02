package com.laser.ordermanage.factory.dto.response;

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
    public FactoryGetUserAccountResponse(String email, String companyName, String representative, String phone, String fax, String zipCode, String address, String detailAddress, Boolean emailNotification) {
        this.email = email;
        this.companyName = companyName;
        this.representative = representative;
        this.phone = phone;
        this.fax = fax;
        this.zipCode = zipCode;
        this.address = address;
        this.detailAddress = detailAddress;
        this.emailNotification = emailNotification;
    }
}
