package com.laser.ordermanage.factory.dto.response;

public class FactoryCreateOrUpdateOrderQuotationResponseBuilder {
    public static FactoryCreateOrUpdateOrderQuotationResponse build() {
        return new FactoryCreateOrUpdateOrderQuotationResponse(null, "quotation.xlsx", "quotation-url.xlsx");
    }

    public static FactoryCreateOrUpdateOrderQuotationResponse createBuild() {
        return new FactoryCreateOrUpdateOrderQuotationResponse(13L, "quotation.xlsx", "quotation-url.xlsx");
    }

    public static FactoryCreateOrUpdateOrderQuotationResponse updateBuild() {
        return new FactoryCreateOrUpdateOrderQuotationResponse(9L, "quotation.xlsx", "quotation-url.xlsx");
    }
}
