package com.laser.ordermanage.factory.dto.response;

import lombok.Builder;

@Builder
public record FactoryGetPurchaseOrderFileResponse(
        Long id,
        String fileName,
        String fileUrl
){
}
