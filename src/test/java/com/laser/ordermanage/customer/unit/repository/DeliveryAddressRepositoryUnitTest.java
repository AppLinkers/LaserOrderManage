package com.laser.ordermanage.customer.unit.repository;

import com.laser.ordermanage.common.RepositoryUnitTest;
import com.laser.ordermanage.customer.domain.DeliveryAddress;
import com.laser.ordermanage.customer.domain.DeliveryAddressBuilder;
import com.laser.ordermanage.customer.dto.response.CustomerGetDeliveryAddressResponse;
import com.laser.ordermanage.customer.dto.response.CustomerGetDeliveryAddressResponseBuilder;
import com.laser.ordermanage.customer.repository.DeliveryAddressRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;
import java.util.Optional;

@EnableJpaRepositories(basePackageClasses = DeliveryAddressRepository.class)
public class DeliveryAddressRepositoryUnitTest extends RepositoryUnitTest {

    @Autowired
    protected DeliveryAddressRepository deliveryAddressRepository;

    @Autowired
    protected JPAQueryFactory queryFactory;

    @Test
    public void findFirstById_존재_O() {
        // given
        final Long expectedDeliveryAddressId = 1L;
        final DeliveryAddress expectedDeliveryAddress = DeliveryAddressBuilder.build();
        expectedDeliveryAddress.asDefault();

        // when
        final Optional<DeliveryAddress> optionalDeliveryAddress = deliveryAddressRepository.findFirstById(expectedDeliveryAddressId);

        // then
        Assertions.assertThat(optionalDeliveryAddress.isPresent()).isTrue();
        optionalDeliveryAddress.ifPresent(
                actualDeliveryAddress -> {
                    Assertions.assertThat(actualDeliveryAddress.getId()).isEqualTo(expectedDeliveryAddressId);
                    DeliveryAddressBuilder.assertDeliveryAddress(actualDeliveryAddress, expectedDeliveryAddress);
                }
        );
    }

    @Test
    public void findFirstById_존재_X() {
        // given
        final Long unknownDeliveryAddressId = 0L;

        // when
        final Optional<DeliveryAddress> optionalDeliveryAddress = deliveryAddressRepository.findFirstById(unknownDeliveryAddressId);

        // then
        Assertions.assertThat(optionalDeliveryAddress.isEmpty()).isTrue();
    }

    @Test
    public void findFirstByCustomerIdAndIsDefaultTrue() {
        // given
        final Long customerId = 1L;
        final Long expectedDeliveryAddressId = 1L;
        final DeliveryAddress expectedDeliveryAddress = DeliveryAddressBuilder.build();

        // when
        final DeliveryAddress actualDeliveryAddress = deliveryAddressRepository.findFirstByCustomerIdAndIsDefaultTrue(customerId);

        // then
        Assertions.assertThat(actualDeliveryAddress.getId()).isEqualTo(expectedDeliveryAddressId);
        DeliveryAddressBuilder.assertDeliveryAddress(actualDeliveryAddress, expectedDeliveryAddress);
    }

    @Test
    public void findFirstByCustomer_User_EmailAndIsDefaultTrue() {
        // given
        final String email = "user1@gmail.com";
        final Long expectedDeliveryAddressId = 1L;
        final DeliveryAddress expectedDeliveryAddress = DeliveryAddressBuilder.build();

        // when
        final DeliveryAddress actualDeliveryAddress = deliveryAddressRepository.findFirstByCustomer_User_EmailAndIsDefaultTrue(email);

        // then
        Assertions.assertThat(actualDeliveryAddress.getId()).isEqualTo(expectedDeliveryAddressId);
        DeliveryAddressBuilder.assertDeliveryAddress(actualDeliveryAddress, expectedDeliveryAddress);
    }

    @Test
    public void findByCustomer() {
        // given
        final String email = "user1@gmail.com";
        final List<CustomerGetDeliveryAddressResponse> expectedResponse = CustomerGetDeliveryAddressResponseBuilder.buildListOfCustomer1();

        // when
        final List<CustomerGetDeliveryAddressResponse> actualResponse = deliveryAddressRepository.findByCustomer(email);

        // then
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    public void findUserEmailById_존재_O() {
        // given
        final Long deliveryAddressId = 1L;
        final String expectedEmail = "user1@gmail.com";

        // when
        Optional<String> optionalEmail = deliveryAddressRepository.findUserEmailById(deliveryAddressId);

        // then
        Assertions.assertThat(optionalEmail.isPresent()).isTrue();
        optionalEmail.ifPresent(
                actualEmail -> Assertions.assertThat(actualEmail).isEqualTo(expectedEmail)
        );
    }

    @Test
    public void findUserEmailById_존재_X() {
        // given
        final Long unknownDeliveryAddressId = 0L;

        // when
        Optional<String> optionalEmail = deliveryAddressRepository.findUserEmailById(unknownDeliveryAddressId);

        // then
        Assertions.assertThat(optionalEmail.isEmpty()).isTrue();
    }

    @Test
    public void deleteByCustomerId() {
        // given
        final Long customerId = 1L;
        final String email = "user1@gmail.com";

        // when
        deliveryAddressRepository.deleteByCustomerId(customerId);

        // then
        final List<CustomerGetDeliveryAddressResponse> actualResponse = deliveryAddressRepository.findByCustomer(email);
        Assertions.assertThat(actualResponse.size()).isEqualTo(0);
    }
}
