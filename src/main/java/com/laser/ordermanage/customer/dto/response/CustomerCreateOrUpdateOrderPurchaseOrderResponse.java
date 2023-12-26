package com.laser.ordermanage.customer.dto.response;

import com.laser.ordermanage.order.domain.PurchaseOrder;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CustomerCreateOrUpdateOrderPurchaseOrderResponse {

    private final Long id;

    @Builder
    public CustomerCreateOrUpdateOrderPurchaseOrderResponse(Long id) {
        this.id = id;
    }

    public static CustomerCreateOrUpdateOrderPurchaseOrderResponse from(PurchaseOrder purchaseOrder) {
        return CustomerCreateOrUpdateOrderPurchaseOrderResponse.builder()
                .id(purchaseOrder.getId())
                .build();
    }
}
