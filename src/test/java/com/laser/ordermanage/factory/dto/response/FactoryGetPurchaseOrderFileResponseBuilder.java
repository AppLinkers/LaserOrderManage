package com.laser.ordermanage.factory.dto.response;

import com.laser.ordermanage.order.domain.PurchaseOrder;
import com.laser.ordermanage.order.domain.PurchaseOrderBuilder;

public class FactoryGetPurchaseOrderFileResponseBuilder {
    public static FactoryGetPurchaseOrderFileResponse build() {
        PurchaseOrder purchaseOrder = PurchaseOrderBuilder.build();

        return FactoryGetPurchaseOrderFileResponse.builder()
                .id(1L)
                .fileName(purchaseOrder.getFile().getName())
                .fileUrl(purchaseOrder.getFile().getUrl())
                .build();
    }
}
