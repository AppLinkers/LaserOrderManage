package com.laser.ordermanage.factory.dto.request;

import java.time.LocalDate;

public class FactoryCreateOrUpdateOrderQuotationRequestBuilder {
    public static FactoryCreateOrUpdateOrderQuotationRequest build() {
        return new FactoryCreateOrUpdateOrderQuotationRequest(50000000L, LocalDate.of(2023, 10, 27));
    }

    public static FactoryCreateOrUpdateOrderQuotationRequest nullTotalCostBuild() {
        return new FactoryCreateOrUpdateOrderQuotationRequest(null, LocalDate.of(2023, 10, 27));
    }

    public static FactoryCreateOrUpdateOrderQuotationRequest nullDeliveryDate() {
        return new FactoryCreateOrUpdateOrderQuotationRequest(50000000L, null);
    }

    public static FactoryCreateOrUpdateOrderQuotationRequest earlyDeliveryDateBuild() {
        return new FactoryCreateOrUpdateOrderQuotationRequest(50000000L, LocalDate.of(2023, 10, 11));
    }
}
