package com.laser.ordermanage.customer.service;

import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.paging.ListResponse;
import com.laser.ordermanage.customer.domain.Customer;
import com.laser.ordermanage.customer.domain.DeliveryAddress;
import com.laser.ordermanage.customer.dto.request.CustomerCreateOrUpdateDeliveryAddressRequest;
import com.laser.ordermanage.customer.dto.response.CustomerGetDeliveryAddressResponse;
import com.laser.ordermanage.customer.exception.CustomerErrorCode;
import com.laser.ordermanage.customer.repository.DeliveryAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CustomerDeliveryAddressService {

    private final CustomerUserAccountService customerUserAccountService;
    private final DeliveryAddressRepository deliveryAddressRepository;

    private String getUserEmailByDeliveryAddress(Long deliveryAddressId) {
        return deliveryAddressRepository.findUserEmailById(deliveryAddressId).orElseThrow(() -> new CustomCommonException(CustomerErrorCode.NOT_FOUND_DELIVERY_ADDRESS));
    }

    @Transactional
    public void createDeliveryAddress(String email, CustomerCreateOrUpdateDeliveryAddressRequest request) {
        Customer customer = customerUserAccountService.getCustomerByUserEmail(email);

        if (request.isDefault()) {
            DeliveryAddress defaultDeliveryAddress = deliveryAddressRepository.findFirstByCustomerIdAndIsDefaultTrue(customer.getId());
            defaultDeliveryAddress.disableDefault();
        }

        DeliveryAddress deliveryAddress = request.toEntity(customer);

        deliveryAddressRepository.save(deliveryAddress);
    }

    @Transactional(readOnly = true)
    public ListResponse<CustomerGetDeliveryAddressResponse> getDeliveryAddressList(String email) {
        return new ListResponse<>(deliveryAddressRepository.findByCustomer(email));
    }

    @Transactional(readOnly = true)
    public DeliveryAddress getDeliveryAddress(Long deliverAddressId) {
        return deliveryAddressRepository.findFirstById(deliverAddressId).orElseThrow(() -> new CustomCommonException(CustomerErrorCode.NOT_FOUND_DELIVERY_ADDRESS));
    }

    @Transactional
    public void updateDeliveryAddress(String email, Long deliveryAddressId, CustomerCreateOrUpdateDeliveryAddressRequest request) {
        DeliveryAddress deliveryAddress = this.getDeliveryAddress(deliveryAddressId);

        if (deliveryAddress.isDefault() && !request.isDefault()) {
            throw new CustomCommonException(CustomerErrorCode.UNABLE_DEFAULT_DELIVERY_ADDRESS_DISABLE);
        }

        if (!deliveryAddress.isDefault() && request.isDefault()) {
            DeliveryAddress defaultDeliveryAddress = deliveryAddressRepository.findFirstByCustomer_User_EmailAndIsDefaultTrue(email);
            defaultDeliveryAddress.disableDefault();
            deliveryAddress.asDefault();
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
    public void checkAuthorityCustomerOfDeliveryAddress(String email, Long deliveryAddressId) {
        if (!this.getUserEmailByDeliveryAddress(deliveryAddressId).equals(email)) {
            throw new CustomCommonException(CustomerErrorCode.DENIED_ACCESS_TO_DELIVERY_ADDRESS);
        }
    }
}
