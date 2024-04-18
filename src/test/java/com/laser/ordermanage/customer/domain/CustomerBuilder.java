package com.laser.ordermanage.customer.domain;

import com.laser.ordermanage.user.domain.UserEntity;
import com.laser.ordermanage.user.domain.UserEntityBuilder;
import org.assertj.core.api.Assertions;

public class CustomerBuilder {
    public static Customer build() {
        UserEntity user = UserEntityBuilder.build();

        return Customer.builder()
                .user(user)
                .companyName("고객 회사 이름 1")
                .build();
    }

    public static Customer nullCompanyNameBuild() {
        UserEntity user = UserEntityBuilder.build();

        return Customer.builder()
                .user(user)
                .companyName(null)
                .build();
    }

    public static void assertCustomer(Customer actualCustomer, Customer expectedCustomer) {
        UserEntityBuilder.assertUserEntity(actualCustomer.getUser(), expectedCustomer.getUser());
        Assertions.assertThat(actualCustomer.getCompanyName()).isEqualTo(expectedCustomer.getCompanyName());
        Assertions.assertThat(actualCustomer.getIsNew()).isEqualTo(expectedCustomer.getIsNew());
    }
}
