package com.laser.ordermanage.factory.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class FactoryGetUserAccountResponse {

    private final String email;

    private final String companyName;

    private final String representative;

    private final String phone;

    private final String fax;

    private final String zipCode;

    private final String address;

    private final String detailAddress;

    private final Boolean emailNotification;

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
