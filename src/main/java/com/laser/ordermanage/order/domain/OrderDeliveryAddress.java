package com.laser.ordermanage.order.domain;

import com.laser.ordermanage.customer.domain.DeliveryAddress;
import com.laser.ordermanage.customer.dto.request.CustomerCreateOrderDeliveryAddressRequest;
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

    @Builder
    public OrderDeliveryAddress(String name, String zipCode, String address, String detailAddress, String receiver, String phone1, String phone2) {
        this.name = name;
        this.zipCode = zipCode;
        this.address = address;
        this.detailAddress = detailAddress;
        this.receiver = receiver;
        this.phone1 = phone1;
        this.phone2 = phone2;
    }

    public static OrderDeliveryAddress ofRequest(CustomerCreateOrderDeliveryAddressRequest request) {
        return OrderDeliveryAddress.builder()
                .name(request.getName())
                .zipCode(request.getZipCode())
                .address(request.getZipCode())
                .detailAddress(request.getDetailAddress())
                .receiver(request.getReceiver())
                .phone1(request.getPhone1())
                .phone2(request.getPhone2())
                .build();
    }

    public void updateProperties(DeliveryAddress deliveryAddress) {
        this.name = deliveryAddress.getName();
        this.zipCode = deliveryAddress.getZipCode();
        this.address = deliveryAddress.getAddress();
        this.detailAddress = deliveryAddress.getDetailAddress();
        this.receiver = deliveryAddress.getReceiver();
        this.phone1 = deliveryAddress.getPhone1();
        this.phone2 = deliveryAddress.getPhone2();
    }
}
