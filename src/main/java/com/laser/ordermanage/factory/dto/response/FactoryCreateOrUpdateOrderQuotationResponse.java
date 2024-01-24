package com.laser.ordermanage.factory.dto.response;

import com.laser.ordermanage.order.domain.Quotation;

public record FactoryCreateOrUpdateOrderQuotationResponse(
        Long id,
        String fileName,
        String fileUrl
) {
    public static FactoryCreateOrUpdateOrderQuotationResponse from(Quotation quotation) {
        return new FactoryCreateOrUpdateOrderQuotationResponse(
                quotation.getId(),
                quotation.getFile().getName(),
                quotation.getFile().getUrl()
        );
    }
}
