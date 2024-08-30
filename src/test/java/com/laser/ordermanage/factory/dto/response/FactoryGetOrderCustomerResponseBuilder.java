package com.laser.ordermanage.factory.dto.response;

import com.laser.ordermanage.customer.domain.Customer;
import com.laser.ordermanage.customer.dto.response.GetCustomerResponse;
import com.laser.ordermanage.order.domain.Order;
import com.laser.ordermanage.order.domain.OrderBuilder;

public class FactoryGetOrderCustomerResponseBuilder {
    public static FactoryGetOrderCustomerResponse build() {
        Order order = OrderBuilder.build();
        Customer customer = order.getCustomer();

        GetCustomerResponse getCustomerResponse = GetCustomerResponse.builder()
                .id(1L)
                .name(customer.getUser().getName())
                .company(customer.getCompanyName())
                .phone(customer.getUser().getPhone())
                .email(customer.getUser().getEmail())
                .build();

        return new FactoryGetOrderCustomerResponse(1L, order.getName(), getCustomerResponse);
    }
}
