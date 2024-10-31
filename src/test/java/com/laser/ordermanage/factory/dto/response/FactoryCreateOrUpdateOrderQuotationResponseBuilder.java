package com.laser.ordermanage.factory.dto.response;

public class FactoryCreateOrUpdateOrderQuotationResponseBuilder {
    public static FactoryCreateOrUpdateOrderQuotationResponse build() {
        return new FactoryCreateOrUpdateOrderQuotationResponse(null, "quotation.xlsx", "https://ordermanage.s3.ap-northeast-2.amazonaws.com/quotation.xlsx");
    }

    public static FactoryCreateOrUpdateOrderQuotationResponse createBuild() {
        return new FactoryCreateOrUpdateOrderQuotationResponse(13L, "quotation.xlsx", "https://ordermanage.s3.ap-northeast-2.amazonaws.com/quotation.xlsx");
    }

    public static FactoryCreateOrUpdateOrderQuotationResponse updateBuild() {
        return new FactoryCreateOrUpdateOrderQuotationResponse(9L, "quotation.xlsx", "https://ordermanage.s3.ap-northeast-2.amazonaws.com/quotation.xlsx");
    }
}
