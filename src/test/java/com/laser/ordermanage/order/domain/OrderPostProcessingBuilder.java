package com.laser.ordermanage.order.domain;

import org.assertj.core.api.Assertions;

public class OrderPostProcessingBuilder {
    public static void assertOrderPostProcessing(OrderPostProcessing actualOrderPostProcessing, OrderPostProcessing expectedOrderPostProcessing) {
        Assertions.assertThat(actualOrderPostProcessing.getIsPainting()).isEqualTo(expectedOrderPostProcessing.getIsPainting());
        Assertions.assertThat(actualOrderPostProcessing.getIsPlating()).isEqualTo(expectedOrderPostProcessing.getIsPlating());
    }
}
