package com.laser.ordermanage.customer.dto.response;

import com.laser.ordermanage.order.domain.OrderManufacturing;
import com.laser.ordermanage.order.domain.type.Stage;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class GetCustomerOrderRes {

    private final Long id;

    private final String name;

    private final String imgUrl;

    private final String stage;

    private final Boolean isUrgent;

    private final List<String> manufacturing;

    private final LocalDate createdAt;

    private final LocalDate deliveryAt;

    private final Long cost;

    private final String request;


    @QueryProjection
    public GetCustomerOrderRes(Long id, String name, String imgUrl, Stage stage, Boolean isUrgent, OrderManufacturing manufacturing, LocalDateTime createdAt, LocalDate deliveryAt, Long cost, String request) {
        this.id = id;
        this.name = name;
        this.imgUrl = imgUrl;
        this.stage = stage.getValue();
        this.isUrgent = isUrgent;
        this.manufacturing = manufacturing.toValueList();
        this.createdAt = createdAt.toLocalDate();
        this.deliveryAt = deliveryAt;
        this.cost = cost;
        this.request = request;
    }
}
