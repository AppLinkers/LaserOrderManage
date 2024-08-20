package com.laser.ordermanage.factory.service;

import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.factory.domain.Factory;
import com.laser.ordermanage.factory.dto.request.FactoryUpdateFactoryAccountRequest;
import com.laser.ordermanage.factory.dto.response.FactoryGetFactoryAccountResponse;
import com.laser.ordermanage.factory.exception.FactoryErrorCode;
import com.laser.ordermanage.factory.repository.FactoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class FactoryUserAccountService {

    private final FactoryRepository factoryRepository;

    @Transactional(readOnly = true)
    public Factory getFactoryByFactoryManagerUserEmail(String email) {
        return factoryRepository.findFactoryByFactoryManagerUserEmail(email).orElseThrow(() -> new CustomCommonException(FactoryErrorCode.NOT_FOUND_FACTORY));
    }

    @Transactional(readOnly = true)
    public FactoryGetFactoryAccountResponse getFactoryAccount(String email) {
        Factory factory = getFactoryByFactoryManagerUserEmail(email);

        return FactoryGetFactoryAccountResponse.builder()
                .companyName(factory.getCompanyName())
                .representative(factory.getRepresentative())
                .fax(factory.getFax())
                .build();
    }

    @Transactional
    public void updateFactoryAccount(String email, FactoryUpdateFactoryAccountRequest request) {
        Factory factory = getFactoryByFactoryManagerUserEmail(email);

        factory.updateProperties(request);
    }
}
