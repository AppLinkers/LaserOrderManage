package com.laser.ordermanage.customer.repository;

import com.laser.ordermanage.customer.dto.response.CustomerGetDeliveryAddressResponse;

import java.util.List;

public interface DeliveryAddressRepositoryCustom {
    List<CustomerGetDeliveryAddressResponse> findByCustomer(String userName);
}
