package com.laser.ordermanage.factory.repository;

import com.laser.ordermanage.factory.dto.response.FactoryGetOrderManagerResponse;

import java.util.List;

public interface OrderManagerRepositoryCustom {

    List<FactoryGetOrderManagerResponse> findByFactory(String email);
}
