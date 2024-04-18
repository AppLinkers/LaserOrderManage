package com.laser.ordermanage.customer.dto.request;

public class CustomerUpdateCustomerAccountRequestBuilder {

    public static CustomerUpdateCustomerAccountRequest build() {
        return CustomerUpdateCustomerAccountRequest.builder()
                .companyName("고객 회사 수정 이름")
                .build();
    }
}
