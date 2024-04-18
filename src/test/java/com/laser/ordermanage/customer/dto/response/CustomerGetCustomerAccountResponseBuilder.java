package com.laser.ordermanage.customer.dto.response;

import com.laser.ordermanage.customer.domain.Customer;
import com.laser.ordermanage.customer.domain.CustomerBuilder;

public class CustomerGetCustomerAccountResponseBuilder {
    public static CustomerGetCustomerAccountResponse build() {
        Customer customer = CustomerBuilder.build();

        return CustomerGetCustomerAccountResponse.builder()
                .companyName(customer.getCompanyName())
                .build();
    }
}
