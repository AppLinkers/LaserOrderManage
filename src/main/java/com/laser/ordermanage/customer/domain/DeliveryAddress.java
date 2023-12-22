package com.laser.ordermanage.customer.domain;

import com.laser.ordermanage.common.converter.BooleanToYNConverter;
import com.laser.ordermanage.common.entity.CreatedAtEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "delivery_address")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DeliveryAddress extends CreatedAtEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "customer_id", nullable = false, updatable = false)
    private Customer customer;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Column(name = "zip_code", nullable = false, length = 5)
    private String zipCode;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "detail_address")
    private String detailAddress;

    @Column(name = "receiver", nullable = false, length = 10)
    private String receiver;

    @Column(name = "phone1", nullable = false, length = 11)
    private String phone1;

    @Column(name = "phone2", length = 11)
    private String phone2;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "is_default", nullable = false, length = 1)
    private Boolean isDefault;

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

    public void disableDefault() {
        this.isDefault = Boolean.FALSE;
    }
}
