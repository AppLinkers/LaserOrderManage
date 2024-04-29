package com.laser.ordermanage.customer.repository;

import com.laser.ordermanage.customer.domain.DeliveryAddress;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface DeliveryAddressRepository extends CrudRepository<DeliveryAddress, Long>, DeliveryAddressRepositoryCustom {

    Optional<DeliveryAddress> findFirstById(Long deliveryAddressId);

    DeliveryAddress findFirstByCustomerIdAndIsDefaultTrue(Long customerId);

    DeliveryAddress findFirstByCustomer_User_EmailAndIsDefaultTrue(String email);
}
