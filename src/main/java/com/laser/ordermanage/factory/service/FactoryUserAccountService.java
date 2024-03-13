package com.laser.ordermanage.factory.service;

import com.laser.ordermanage.factory.domain.Factory;
import com.laser.ordermanage.factory.dto.request.FactoryUpdateFactoryAccountRequest;
import com.laser.ordermanage.factory.dto.response.FactoryGetFactoryAccountResponse;
import com.laser.ordermanage.factory.repository.FactoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class FactoryUserAccountService {

    private final FactoryRepository factoryRepository;

    @Transactional(readOnly = true)
    public FactoryGetFactoryAccountResponse getFactoryAccount(String email) {
        Factory factory = factoryRepository.findFactoryByFactoryManager(email);

        return FactoryGetFactoryAccountResponse.builder()
                .companyName(factory.getCompanyName())
                .representative(factory.getRepresentative())
                .fax(factory.getFax())
                .build();
    }

    @Transactional
    public void updateFactoryAccount(String email, FactoryUpdateFactoryAccountRequest request) {
        Factory factory = factoryRepository.findFactoryByFactoryManager(email);

        factory.updateProperties(request);
    }
}
