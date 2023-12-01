package com.laser.ordermanage.customer.repository;

import com.laser.ordermanage.customer.dto.response.GetDeliveryAddressResponse;

import java.util.List;

public interface DeliveryAddressRepositoryCustom {
    List<GetDeliveryAddressResponse> findByCustomer(String userName);
}
