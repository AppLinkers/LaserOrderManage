package com.laser.ordermanage.customer.domain;

import com.laser.ordermanage.user.domain.UserEntity;
import com.laser.ordermanage.user.domain.UserEntityBuilder;

public class CustomerBuilder {
    public static Customer build() {
        UserEntity user = UserEntityBuilder.build();

        return Customer.builder()
                .user(user)
                .companyName("고객 회사 이름 1")
                .build();
    }
}
