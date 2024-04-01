package com.laser.ordermanage.order.domain;

import org.assertj.core.api.Assertions;

public class OrderManufacturingBuilder {
    public static void assertOrderManufacturing(OrderManufacturing actualOrderManufacturing, OrderManufacturing expectedOrderManufacturing) {
        Assertions.assertThat(actualOrderManufacturing.getIsLaserCutting()).isEqualTo(expectedOrderManufacturing.getIsLaserCutting());
        Assertions.assertThat(actualOrderManufacturing.getIsBending()).isEqualTo(expectedOrderManufacturing.getIsBending());
        Assertions.assertThat(actualOrderManufacturing.getIsWelding()).isEqualTo(expectedOrderManufacturing.getIsWelding());
    }
}
