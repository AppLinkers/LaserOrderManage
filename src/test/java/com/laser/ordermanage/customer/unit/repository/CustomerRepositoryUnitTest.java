package com.laser.ordermanage.customer.unit.repository;

import com.laser.ordermanage.common.RepositoryUnitTest;
import com.laser.ordermanage.customer.domain.Customer;
import com.laser.ordermanage.customer.domain.CustomerBuilder;
import com.laser.ordermanage.customer.repository.CustomerRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;

@EnableJpaRepositories(basePackageClasses = CustomerRepository.class)
public class CustomerRepositoryUnitTest extends RepositoryUnitTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void findFirstByUserEmail_존재_O() {
        // given
        final Long expectedCustomerId = 1L;
        final Customer expectedCustomer = CustomerBuilder.build();
        expectedCustomer.disableNewCustomer();
        final String userEmail = expectedCustomer.getUser().getEmail();

        // when
        final Optional<Customer> optionalCustomer = customerRepository.findFirstByUserEmail(userEmail);

        // then
        Assertions.assertThat(optionalCustomer.isPresent()).isTrue();
        optionalCustomer.ifPresent(
                actualCustomer -> {
                    Assertions.assertThat(actualCustomer.getId()).isEqualTo(expectedCustomerId);
                    CustomerBuilder.assertCustomer(actualCustomer, expectedCustomer);
                }
        );
    }

    @Test
    public void findFirstByUserEmail_존재_X() {
        // given
        final String unknownUserEmail = "unknown-user@gmail.com";

        // when
        final Optional<Customer> optionalCustomer = customerRepository.findFirstByUserEmail(unknownUserEmail);

        // then
        Assertions.assertThat(optionalCustomer.isEmpty()).isTrue();
    }
}
