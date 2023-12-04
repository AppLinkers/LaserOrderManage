package com.laser.ordermanage.customer.service;

import com.laser.ordermanage.common.paging.ListResponse;
import com.laser.ordermanage.customer.domain.Customer;
import com.laser.ordermanage.customer.domain.DeliveryAddress;
import com.laser.ordermanage.customer.dto.request.CreateCustomerDeliveryAddressRequest;
import com.laser.ordermanage.customer.dto.response.GetDeliveryAddressResponse;
import com.laser.ordermanage.customer.repository.CustomerRepository;
import com.laser.ordermanage.customer.repository.DeliveryAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CustomerDeliveryAddressService {

    private final CustomerRepository customerRepository;
    private final DeliveryAddressRepository deliveryAddressRepository;

    @Transactional
    public void createDeliveryAddress(String userName, CreateCustomerDeliveryAddressRequest request) {
        Customer customer = customerRepository.findFirstByUserEmail(userName);

        DeliveryAddress defaultDeliveryAddress = deliveryAddressRepository.findFirstByCustomerAndIsDefaultTrue(customer);
        if (request.getIsDefault()) {
            defaultDeliveryAddress.disableDefault();
        }

        DeliveryAddress deliveryAddress = DeliveryAddress.builder()
                .customer(customer)
                .name(request.getDeliveryName())
                .zipCode(request.getZipCode())
                .address(request.getAddress())
                .detailAddress(request.getDetailAddress())
                .receiver(request.getReceiver())
                .phone1(request.getPhone1())
                .phone2(request.getPhone2())
                .isDefault(request.getIsDefault())
                .build();

        deliveryAddressRepository.save(deliveryAddress);
    }

    @Transactional(readOnly = true)
    public ListResponse<GetDeliveryAddressResponse> getDeliveryAddress(String userName) {
        return new ListResponse<>(deliveryAddressRepository.findByCustomer(userName));
    }
}
