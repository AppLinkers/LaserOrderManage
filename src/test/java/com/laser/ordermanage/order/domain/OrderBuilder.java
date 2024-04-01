package com.laser.ordermanage.order.domain;

import com.laser.ordermanage.customer.domain.Customer;
import com.laser.ordermanage.user.domain.UserEntity;
import com.laser.ordermanage.user.domain.UserEntityBuilder;
import org.assertj.core.api.Assertions;

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

        OrderDeliveryAddress deliveryAddress = OrderDeliveryAddressBuilder.build();

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

    public static void assertOrder(Order actualOrder, Order expectedOrder) {
        UserEntityBuilder.assertUserEntity(actualOrder.getCustomer().getUser(), expectedOrder.getCustomer().getUser());
        Assertions.assertThat(actualOrder.getCustomer().getCompanyName()).isEqualTo(expectedOrder.getCustomer().getCompanyName());
        Assertions.assertThat(actualOrder.getCustomer().getIsNew()).isEqualTo(expectedOrder.getCustomer().getIsNew());
        OrderDeliveryAddressBuilder.assertOrderDeliveryAddress(actualOrder.getDeliveryAddress(), expectedOrder.getDeliveryAddress());
        Assertions.assertThat(actualOrder.getName()).isEqualTo(expectedOrder.getName());
        Assertions.assertThat(actualOrder.getImgUrl()).isEqualTo(expectedOrder.getImgUrl());
        Assertions.assertThat(actualOrder.getStage()).isEqualTo(expectedOrder.getStage());
        OrderManufacturingBuilder.assertOrderManufacturing(actualOrder.getManufacturing(), expectedOrder.getManufacturing());
        OrderPostProcessingBuilder.assertOrderPostProcessing(actualOrder.getPostProcessing(), expectedOrder.getPostProcessing());
        Assertions.assertThat(actualOrder.getRequest()).isEqualTo(expectedOrder.getRequest());
        Assertions.assertThat(actualOrder.getIsNewIssue()).isEqualTo(expectedOrder.getIsNewIssue());
        Assertions.assertThat(actualOrder.getIsDeleted()).isEqualTo(expectedOrder.getIsDeleted());
    }
}
