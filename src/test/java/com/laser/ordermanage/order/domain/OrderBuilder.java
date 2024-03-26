package com.laser.ordermanage.order.domain;

import com.laser.ordermanage.common.entity.embedded.Address;
import com.laser.ordermanage.customer.domain.Customer;
import com.laser.ordermanage.user.domain.UserEntity;
import com.laser.ordermanage.user.domain.UserEntityBuilder;

import java.util.ArrayList;
import java.util.List;

public class OrderBuilder {

    public static Order build() {
        UserEntity user = UserEntityBuilder.build();

        Customer customer = Customer.builder()
                .user(user)
                .companyName("고객 회사 이름 1")
                .build();
        customer.disableNewCustomer();

        Address address = Address.builder()
                .zipCode("11111")
                .address("배송지 1 기본 주소")
                .detailAddress("배송지 1 상세 주소")
                .build();

        OrderDeliveryAddress deliveryAddress = OrderDeliveryAddress.builder()
                .name("고객 1 배송지 1")
                .address(address)
                .receiver("배송지 1 수신자")
                .phone1("01011111111")
                .phone2("01011112222")
                .build();

        OrderManufacturing manufacturing = OrderManufacturing.ofRequest(List.of("bending"));
        OrderPostProcessing postProcessing = OrderPostProcessing.ofRequest(new ArrayList<>());

        Order order = Order.builder()
                .customer(customer)
                .deliveryAddress(deliveryAddress)
                .name("거래 1 이름")
                .imgUrl("https://ordermanage.s3.ap-northeast-2.amazonaws.com/output.png")
                .manufacturing(manufacturing)
                .postProcessing(postProcessing)
                .request(null)
                .isNewIssue(Boolean.TRUE)
                .build();

        return order;
    }
}
