package com.laser.ordermanage.factory.dto.response;

import com.laser.ordermanage.order.domain.OrderManufacturing;
import com.querydsl.core.annotations.QueryProjection;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record FactoryGetOrderIsNewAndIsNewIssueHistoryResponse(
        Long id,
        String name,
        String customer,
        String company,
        Boolean isNewCustomer,
        Boolean hasQuotation,
        String imgUrl,
        Boolean isUrgent,
        List<String> manufacturingList,
        LocalDate createdAt,
        LocalDate deliveryAt,
        Long cost,
        String request
) {
    @QueryProjection
    public FactoryGetOrderIsNewAndIsNewIssueHistoryResponse(Long id, String name, String customer, String company, Boolean isNewCustomer, Boolean hasQuotation, String imgUrl, Boolean isUrgent, OrderManufacturing orderManufacturing, LocalDateTime createdAt, LocalDate deliveryAt, Long cost, String request) {
        this(
                id,
                name,
                customer,
                company,
                isNewCustomer,
                hasQuotation,
                imgUrl,
                isUrgent,
                orderManufacturing.toValueList(),
                createdAt.toLocalDate(),
                deliveryAt,
                cost,
                request
        );
    }
}
