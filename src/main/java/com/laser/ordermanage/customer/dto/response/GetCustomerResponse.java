package com.laser.ordermanage.customer.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;

@Builder
public record GetCustomerResponse(
        Long id,
        String name,
        String company,
        String phone,
        String email
) {

    @QueryProjection
    public GetCustomerResponse(Long id, String name, String company, String phone, String email) {
        this.id = id;
        this.name = name;
        this.company = company;
        this.phone = phone;
        this.email = email;
    }
}
