package com.laser.ordermanage.customer.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class GetCustomerResponse {

    private final Long id;

    private final String name;

    private final String company;

    private final String phone;

    private final String email;

    @QueryProjection
    public GetCustomerResponse(Long id, String name, String company, String phone, String email) {
        this.id = id;
        this.name = name;
        this.company = company;
        this.phone = phone;
        this.email = email;
    }
}
