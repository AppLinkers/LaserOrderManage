package com.laser.ordermanage.customer.service;

import com.laser.ordermanage.common.entity.embedded.Address;
import com.laser.ordermanage.customer.domain.Customer;
import com.laser.ordermanage.customer.domain.DeliveryAddress;
import com.laser.ordermanage.customer.dto.request.CustomerUpdateUserAccountRequest;
import com.laser.ordermanage.customer.dto.request.JoinCustomerRequest;
import com.laser.ordermanage.customer.dto.response.CustomerGetUserAccountResponse;
import com.laser.ordermanage.customer.repository.CustomerRepository;
import com.laser.ordermanage.customer.repository.DeliveryAddressRepository;
import com.laser.ordermanage.user.domain.UserEntity;
import com.laser.ordermanage.user.domain.type.Role;
import com.laser.ordermanage.user.dto.response.UserJoinStatusResponse;
import com.laser.ordermanage.user.dto.type.JoinStatus;
import com.laser.ordermanage.user.repository.UserEntityRepository;
import com.laser.ordermanage.user.service.UserJoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CustomerUserAccountService {

    private final PasswordEncoder passwordEncoder;

    private final UserJoinService userJoinService;

    private final DeliveryAddressRepository deliveryAddressRepository;
    private final CustomerRepository customerRepository;
    private final UserEntityRepository userRepository;

    @Transactional
    public UserJoinStatusResponse join(JoinCustomerRequest request) {
        UserJoinStatusResponse response = userJoinService.checkDuplicatedEmail(request.email());

        if (JoinStatus.isPossible(response.status())) {
            Address address = Address.builder()
                    .zipCode(request.zipCode())
                    .address(request.address())
                    .detailAddress(request.detailAddress())
                    .build();

            UserEntity user = UserEntity.builder()
                    .email(request.email())
                    .password(passwordEncoder.encode(request.password()))
                    .role(Role.ROLE_CUSTOMER)
                    .phone(request.phone())
                    .address(address)
                    .build();

            Customer customer = Customer.builder()
                    .user(user)
                    .name(request.name())
                    .companyName(request.companyName())
                    .build();

            DeliveryAddress deliveryAddress = DeliveryAddress.builder()
                    .customer(customer)
                    .name(customer.getName())
                    .address(user.getAddress())
                    .receiver(customer.getName())
                    .phone1(user.getPhone())
                    .phone2(user.getPhone())
                    .isDefault(Boolean.TRUE)
                    .build();

            deliveryAddressRepository.save(deliveryAddress);

            response = UserJoinStatusResponse.builderWithUserEntity()
                    .userEntity(user)
                    .status(JoinStatus.COMPLETED)
                    .buildWithUserEntity();
        }

        return response;
    }

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
