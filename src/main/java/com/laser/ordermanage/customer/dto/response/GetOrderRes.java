package com.laser.ordermanage.customer.dto.response;

import com.laser.ordermanage.order.domain.OrderManufacturing;
import com.laser.ordermanage.order.domain.type.Stage;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class GetOrderRes {

    private Long id;

    private String name;

    private String imgUrl;

    private String stage;

    private Boolean isUrgent;

    private List<String> manufacturing;

    private LocalDate createdAt;

    private LocalDate deliveryAt;

    private Long cost;

    private String request;


    @QueryProjection
    public GetOrderRes(Long id, String name, String imgUrl, Stage stage, Boolean isUrgent, OrderManufacturing manufacturing, LocalDateTime createdAt, LocalDate deliveryAt, Long cost, String request) {
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
