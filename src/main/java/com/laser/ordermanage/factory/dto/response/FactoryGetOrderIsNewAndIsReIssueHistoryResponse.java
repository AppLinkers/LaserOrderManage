package com.laser.ordermanage.factory.dto.response;

import com.laser.ordermanage.order.domain.OrderManufacturing;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class FactoryGetOrderIsNewAndIsReIssueHistoryResponse {

    private final Long id;

    private final String name;

    private final String customer;

    private final String company;

    private final Boolean hasQuotation;

    private final String imgUrl;

    private final Boolean isUrgent;

    private final List<String> manufacturing;

    private final LocalDate createdAt;

    private final LocalDate deliveryAt;

    private final Long cost;

    private final String request;

    @QueryProjection
    public FactoryGetOrderIsNewAndIsReIssueHistoryResponse(Long id, String name, String customer, String company, Boolean hasQuotation, String imgUrl, Boolean isUrgent, OrderManufacturing orderManufacturing, LocalDateTime createdAt, LocalDate deliveryAt, Long cost, String request) {
        this.id = id;
        this.name = name;
        this.customer = customer;
        this.company = company;
        this.hasQuotation = hasQuotation;
        this.imgUrl = imgUrl;
        this.isUrgent = isUrgent;
        this.manufacturing = orderManufacturing.toValueList();
        this.createdAt = createdAt.toLocalDate();
        this.deliveryAt = deliveryAt;
        this.cost = cost;
        this.request = request;
    }
}
