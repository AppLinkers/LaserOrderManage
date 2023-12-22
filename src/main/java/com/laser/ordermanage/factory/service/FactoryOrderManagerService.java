package com.laser.ordermanage.factory.service;

import com.laser.ordermanage.common.paging.ListResponse;
import com.laser.ordermanage.factory.domain.Factory;
import com.laser.ordermanage.factory.domain.OrderManager;
import com.laser.ordermanage.factory.dto.request.FactoryCreateOrUpdateOrderManagerRequest;
import com.laser.ordermanage.factory.dto.response.FactoryGetOrderManagerResponse;
import com.laser.ordermanage.factory.repository.FactoryRepository;
import com.laser.ordermanage.factory.repository.OrderManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class FactoryOrderManagerService {

    private final FactoryRepository factoryRepository;
    private final OrderManagerRepository orderManagerRepository;

    @Transactional
    public void createOrderManger(String email, FactoryCreateOrUpdateOrderManagerRequest request) {
        Factory factory = factoryRepository.findFirstByUserEmail(email);

        OrderManager orderManager = OrderManager.builder()
                .factory(factory)
                .name(request.getName())
                .phone(request.getPhone())
                .build();

        orderManagerRepository.save(orderManager);
    }

    @Transactional(readOnly = true)
    public ListResponse<FactoryGetOrderManagerResponse> getOrderManager(String email) {
        return new ListResponse<>(orderManagerRepository.findByFactory(email));
    }
}
