package com.laser.ordermanage.order.domain;

import com.laser.ordermanage.common.entity.embedded.Address;
import com.laser.ordermanage.customer.domain.Customer;
import com.laser.ordermanage.user.domain.UserEntity;
import com.laser.ordermanage.user.domain.UserEntityBuilder;

import java.util.List;

public class OrderBuilder {

    public static Order build() {
        UserEntity user = UserEntityBuilder.build();

        Customer customer = Customer.builder()
                .user(user)
                .companyName("고객 회사 이름")
                .build();

        Address address = Address.builder()
                .zipCode("111111")
                .address("주소")
                .detailAddress("상세 주소")
                .build();

        OrderDeliveryAddress deliveryAddress = OrderDeliveryAddress.builder()
                .name("거래 배송지 이름")
                .address(address)
                .receiver("거래 배송지 수신자")
                .phone1("01011111111")
                .phone2("01011112222")
                .build();

        OrderManufacturing manufacturing = OrderManufacturing.ofRequest(List.of("laser-cutting", "bending"));
        OrderPostProcessing postProcessing = OrderPostProcessing.ofRequest(List.of("painting", "plating"));

        return Order.builder()
                .customer(customer)
                .deliveryAddress(deliveryAddress)
                .name("거래 이름")
                .imgUrl("거래 이미지 url")
                .manufacturing(manufacturing)
                .postProcessing(postProcessing)
                .request("거래 요청 사항")
                .isNewIssue(Boolean.FALSE)
                .build();
    }
}
