package com.laser.ordermanage.customer.dto.response;

public class CustomerCreateOrUpdateOrderPurchaseOrderResponseBuilder {
    public static CustomerCreateOrUpdateOrderPurchaseOrderResponse build() {
        return new CustomerCreateOrUpdateOrderPurchaseOrderResponse(null, "purchase-order.png", "https://ordermanage.s3.ap-northeast-2.amazonaws.com/purchase-order/purchase-order.png");
    }

    public static CustomerCreateOrUpdateOrderPurchaseOrderResponse createBuild() {
        return new CustomerCreateOrUpdateOrderPurchaseOrderResponse(9L, "purchase-order.png", "https://ordermanage.s3.ap-northeast-2.amazonaws.com/purchase-order/purchase-order.png");
    }

    public static CustomerCreateOrUpdateOrderPurchaseOrderResponse updateBuild() {
        return new CustomerCreateOrUpdateOrderPurchaseOrderResponse(7L, "purchase-order.png", "https://ordermanage.s3.ap-northeast-2.amazonaws.com/purchase-order/purchase-order.png");
    }
}
