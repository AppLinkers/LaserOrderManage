package com.laser.ordermanage.order.dto.response;

import com.laser.ordermanage.customer.dto.response.GetCustomerResponse;
import com.querydsl.core.annotations.QueryProjection;

public record GetOrderDetailResponse (
        GetCustomerResponse customer,
        GetOrderResponse order,
        GetQuotationResponse quotation,
        GetPurchaseOrderResponse purchaseOrder,
        GetAcquireResponse acquire
) {
    @QueryProjection
    public GetOrderDetailResponse(GetCustomerResponse customer, GetOrderResponse order, GetQuotationResponse quotation, GetPurchaseOrderResponse purchaseOrder, GetAcquireResponse acquire) {
        this.customer = customer;
        this.order = order;
        this.quotation = quotation;
        this.purchaseOrder = purchaseOrder;
        this.acquire = acquire;
    }
}
