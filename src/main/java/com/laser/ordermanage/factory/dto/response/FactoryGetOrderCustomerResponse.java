package com.laser.ordermanage.factory.dto.response;

import com.laser.ordermanage.customer.domain.Customer;
import com.laser.ordermanage.customer.dto.response.GetCustomerResponse;
import lombok.Builder;

public record FactoryGetOrderCustomerResponse(
        Long orderId,
        String orderName,
        GetCustomerResponse customer
) {

    @Builder
    public FactoryGetOrderCustomerResponse(Long orderId, String orderName, Customer customer) {
        this(
                orderId,
                orderName,
                GetCustomerResponse.builder()
                        .id(customer.getId())
                        .name(customer.getName())
                        .company(customer.getCompanyName())
                        .phone(customer.getUser().getPhone())
                        .email(customer.getUser().getEmail())
                        .build()
        );
    }
}
