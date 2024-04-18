package com.laser.ordermanage.customer.service;

import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.customer.domain.Customer;
import com.laser.ordermanage.customer.dto.request.CustomerUpdateCustomerAccountRequest;
import com.laser.ordermanage.customer.dto.response.CustomerGetCustomerAccountResponse;
import com.laser.ordermanage.customer.repository.CustomerRepository;
import com.laser.ordermanage.customer.repository.DeliveryAddressRepository;
import com.laser.ordermanage.user.exception.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CustomerUserAccountService {

    private final DeliveryAddressRepository deliveryAddressRepository;
    private final CustomerRepository customerRepository;

    @Transactional(readOnly = true)
    public Customer getCustomerByUserEmail(String email) {
        return customerRepository.findFirstByUserEmail(email).orElseThrow(() -> new CustomCommonException(UserErrorCode.NOT_FOUND_USER));
    }

    @Transactional(readOnly = true)
    public CustomerGetCustomerAccountResponse getCustomerAccount(String email) {
        Customer customer = getCustomerByUserEmail(email);

        return CustomerGetCustomerAccountResponse.builder()
                .companyName(customer.getCompanyName())
                .build();
    }

    @Transactional
    public void updateCustomerAccount(String email, CustomerUpdateCustomerAccountRequest request) {
        Customer customer = getCustomerByUserEmail(email);

        customer.updateProperties(request);
    }

    @Transactional
    public void deleteUser(String email) {
        Customer customer = getCustomerByUserEmail(email);

        // 고객 회원의 배송지 목록 삭제
        deliveryAddressRepository.deleteByCustomer(customer.getId());

        // 고객 회원 데이터 삭제 (고객, 사용자)
        customerRepository.delete(customer);
    }
}
