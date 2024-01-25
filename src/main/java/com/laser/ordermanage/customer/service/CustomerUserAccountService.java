package com.laser.ordermanage.customer.service;

import com.laser.ordermanage.customer.domain.Customer;
import com.laser.ordermanage.customer.dto.request.CustomerUpdateUserAccountRequest;
import com.laser.ordermanage.customer.dto.response.CustomerGetUserAccountResponse;
import com.laser.ordermanage.customer.repository.CustomerRepository;
import com.laser.ordermanage.user.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CustomerUserAccountService {

    private final CustomerRepository customerRepository;
    private final UserEntityRepository userRepository;

    @Transactional(readOnly = true)
    public CustomerGetUserAccountResponse getUserAccount(String email) {
        return userRepository.findUserAccountByCustomer(email);
    }

    @Transactional
    public void updateUserAccount(String email, CustomerUpdateUserAccountRequest request) {

        Customer customer = customerRepository.findFirstByUserEmail(email);

        customer.updateProperties(request);
    }
}
