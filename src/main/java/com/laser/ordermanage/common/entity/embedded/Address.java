package com.laser.ordermanage.common.entity.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Address {

    @Column(name = "zip_code", nullable = false, length = 5)
    private String zipCode;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "detail_address", length = 30)
    private String detailAddress;

    @Builder
    public Address(String zipCode, String address, String detailAddress) {
        this.zipCode = zipCode;
        this.address = address;
        this.detailAddress = detailAddress;
    }

    public void updateProperties(String zipCode, String address, String detailAddress) {
        this.zipCode = zipCode;
        this.address = address;
        this.detailAddress = detailAddress;
    }
}
