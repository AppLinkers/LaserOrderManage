package com.laser.ordermanage.order.unit.domain;

import com.laser.ordermanage.customer.domain.DeliveryAddress;
import com.laser.ordermanage.customer.domain.DeliveryAddressBuilder;
import com.laser.ordermanage.customer.dto.request.CustomerCreateOrderDeliveryAddressRequest;
import com.laser.ordermanage.customer.dto.request.CustomerCreateOrderDeliveryAddressRequestBuilder;
import com.laser.ordermanage.order.domain.OrderDeliveryAddress;
import com.laser.ordermanage.order.domain.OrderDeliveryAddressBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class OrderDeliveryAddressUnitTest {
    @Test
    public void ofRequest() {
        // given
        final OrderDeliveryAddress expectedOrderDeliveryAddress = OrderDeliveryAddressBuilder.build();
        final CustomerCreateOrderDeliveryAddressRequest request = CustomerCreateOrderDeliveryAddressRequestBuilder.build();

        // when
        final OrderDeliveryAddress actualOrderDeliveryAddress = OrderDeliveryAddress.ofRequest(request);

        // then
        assertOrderDeliveryAddress(actualOrderDeliveryAddress, expectedOrderDeliveryAddress);
    }

    @Test
    public void updateDeliveryAddress() {
        // given
        final OrderDeliveryAddress orderDeliveryAddress = OrderDeliveryAddressBuilder.build();
        final DeliveryAddress deliveryAddress = DeliveryAddressBuilder.build2();
        final OrderDeliveryAddress expectedOrderDeliveryAddress = OrderDeliveryAddressBuilder.build2();

        // when
        orderDeliveryAddress.updateDeliveryAddress(deliveryAddress);

        // then
        assertOrderDeliveryAddress(orderDeliveryAddress, expectedOrderDeliveryAddress);
    }

    public static void assertOrderDeliveryAddress(OrderDeliveryAddress actualOrderDeliveryAddress, OrderDeliveryAddress expectedOrderDeliveryAddress) {
        Assertions.assertThat(actualOrderDeliveryAddress.getId()).isEqualTo(expectedOrderDeliveryAddress.getId());
        Assertions.assertThat(actualOrderDeliveryAddress.getName()).isEqualTo(expectedOrderDeliveryAddress.getName());
        Assertions.assertThat(actualOrderDeliveryAddress.getAddress().getZipCode()).isEqualTo(expectedOrderDeliveryAddress.getAddress().getZipCode());
        Assertions.assertThat(actualOrderDeliveryAddress.getAddress().getAddress()).isEqualTo(expectedOrderDeliveryAddress.getAddress().getAddress());
        Assertions.assertThat(actualOrderDeliveryAddress.getAddress().getDetailAddress()).isEqualTo(expectedOrderDeliveryAddress.getAddress().getDetailAddress());
        Assertions.assertThat(actualOrderDeliveryAddress.getReceiver()).isEqualTo(expectedOrderDeliveryAddress.getReceiver());
        Assertions.assertThat(actualOrderDeliveryAddress.getPhone1()).isEqualTo(expectedOrderDeliveryAddress.getPhone1());
        Assertions.assertThat(actualOrderDeliveryAddress.getPhone2()).isEqualTo(expectedOrderDeliveryAddress.getPhone2());
    }
}
