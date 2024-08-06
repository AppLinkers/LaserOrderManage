package com.laser.ordermanage.user.dto.request;

import com.laser.ordermanage.customer.dto.request.JoinKakaoCustomerRequest;

public class JoinKakaoCustomerRequestBuilder {

    public static JoinKakaoCustomerRequest build() {
        return JoinKakaoCustomerRequest.builder()
                .email("new-user1@gmail.com")
                .name("new-user")
                .companyName("new-company-name")
                .phone("01011111111")
                .zipCode("11111")
                .address("address")
                .build();
    }

    public static JoinKakaoCustomerRequest duplicateEmailBuild() {
        return JoinKakaoCustomerRequest.builder()
                .email("user1@gmail.com")
                .name("new-user")
                .companyName("new-company-name")
                .phone("01011111111")
                .zipCode("11111")
                .address("address")
                .build();
    }
}
