package com.laser.ordermanage.customer.dto.request;

import java.time.LocalDate;

public class CustomerCreateOrUpdateOrderPurchaseOrderRequestBuilder {
    public static CustomerCreateOrUpdateOrderPurchaseOrderRequest createBuild() {
        return new CustomerCreateOrUpdateOrderPurchaseOrderRequest(LocalDate.of(2023, 10, 25), "신규 검수 조건", LocalDate.of(2023, 10, 25));
    }

    public static CustomerCreateOrUpdateOrderPurchaseOrderRequest updateBuild() {
        return new CustomerCreateOrUpdateOrderPurchaseOrderRequest(LocalDate.of(2023, 11, 2), "수정 검수 조건", LocalDate.of(2023, 11, 2));
    }

    public static CustomerCreateOrUpdateOrderPurchaseOrderRequest nullInspectionPeriodBuild() {
        return new CustomerCreateOrUpdateOrderPurchaseOrderRequest(null, "신규 검수 조건", LocalDate.of(2023, 10, 25));
    }

    public static CustomerCreateOrUpdateOrderPurchaseOrderRequest nullInspectionConditionBuild() {
        return new CustomerCreateOrUpdateOrderPurchaseOrderRequest(LocalDate.of(2023, 10, 25), null, LocalDate.of(2023, 10, 25));
    }

    public static CustomerCreateOrUpdateOrderPurchaseOrderRequest emptyInspectionConditionBuild() {
        return new CustomerCreateOrUpdateOrderPurchaseOrderRequest(LocalDate.of(2023, 10, 25), "", LocalDate.of(2023, 10, 25));
    }

    public static CustomerCreateOrUpdateOrderPurchaseOrderRequest nullPaymentDateBuild() {
        return new CustomerCreateOrUpdateOrderPurchaseOrderRequest(LocalDate.of(2023, 10, 25), "신규 검수 조건", null);
    }

    public static CustomerCreateOrUpdateOrderPurchaseOrderRequest earlyInspectionPeriodBuild() {
        return new CustomerCreateOrUpdateOrderPurchaseOrderRequest(LocalDate.of(2023, 10, 12), "신규 검수 조건", LocalDate.of(2023, 10, 25));
    }

    public static CustomerCreateOrUpdateOrderPurchaseOrderRequest earlyPaymentDateBuild() {
        return new CustomerCreateOrUpdateOrderPurchaseOrderRequest(LocalDate.of(2023, 10, 25), "신규 검수 조건", LocalDate.of(2023, 10, 12));
    }
}
