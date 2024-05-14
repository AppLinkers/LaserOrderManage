package com.laser.ordermanage.customer.dto.request;

import java.time.LocalDate;

public class CustomerCreateOrUpdateOrderPurchaseOrderRequestBuilder {
    public static CustomerCreateOrUpdateOrderPurchaseOrderRequest createBuild() {
        return new CustomerCreateOrUpdateOrderPurchaseOrderRequest(LocalDate.of(2023, 10, 25), "신규 검수 조건", LocalDate.of(2023, 10, 25));
    }

    public static CustomerCreateOrUpdateOrderPurchaseOrderRequest updateBuild() {
        return new CustomerCreateOrUpdateOrderPurchaseOrderRequest(LocalDate.of(2023, 11, 2), "수정 검수 조건", LocalDate.of(2023, 11, 2));
    }
}
