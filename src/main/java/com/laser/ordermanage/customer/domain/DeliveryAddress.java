package com.laser.ordermanage.customer.domain;

import com.laser.ordermanage.common.converter.BooleanToYNConverter;
import com.laser.ordermanage.common.entity.CreatedAtEntity;
import com.laser.ordermanage.common.entity.embedded.Address;
import com.laser.ordermanage.customer.dto.request.CustomerCreateOrUpdateDeliveryAddressRequest;
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

    @Embedded
    private Address address;

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
    public DeliveryAddress(Customer customer, String name, Address address, String receiver, String phone1, String phone2, Boolean isDefault) {
        this.customer = customer;
        this.name = name;
        this.address = address;
        this.receiver = receiver;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.isDefault = isDefault;
    }

    public void disableDefault() {
        this.isDefault = Boolean.FALSE;
    }

    public void updateProperties(CustomerCreateOrUpdateDeliveryAddressRequest request) {
        this.name = request.name();
        this.address.updateProperties(request.zipCode(), request.address(), request.detailAddress());
        this.receiver = request.receiver();
        this.phone1 = request.phone1();
        this.phone2 = request.phone2();
    }

    public void asDefault() {
        this.isDefault = Boolean.TRUE;
    }

    public boolean isDefault() {
        return this.isDefault;
    }
}
