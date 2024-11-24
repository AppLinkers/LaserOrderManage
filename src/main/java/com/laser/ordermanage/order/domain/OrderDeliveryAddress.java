package com.laser.ordermanage.order.domain;

import com.laser.ordermanage.common.entity.embedded.Address;
import com.laser.ordermanage.customer.domain.DeliveryAddress;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_delivery_address")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrderDeliveryAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

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

    @Builder
    public OrderDeliveryAddress(String name, Address address, String receiver, String phone1, String phone2) {
        this.name = name;
        this.address = address;
        this.receiver = receiver;
        this.phone1 = phone1;
        this.phone2 = phone2;
    }

    public void updateDeliveryAddress(DeliveryAddress deliveryAddress) {
        this.name = deliveryAddress.getName();
        this.address = deliveryAddress.getAddress();
        this.receiver = deliveryAddress.getReceiver();
        this.phone1 = deliveryAddress.getPhone1();
        this.phone2 = deliveryAddress.getPhone2();
    }
}
