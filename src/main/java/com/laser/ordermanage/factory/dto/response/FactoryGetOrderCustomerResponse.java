package com.laser.ordermanage.factory.dto.response;

import com.laser.ordermanage.customer.domain.Customer;
import com.laser.ordermanage.customer.dto.response.GetCustomerResponse;
import com.laser.ordermanage.order.domain.Order;
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
                        .name(customer.getUser().getName())
                        .company(customer.getCompanyName())
                        .phone(customer.getUser().getPhone())
                        .email(customer.getUser().getEmail())
                        .build()
        );
    }

    public static FactoryGetOrderCustomerResponse fromEntity(Order order, Customer customer) {
        return FactoryGetOrderCustomerResponse.builder()
                .orderId(order.getId())
                .orderName(order.getName())
                .customer(customer)
                .build();
    }
}
