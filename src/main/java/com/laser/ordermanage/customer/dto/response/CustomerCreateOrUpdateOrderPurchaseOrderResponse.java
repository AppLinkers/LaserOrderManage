package com.laser.ordermanage.customer.dto.response;

import com.laser.ordermanage.order.domain.PurchaseOrder;

public record CustomerCreateOrUpdateOrderPurchaseOrderResponse(Long id) {
    public static CustomerCreateOrUpdateOrderPurchaseOrderResponse from(PurchaseOrder purchaseOrder) {
        return new CustomerCreateOrUpdateOrderPurchaseOrderResponse(purchaseOrder.getId());
    }
}
