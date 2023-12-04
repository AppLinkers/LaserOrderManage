package com.laser.ordermanage.order.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class GetPurchaseOrderResponse {

    private final Long id;

    private final LocalDate inspectionPeriod;

    private final String inspectionCondition;

    private final LocalDate paymentDate;

    private final LocalDateTime createdAt;

    @QueryProjection
    public GetPurchaseOrderResponse(Long id, LocalDate inspectionPeriod, String inspectionCondition, LocalDate paymentDate, LocalDateTime createdAt) {
        this.id = id;
        this.inspectionPeriod = inspectionPeriod;
        this.inspectionCondition = inspectionCondition;
        this.paymentDate = paymentDate;
        this.createdAt = createdAt;
    }
}
