package com.laser.ordermanage.customer.unit.domain;

import com.laser.ordermanage.customer.domain.Customer;
import com.laser.ordermanage.customer.domain.CustomerBuilder;
import com.laser.ordermanage.customer.dto.request.CustomerUpdateCustomerAccountRequest;
import com.laser.ordermanage.customer.dto.request.CustomerUpdateCustomerAccountRequestBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class CustomerUnitTest {

    @Test
    public void updateProperties() {
        // given
        final Customer customer = CustomerBuilder.build();
        final CustomerUpdateCustomerAccountRequest request = CustomerUpdateCustomerAccountRequestBuilder.build();

        // when
        customer.updateProperties(request);

        // then
        Assertions.assertThat(customer.getCompanyName()).isEqualTo(request.companyName());
    }

    @Test
    public void isNewCustomer_True() {
        // given
        final Customer customer = CustomerBuilder.build();

        // when & then
        Assertions.assertThat(customer.isNewCustomer()).isTrue();
    }

    @Test
    public void isNewCustomer_False() {
        // given
        final Customer customer = CustomerBuilder.build();
        customer.disableNewCustomer();

        // when & then
        Assertions.assertThat(customer.isNewCustomer()).isFalse();
    }

    @Test
    public void disableNewCustomer() {
        // given
        final Customer customer = CustomerBuilder.build();
        Assertions.assertThat(customer.getIsNew()).isTrue();

        // when
        customer.disableNewCustomer();

        // then
        Assertions.assertThat(customer.getIsNew()).isFalse();
    }

    @Test
    public void hasCompanyName_True() {
        // given
        final Customer customer = CustomerBuilder.build();

        // when & then
        Assertions.assertThat(customer.hasCompanyName()).isTrue();
    }

    @Test
    public void hasCompanyName_False() {
        // given
        final Customer customer = CustomerBuilder.nullCompanyNameBuild();

        // when & then
        Assertions.assertThat(customer.hasCompanyName()).isFalse();
    }
}
