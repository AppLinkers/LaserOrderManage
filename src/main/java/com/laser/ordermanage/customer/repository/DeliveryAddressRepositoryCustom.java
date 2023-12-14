package com.laser.ordermanage.customer.repository;

import com.laser.ordermanage.customer.dto.response.GetDeliveryAddressResponse;

import java.util.List;
import java.util.Optional;

public interface DeliveryAddressRepositoryCustom {
    List<GetDeliveryAddressResponse> findByCustomer(String userName);

    Optional<String> findUserEmailById(Long orderId);
}
