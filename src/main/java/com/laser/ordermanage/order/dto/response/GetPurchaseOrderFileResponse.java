package com.laser.ordermanage.order.dto.response;

import lombok.Builder;

@Builder
public record GetPurchaseOrderFileResponse(
        Long id,
        String fileName,
        String fileUrl
){
}
