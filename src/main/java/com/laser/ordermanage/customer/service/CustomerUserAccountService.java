package com.laser.ordermanage.customer.service;

import com.laser.ordermanage.customer.dto.response.CustomerGetUserAccountResponse;
import com.laser.ordermanage.user.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomerUserAccountService {

    private final UserEntityRepository userRepository;

    public CustomerGetUserAccountResponse getUserAccount(String email) {
        return userRepository.findUserAccountByCustomer(email);
    }
}
