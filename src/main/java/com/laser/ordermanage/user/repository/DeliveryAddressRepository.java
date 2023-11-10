package com.laser.ordermanage.user.repository;

import com.laser.ordermanage.user.domain.DeliveryAddress;
import org.springframework.data.repository.CrudRepository;

public interface DeliveryAddressRepository extends CrudRepository<DeliveryAddress, Long> {
}
