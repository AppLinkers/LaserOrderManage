package com.laser.ordermanage.factory.dto.response;

import com.laser.ordermanage.order.domain.OrderManufacturing;
import com.laser.ordermanage.order.domain.type.Stage;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class FactoryGetOrderHistoryResponse {

    private final Long id;

    private final String name;

    private final String customer;

    private final String company;

    private final String imgUrl;

    private final String stage;

    private final Boolean isUrgent;

    private final List<String> manufacturing;

    private final LocalDate createdAt;

    private final LocalDate deliveryAt;

    private final Long cost;

    private final String request;

    @QueryProjection
    public FactoryGetOrderHistoryResponse(Long id, String name, String customer, String company, String imgUrl, Stage stage, Boolean isUrgent, OrderManufacturing orderManufacturing, LocalDateTime createdAt, LocalDate deliveryAt, Long cost, String request) {
        this.id = id;
        this.name = name;
        this.customer = customer;
        this.company = company;
        this.imgUrl = imgUrl;
        this.stage = stage.getValue();
        this.isUrgent = isUrgent;
        this.manufacturing = orderManufacturing.toValueList();
        this.createdAt = createdAt.toLocalDate();
        this.deliveryAt = deliveryAt;
        this.cost = cost;
        this.request = request;
    }
}
