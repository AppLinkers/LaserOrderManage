package com.laser.ordermanage.order.dto.response;

import com.querydsl.core.annotations.QueryProjection;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record GetQuotationResponse(
        Long id,
        String fileName,
        String fileUrl,
        Long totalCost,
        LocalDate deliveryDate,
        LocalDateTime createdAt
) {
    @QueryProjection
    public GetQuotationResponse(Long id, String fileName, String fileUrl, Long totalCost, LocalDate deliveryDate, LocalDateTime createdAt) {
        this.id = id;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.totalCost = totalCost;
        this.deliveryDate = deliveryDate;
        this.createdAt = createdAt;
    }
}
