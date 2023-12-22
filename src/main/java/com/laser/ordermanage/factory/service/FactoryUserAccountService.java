package com.laser.ordermanage.factory.service;

import com.laser.ordermanage.factory.dto.response.FactoryGetUserAccountResponse;
import com.laser.ordermanage.user.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FactoryUserAccountService {

    private final UserEntityRepository userRepository;

    public FactoryGetUserAccountResponse getUserAccount(String email) {
        return userRepository.findUserAccountByFactory(email);
    }
}
