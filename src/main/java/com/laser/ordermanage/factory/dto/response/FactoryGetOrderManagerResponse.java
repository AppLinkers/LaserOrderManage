package com.laser.ordermanage.factory.dto.response;

import com.querydsl.core.annotations.QueryProjection;

public record FactoryGetOrderManagerResponse(
        Long id,
        String name,
        String phone
) {
    @QueryProjection
    public FactoryGetOrderManagerResponse(Long id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }
}
