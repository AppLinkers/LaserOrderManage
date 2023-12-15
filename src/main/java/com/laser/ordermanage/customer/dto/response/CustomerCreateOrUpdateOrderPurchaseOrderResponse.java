package com.laser.ordermanage.customer.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CustomerCreateOrUpdateOrderPurchaseOrderResponse {

    private final Long id;

    @Builder
    public CustomerCreateOrUpdateOrderPurchaseOrderResponse(Long id) {
        this.id = id;
    }
}
