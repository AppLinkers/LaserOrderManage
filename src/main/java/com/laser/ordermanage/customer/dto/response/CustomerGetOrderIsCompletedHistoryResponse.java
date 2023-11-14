package com.laser.ordermanage.customer.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class CustomerGetOrderIsCompletedHistoryResponse {

    private final Long id;

    private final String name;

    private final String imgUrl;

    private final LocalDate createdAt;

    @QueryProjection

    public CustomerGetOrderIsCompletedHistoryResponse(Long id, String name, String imgUrl, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.imgUrl = imgUrl;
        this.createdAt = createdAt.toLocalDate();
    }
}
