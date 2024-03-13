package com.laser.ordermanage.customer.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;

@Builder
public record CustomerGetCustomerAccountResponse(
        String companyName
) {

    @QueryProjection
    public CustomerGetCustomerAccountResponse(String companyName) {
        this.companyName = companyName;
    }
}
