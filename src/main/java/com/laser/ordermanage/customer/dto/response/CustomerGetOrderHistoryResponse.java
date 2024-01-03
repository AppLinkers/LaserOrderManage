package com.laser.ordermanage.customer.dto.response;

import com.laser.ordermanage.order.domain.OrderManufacturing;
import com.laser.ordermanage.order.domain.type.Stage;
import com.querydsl.core.annotations.QueryProjection;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record CustomerGetOrderHistoryResponse(
        Long id,
        String name,
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
    public CustomerGetOrderHistoryResponse(Long id, String name, String imgUrl, Stage stage, Boolean isUrgent, OrderManufacturing manufacturing, LocalDateTime createdAt, LocalDate deliveryAt, Long cost, String request) {
        this(
                id,
                name,
                imgUrl,
                stage.getValue(),
                isUrgent,
                manufacturing.toValueList(),
                createdAt.toLocalDate(),
                deliveryAt,
                cost,
                request
        );
    }
}
