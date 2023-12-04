package com.laser.ordermanage.order.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class GetQuotationResponse {

    private final Long id;

    private final String fileUrl;

    private final Long totalCost;

    private final LocalDate deliveryDate;

    private final LocalDateTime createdAt;

    @QueryProjection
    public GetQuotationResponse(Long id, String fileUrl, Long totalCost, LocalDate deliveryDate, LocalDateTime createdAt) {
        this.id = id;
        this.fileUrl = fileUrl;
        this.totalCost = totalCost;
        this.deliveryDate = deliveryDate;
        this.createdAt = createdAt;
    }
}
