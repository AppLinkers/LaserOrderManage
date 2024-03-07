package com.laser.ordermanage.user.dto.request;

import com.laser.ordermanage.customer.dto.request.JoinCustomerRequest;

public class JoinCustomerRequestBuilder {

    public static JoinCustomerRequest build() {
        return JoinCustomerRequest.builder()
                .email("new-user1@gmail.com")
                .password("new-user1-password")
                .name("new-user")
                .companyName("new-company-name")
                .phone("01011111111")
                .zipCode("11111")
                .address("addresss")
                .build();
    }

    public static JoinCustomerRequest duplicateEmailBuild() {
        return JoinCustomerRequest.builder()
                .email("user1@gmail.com")
                .password("user1-password")
                .name("new-user")
                .companyName("company-name")
                .phone("01011111111")
                .zipCode("11111")
                .address("addresss")
                .build();
    }
}
