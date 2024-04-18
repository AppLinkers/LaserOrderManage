package com.laser.ordermanage.customer.repository;

import com.laser.ordermanage.customer.dto.response.CustomerGetDeliveryAddressResponse;

import java.util.List;
import java.util.Optional;

public interface DeliveryAddressRepositoryCustom {
    List<CustomerGetDeliveryAddressResponse> findByCustomer(String email);

    Optional<String> findUserEmailById(Long deliveryAddressId);

    void deleteByCustomerId(Long customerId);
}
