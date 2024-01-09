package com.laser.ordermanage.customer.service;

import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.exception.CommonErrorCode;
import com.laser.ordermanage.common.paging.ListResponse;
import com.laser.ordermanage.customer.domain.Customer;
import com.laser.ordermanage.customer.domain.DeliveryAddress;
import com.laser.ordermanage.customer.dto.request.CustomerCreateOrUpdateDeliveryAddressRequest;
import com.laser.ordermanage.customer.dto.response.CustomerGetDeliveryAddressResponse;
import com.laser.ordermanage.customer.exception.CustomerErrorCode;
import com.laser.ordermanage.customer.repository.CustomerRepository;
import com.laser.ordermanage.customer.repository.DeliveryAddressRepository;
import com.laser.ordermanage.user.exception.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CustomerDeliveryAddressService {

    private final CustomerRepository customerRepository;
    private final DeliveryAddressRepository deliveryAddressRepository;

    private String getUserEmailByDeliveryAddress(Long deliveryAddressId) {
        return deliveryAddressRepository.findUserEmailById(deliveryAddressId).orElseThrow(() -> new CustomCommonException(CommonErrorCode.NOT_FOUND_ENTITY, "deliveryAddress"));
    }

    @Transactional
    public void createDeliveryAddress(String email, CustomerCreateOrUpdateDeliveryAddressRequest request) {
        Customer customer = customerRepository.findFirstByUserEmail(email);

        if (request.isDefault()) {
            DeliveryAddress defaultDeliveryAddress = deliveryAddressRepository.findFirstByCustomerAndIsDefaultTrue(customer);
            defaultDeliveryAddress.disableDefault();
        }

        DeliveryAddress deliveryAddress = DeliveryAddress.builder()
                .customer(customer)
                .name(request.name())
                .zipCode(request.zipCode())
                .address(request.address())
                .detailAddress(request.detailAddress())
                .receiver(request.receiver())
                .phone1(request.phone1())
                .phone2(request.phone2())
                .isDefault(request.isDefault())
                .build();

        deliveryAddressRepository.save(deliveryAddress);
    }

    @Transactional(readOnly = true)
    public ListResponse<CustomerGetDeliveryAddressResponse> getDeliveryAddressList(String email) {
        return new ListResponse<>(deliveryAddressRepository.findByCustomer(email));
    }

    @Transactional(readOnly = true)
    public DeliveryAddress getDeliveryAddress(Long deliverAddressId) {
        return deliveryAddressRepository.findFirstById(deliverAddressId).orElseThrow(() -> new CustomCommonException(CommonErrorCode.NOT_FOUND_ENTITY, "deliveryAddress"));
    }

    @Transactional
    public void updateDeliveryAddress(String email, Long deliveryAddressId, CustomerCreateOrUpdateDeliveryAddressRequest request) {
        DeliveryAddress deliveryAddress = this.getDeliveryAddress(deliveryAddressId);

        if (request.isDefault()) {
            DeliveryAddress defaultDeliveryAddress = deliveryAddressRepository.findFirstByCustomer_User_EmailAndIsDefaultTrue(email);
            defaultDeliveryAddress.disableDefault();
        }

        deliveryAddress.updateProperties(request);
    }

    @Transactional
    public void deleteDeliveryAddress(Long deliveryAddressId) {

        DeliveryAddress deliveryAddress = this.getDeliveryAddress(deliveryAddressId);

        if (deliveryAddress.isDefault()) {
            throw new CustomCommonException(CustomerErrorCode.DEFAULT_DELIVERY_ADDRESS_DELETE);
        }

        deliveryAddressRepository.delete(deliveryAddress);
    }

    @Transactional(readOnly = true)
    public void checkAuthorityCustomerOfDeliveryAddress(User user, Long deliveryAddressId) {
        if (!this.getUserEmailByDeliveryAddress(deliveryAddressId).equals(user.getUsername())) {
            throw new CustomCommonException(UserErrorCode.DENIED_ACCESS_TO_ENTITY, "deliveryAddress");
        }
    }
}
