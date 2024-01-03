package com.laser.ordermanage.factory.dto.response;

import lombok.Builder;

@Builder
public record FactoryCreateOrUpdateOrderQuotationResponse(
        Long id,
        String fileName,
        String fileUrl
) {
}
