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
        Assertions.assertThat(actualOrderDeliveryAddress.getId()).isEqualTo(expectedOrderDeliveryAddress.getId());
        OrderDeliveryAddressBuilder.assertOrderDeliveryAddress(actualOrderDeliveryAddress, expectedOrderDeliveryAddress);
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
        Assertions.assertThat(orderDeliveryAddress.getId()).isEqualTo(expectedOrderDeliveryAddress.getId());
        OrderDeliveryAddressBuilder.assertOrderDeliveryAddress(orderDeliveryAddress, expectedOrderDeliveryAddress);
    }
}
