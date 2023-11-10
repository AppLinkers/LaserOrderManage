package com.laser.ordermanage.user.domain;

import com.laser.ordermanage.common.converter.BooleanToYNConverter;
import com.laser.ordermanage.customer.domain.Customer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
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
    private String phone1;

    private String phone2;

    @NotNull
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean isDefault;

    @NotNull
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean isDeleted;
}
