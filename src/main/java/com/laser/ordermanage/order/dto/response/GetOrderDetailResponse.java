package com.laser.ordermanage.order.dto.response;

import com.laser.ordermanage.customer.dto.response.GetCustomerResponse;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class GetOrderDetailResponse {

    private GetCustomerResponse customer;

    private GetOrderResponse order;

    private GetQuotationResponse quotation;

    private GetPurchaseOrderResponse purchaseOrder;

    @QueryProjection
    public GetOrderDetailResponse(GetCustomerResponse customer, GetOrderResponse order, GetQuotationResponse quotation, GetPurchaseOrderResponse purchaseOrder) {
        this.customer = customer;
        this.order = order;
        this.quotation = quotation;
        this.purchaseOrder = purchaseOrder;
    }
}
