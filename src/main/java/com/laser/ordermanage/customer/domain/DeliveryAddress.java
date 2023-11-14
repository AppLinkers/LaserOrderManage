package com.laser.ordermanage.customer.domain;

import com.laser.ordermanage.common.converter.BooleanToYNConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DeliveryAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    private Customer customer;

    @NotNull
    private String name;

    @NotNull
    private String zipCode;

    @NotNull
    private String address;

    private String detailAddress;

    @NotNull
    private String receiver;

    @NotNull
    private String phone1;

    private String phone2;

    @NotNull
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean isDefault;

    public void disableDefault() {
        this.isDefault = Boolean.FALSE;
    }

    @NotNull
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean isDeleted = Boolean.FALSE;

    @Builder
    public DeliveryAddress(Customer customer, String name, String zipCode, String address, String detailAddress, String receiver, String phone1, String phone2, Boolean isDefault) {
        this.customer = customer;
        this.name = name;
        this.zipCode = zipCode;
        this.address = address;
        this.detailAddress = detailAddress;
        this.receiver = receiver;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.isDefault = isDefault;
    }
}
