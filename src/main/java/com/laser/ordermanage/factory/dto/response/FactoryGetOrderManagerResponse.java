package com.laser.ordermanage.factory.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class FactoryGetOrderManagerResponse {

    private Long id;

    private String name;

    private String phone;

    @QueryProjection
    public FactoryGetOrderManagerResponse(Long id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }
}
