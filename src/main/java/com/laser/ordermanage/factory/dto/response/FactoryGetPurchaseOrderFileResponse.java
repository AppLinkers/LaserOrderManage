package com.laser.ordermanage.factory.dto.response;

import com.laser.ordermanage.order.domain.PurchaseOrder;
import lombok.Builder;

@Builder
public record FactoryGetPurchaseOrderFileResponse(
        Long id,
        String fileName,
        String fileUrl
){
    public static FactoryGetPurchaseOrderFileResponse fromEntity(PurchaseOrder purchaseOrder) {
        return FactoryGetPurchaseOrderFileResponse.builder()
                .id(purchaseOrder.getId())
                .fileName(purchaseOrder.getFile().getName())
                .fileUrl(purchaseOrder.getFile().getUrl())
                .build();
    }
}
