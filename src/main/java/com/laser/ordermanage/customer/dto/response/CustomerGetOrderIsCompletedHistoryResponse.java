package com.laser.ordermanage.customer.dto.response;

import com.querydsl.core.annotations.QueryProjection;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CustomerGetOrderIsCompletedHistoryResponse(
        Long id,
        String name,
        String imgUrl,
        LocalDate createdAt
) {
    @QueryProjection
    public CustomerGetOrderIsCompletedHistoryResponse(Long id, String name, String imgUrl, LocalDateTime createdAt) {
        this(
                id,
                name,
                imgUrl,
                createdAt.toLocalDate()
        );
    }
}
