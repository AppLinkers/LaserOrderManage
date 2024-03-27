package com.laser.ordermanage.factory.dto.request;

import java.time.LocalDate;

public class FactoryCreateOrUpdateOrderQuotationRequestBuilder {
    public static FactoryCreateOrUpdateOrderQuotationRequest build() {
        return new FactoryCreateOrUpdateOrderQuotationRequest(50000000L, LocalDate.of(2023, 10, 25));
    }
}
