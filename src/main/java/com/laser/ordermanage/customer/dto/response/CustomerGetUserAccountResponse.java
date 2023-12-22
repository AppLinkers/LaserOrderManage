package com.laser.ordermanage.customer.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class CustomerGetUserAccountResponse {

    private final String email;

    private final String name;

    private final String phone;

    private final String zipCode;

    private final String address;

    private final String detailAddress;

    private final String companyName;

    private final Boolean emailNotification;

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
