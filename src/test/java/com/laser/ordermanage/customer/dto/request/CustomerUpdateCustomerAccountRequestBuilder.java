package com.laser.ordermanage.customer.dto.request;

public class CustomerUpdateCustomerAccountRequestBuilder {

    public static CustomerUpdateCustomerAccountRequest build() {
        return new CustomerUpdateCustomerAccountRequest("고객 회사 수정 이름");
    }

    public static CustomerUpdateCustomerAccountRequest invalidContentBuild() {
        return new CustomerUpdateCustomerAccountRequest("고객 회사 수정 이름".repeat(2));
    }
}
