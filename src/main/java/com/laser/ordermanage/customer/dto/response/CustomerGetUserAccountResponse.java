package com.laser.ordermanage.customer.dto.response;

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
    public CustomerGetUserAccountResponse(String email, String name, String phone, String zipCode, String address, String detailAddress, String companyName, Boolean emailNotification) {
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.zipCode = zipCode;
        this.address = address;
        this.detailAddress = detailAddress;
        this.companyName = companyName;
        this.emailNotification = emailNotification;
    }
}
