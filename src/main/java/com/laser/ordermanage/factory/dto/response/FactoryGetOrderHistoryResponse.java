package com.laser.ordermanage.factory.dto.response;

import com.laser.ordermanage.order.domain.OrderManufacturing;
import com.laser.ordermanage.order.domain.type.Stage;
import com.querydsl.core.annotations.QueryProjection;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record FactoryGetOrderHistoryResponse(
        Long id,
        String name,
        String customer,
        String company,
        String imgUrl,
        String stage,
        Boolean isUrgent,
        List<String> manufacturingList,
        LocalDate createdAt,
        LocalDate deliveryAt,
        Long cost,
        String request
) {
    @QueryProjection
    public FactoryGetOrderHistoryResponse(Long id, String name, String customer, String company, String imgUrl, Stage stage, Boolean isUrgent, OrderManufacturing orderManufacturing, LocalDateTime createdAt, LocalDate deliveryAt, Long cost, String request) {
        this(
                id,
                name,
                customer,
                company,
                imgUrl,
                stage.getValue(),
                isUrgent,
                orderManufacturing.toValueList(),
                createdAt.toLocalDate(),
                deliveryAt,
                cost,
                request
        );
    }
}

