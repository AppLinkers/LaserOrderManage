package com.laser.ordermanage.customer.repository;

import com.laser.ordermanage.customer.domain.Customer;
import com.laser.ordermanage.customer.domain.DeliveryAddress;
import org.springframework.data.repository.CrudRepository;

public interface DeliveryAddressRepository extends CrudRepository<DeliveryAddress, Long>, DeliveryAddressRepositoryCustom {

    DeliveryAddress findFirstById(Long deliveryId);

    DeliveryAddress findFirstByCustomerAndIsDefaultTrue(Customer customer);
}
