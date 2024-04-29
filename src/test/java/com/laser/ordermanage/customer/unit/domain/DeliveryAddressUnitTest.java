package com.laser.ordermanage.customer.unit.domain;

import com.laser.ordermanage.customer.domain.DeliveryAddress;
import com.laser.ordermanage.customer.domain.DeliveryAddressBuilder;
import com.laser.ordermanage.customer.dto.request.CustomerCreateOrUpdateDeliveryAddressRequest;
import com.laser.ordermanage.customer.dto.request.CustomerCreateOrUpdateDeliveryAddressRequestBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class DeliveryAddressUnitTest {

    @Test
    public void disableDefault() {
        // given
        final DeliveryAddress deliveryAddress = DeliveryAddressBuilder.build();

        // when
        deliveryAddress.disableDefault();

        // then
        Assertions.assertThat(deliveryAddress.getIsDefault()).isFalse();
    }

    @Test
    public void updateProperties() {
        // given
        final DeliveryAddress deliveryAddress = DeliveryAddressBuilder.build();
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.updateBuild();

        // when
        deliveryAddress.updateProperties(request);

        // then
        Assertions.assertThat(deliveryAddress.getName()).isEqualTo(request.name());
        Assertions.assertThat(deliveryAddress.getAddress().getZipCode()).isEqualTo(request.zipCode());
        Assertions.assertThat(deliveryAddress.getAddress().getAddress()).isEqualTo(request.address());
        Assertions.assertThat(deliveryAddress.getAddress().getDetailAddress()).isEqualTo(request.detailAddress());
        Assertions.assertThat(deliveryAddress.getReceiver()).isEqualTo(request.receiver());
        Assertions.assertThat(deliveryAddress.getPhone1()).isEqualTo(request.phone1());
        Assertions.assertThat(deliveryAddress.getPhone2()).isEqualTo(request.phone2());
    }

    @Test
    public void asDefault() {
        // given
        final DeliveryAddress deliveryAddress = DeliveryAddressBuilder.build();

        // when
        deliveryAddress.asDefault();

        // then
        Assertions.assertThat(deliveryAddress.getIsDefault()).isTrue();
    }

    @Test
    public void isDefault() {
        // given
        final DeliveryAddress deliveryAddress = DeliveryAddressBuilder.build();
        deliveryAddress.asDefault();

        // when & then
        Assertions.assertThat(deliveryAddress.isDefault()).isTrue();
    }
}
