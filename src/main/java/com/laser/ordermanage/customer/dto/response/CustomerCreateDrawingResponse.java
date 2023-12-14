package com.laser.ordermanage.customer.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CustomerCreateDrawingResponse {

    private final Long id;

    @Builder
    public CustomerCreateDrawingResponse(Long id) {
        this.id = id;
    }
}
