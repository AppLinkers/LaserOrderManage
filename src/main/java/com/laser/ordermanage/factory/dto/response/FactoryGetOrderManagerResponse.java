package com.laser.ordermanage.factory.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class FactoryGetOrderManagerResponse {

    private String name;

    private String phone;

    @QueryProjection
    public FactoryGetOrderManagerResponse(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }
}
