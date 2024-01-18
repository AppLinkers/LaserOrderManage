package com.laser.ordermanage.factory.service;

import com.laser.ordermanage.factory.domain.Factory;
import com.laser.ordermanage.factory.dto.request.FactoryUpdateUserAccountRequest;
import com.laser.ordermanage.factory.dto.response.FactoryGetUserAccountResponse;
import com.laser.ordermanage.factory.repository.FactoryRepository;
import com.laser.ordermanage.user.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class FactoryUserAccountService {

    private final FactoryRepository factoryRepository;
    private final UserEntityRepository userRepository;

    @Transactional(readOnly = true)
    public FactoryGetUserAccountResponse getUserAccount(String email) {
        return userRepository.findUserAccountByFactory(email);
    }

    @Transactional
    public void updateUserAccount(String email, FactoryUpdateUserAccountRequest request) {
        Factory factory = factoryRepository.findFirstByUserEmail(email);

        factory.updateProperties(request);
    }
}
