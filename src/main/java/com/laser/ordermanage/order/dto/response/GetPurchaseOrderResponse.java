package com.laser.ordermanage.order.dto.response;

import com.querydsl.core.annotations.QueryProjection;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record GetPurchaseOrderResponse(
        Long id,
        String fileName,
        String fileUrl,
        LocalDate inspectionPeriod,
        String inspectionCondition,
        LocalDate paymentDate,
        LocalDateTime createdAt
) {
    @QueryProjection
    public GetPurchaseOrderResponse(Long id, String fileName, String fileUrl, LocalDate inspectionPeriod, String inspectionCondition, LocalDate paymentDate, LocalDateTime createdAt) {
        this.id = id;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.inspectionPeriod = inspectionPeriod;
        this.inspectionCondition = inspectionCondition;
        this.paymentDate = paymentDate;
        this.createdAt = createdAt;
    }
}
