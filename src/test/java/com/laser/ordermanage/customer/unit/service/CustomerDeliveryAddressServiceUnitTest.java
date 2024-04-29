package com.laser.ordermanage.customer.unit.service;

import com.laser.ordermanage.common.ServiceUnitTest;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.paging.ListResponse;
import com.laser.ordermanage.customer.domain.Customer;
import com.laser.ordermanage.customer.domain.CustomerBuilder;
import com.laser.ordermanage.customer.domain.DeliveryAddress;
import com.laser.ordermanage.customer.domain.DeliveryAddressBuilder;
import com.laser.ordermanage.customer.dto.request.CustomerCreateOrUpdateDeliveryAddressRequest;
import com.laser.ordermanage.customer.dto.request.CustomerCreateOrUpdateDeliveryAddressRequestBuilder;
import com.laser.ordermanage.customer.dto.response.CustomerGetDeliveryAddressResponse;
import com.laser.ordermanage.customer.dto.response.CustomerGetDeliveryAddressResponseBuilder;
import com.laser.ordermanage.customer.exception.CustomerErrorCode;
import com.laser.ordermanage.customer.repository.DeliveryAddressRepository;
import com.laser.ordermanage.customer.service.CustomerDeliveryAddressService;
import com.laser.ordermanage.customer.service.CustomerUserAccountService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class CustomerDeliveryAddressServiceUnitTest extends ServiceUnitTest {

    @InjectMocks
    private CustomerDeliveryAddressService customerDeliveryAddressService;

    @Mock
    private CustomerUserAccountService customerUserAccountService;

    @Mock
    private DeliveryAddressRepository deliveryAddressRepository;

    /**
     * 배송지 생성 성공
     */
    @Test
    public void createDeliveryAddress_성공() {
        // given
        final Customer customer = CustomerBuilder.build();
        final String email = "user@gmail.com";
        final DeliveryAddress defaultDeliveryAddress = DeliveryAddressBuilder.build();
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.createBuild();

        // stub
        when(customerUserAccountService.getCustomerByUserEmail(email)).thenReturn(customer);
        when(deliveryAddressRepository.findFirstByCustomerAndIsDefaultTrue(customer)).thenReturn(defaultDeliveryAddress);

        // when
        customerDeliveryAddressService.createDeliveryAddress(email, request);

        // then
        Assertions.assertThat(defaultDeliveryAddress.getIsDefault()).isFalse();
        verify(deliveryAddressRepository, times(1)).findFirstByCustomerAndIsDefaultTrue(any());
        verify(deliveryAddressRepository, times(1)).save(any());
    }

    /**
     * 고객 사용자 이메일을 활용한 배송지 정보 목록 조회 성공
     */
    @Test
    public void getDeliveryAddressList_성공() {
        // given
        final String email = "user@gmail.com";
        final ListResponse<CustomerGetDeliveryAddressResponse> expectedResponse = new ListResponse<>(CustomerGetDeliveryAddressResponseBuilder.buildListOfCustomer1());

        // stub
        when(deliveryAddressRepository.findByCustomer(email)).thenReturn(expectedResponse.contents());

        // when
        final ListResponse<CustomerGetDeliveryAddressResponse> actualResponse = customerDeliveryAddressService.getDeliveryAddressList(email);

        // then
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 배송지 DB id 를 활용한 배송지 조회 성공
     */
    @Test
    public void getDeliveryAddress_성공() {
        // given
        final Long deliveryAddressId = 1L;
        final DeliveryAddress expectedDeliveryAddress = DeliveryAddressBuilder.build();

        // stub
        when(deliveryAddressRepository.findFirstById(deliveryAddressId)).thenReturn(Optional.of(expectedDeliveryAddress));

        // when
        final DeliveryAddress actualDeliveryAddress = customerDeliveryAddressService.getDeliveryAddress(deliveryAddressId);

        // then
        Assertions.assertThat(actualDeliveryAddress).isEqualTo(expectedDeliveryAddress);
    }

    /**
     * 배송지 DB id 를 활용한 배송지 조회 실패
     * - 실패 사유 : 존재하지 않는 배송지
     */
    @Test
    public void getDeliveryAddress_실패_NOT_FOUND_DELIVERY_ADDRESS() {
        // given
        final Long unknownDeliveryAddressId = 0L;

        // stub
        when(deliveryAddressRepository.findFirstById(unknownDeliveryAddressId)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> customerDeliveryAddressService.getDeliveryAddress(unknownDeliveryAddressId))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(CustomerErrorCode.NOT_FOUND_DELIVERY_ADDRESS.getMessage());
    }

    /**
     * 배송지 수정 성공
     */
    @Test
    public void updateDeliveryAddress_성공() {
        // given
        final String email = "user@gmail.com";
        final Long deliveryAddressId = 1L;
        final DeliveryAddress deliveryAddress = DeliveryAddressBuilder.build();
        deliveryAddress.asDefault();
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.updateBuild();

        // stub
        when(deliveryAddressRepository.findFirstById(deliveryAddressId)).thenReturn(Optional.of(deliveryAddress));

        // when
        customerDeliveryAddressService.updateDeliveryAddress(email, deliveryAddressId, request);

        // then
        verify(deliveryAddressRepository, times(0)).findFirstByCustomer_User_EmailAndIsDefaultTrue(any());
        Assertions.assertThat(deliveryAddress.getName()).isEqualTo(request.name());
        Assertions.assertThat(deliveryAddress.getAddress().getZipCode()).isEqualTo(request.zipCode());
        Assertions.assertThat(deliveryAddress.getAddress().getAddress()).isEqualTo(request.address());
        Assertions.assertThat(deliveryAddress.getAddress().getDetailAddress()).isEqualTo(request.detailAddress());
        Assertions.assertThat(deliveryAddress.getReceiver()).isEqualTo(request.receiver());
        Assertions.assertThat(deliveryAddress.getPhone1()).isEqualTo(request.phone1());
        Assertions.assertThat(deliveryAddress.getPhone2()).isEqualTo(request.phone2());
        Assertions.assertThat(deliveryAddress.getIsDefault()).isEqualTo(request.isDefault());
    }

    /**
     * 배송지 수정 성공
     * - 기본 배송지 설정
     */
    @Test
    public void updateDeliveryAddress_성공_기본배송지_설정() {
        // given
        final String email = "user@gmail.com";
        final Long deliveryAddressId = 1L;
        final DeliveryAddress deliveryAddress = DeliveryAddressBuilder.build();
        deliveryAddress.disableDefault();
        final DeliveryAddress defaultDeliveryAddress = DeliveryAddressBuilder.build2();
        defaultDeliveryAddress.asDefault();
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.updateBuild();

        // stub
        when(deliveryAddressRepository.findFirstById(deliveryAddressId)).thenReturn(Optional.of(deliveryAddress));
        when(deliveryAddressRepository.findFirstByCustomer_User_EmailAndIsDefaultTrue(email)).thenReturn(defaultDeliveryAddress);

        // when
        customerDeliveryAddressService.updateDeliveryAddress(email, deliveryAddressId, request);

        // then
        verify(deliveryAddressRepository, times(1)).findFirstByCustomer_User_EmailAndIsDefaultTrue(any());
        Assertions.assertThat(deliveryAddress.isDefault()).isTrue();
        Assertions.assertThat(defaultDeliveryAddress.isDefault()).isFalse();
    }

    /**
     * 배송지 수정 실패
     * - 기본 배송지 설정 해제
     */
    @Test
    public void updateDeliveryAddress_실패_UNABLE_DEFAULT_DELIVERY_ADDRESS_DISABLE() {
        // given
        final String email = "user@gmail.com";
        final Long defaultDeliveryAddressId = 1L;
        final DeliveryAddress defaultDeliveryAddress = DeliveryAddressBuilder.build();
        defaultDeliveryAddress.asDefault();
        final CustomerCreateOrUpdateDeliveryAddressRequest request = CustomerCreateOrUpdateDeliveryAddressRequestBuilder.defaultDeliveryAddressDisableUpdateBuild();

        // stub
        when(deliveryAddressRepository.findFirstById(defaultDeliveryAddressId)).thenReturn(Optional.of(defaultDeliveryAddress));

        // when & then
        Assertions.assertThatThrownBy(() -> customerDeliveryAddressService.updateDeliveryAddress(email, defaultDeliveryAddressId, request))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(CustomerErrorCode.UNABLE_DEFAULT_DELIVERY_ADDRESS_DISABLE.getMessage());
    }

    /**
     * 배송지 삭제 성공
     */
    @Test
    public void deleteDeliveryAddress_성공() {
        // given
        final Long deliveryAddressId = 1L;
        final DeliveryAddress deliveryAddress = DeliveryAddressBuilder.build();
        deliveryAddress.disableDefault();

        // stub
        when(deliveryAddressRepository.findFirstById(deliveryAddressId)).thenReturn(Optional.of(deliveryAddress));

        // when
        customerDeliveryAddressService.deleteDeliveryAddress(deliveryAddressId);

        // then
        verify(deliveryAddressRepository, times(1)).delete(any());
    }

    /**
     * 배송지 삭제 실패
     * - 실패 사유 : 기본 배송지 삭제
     */
    @Test
    public void deleteDeliveryAddress_실패_DEFAULT_DELIVERY_ADDRESS_DELETE() {
        // given
        final Long defaultDeliveryAddressId = 1L;
        final DeliveryAddress defaultDeliveryAddress = DeliveryAddressBuilder.build();
        defaultDeliveryAddress.asDefault();

        // stub
        when(deliveryAddressRepository.findFirstById(defaultDeliveryAddressId)).thenReturn(Optional.of(defaultDeliveryAddress));

        // when & then
        Assertions.assertThatThrownBy(() -> customerDeliveryAddressService.deleteDeliveryAddress(defaultDeliveryAddressId))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(CustomerErrorCode.DEFAULT_DELIVERY_ADDRESS_DELETE.getMessage());
    }

    /**
     * 배송지 DB id 에 해당하는 배송지의 접근 권한 확인 성공
     */
    @Test
    public void checkAuthorityCustomerOfDeliveryAddress_성공() {
        // given
        final String email = "user@gmail.com";
        final Long deliveryAddressId = 1L;

        // stub
        when(deliveryAddressRepository.findUserEmailById(deliveryAddressId)).thenReturn(Optional.of(email));

        // when
        customerDeliveryAddressService.checkAuthorityCustomerOfDeliveryAddress(email, deliveryAddressId);
    }

    /**
     * 배송지 DB id 에 해당하는 배송지의 접근 권한 확인 실패
     * - 실패 사유 : 존재하지 않는 배송지
     */
    @Test
    public void checkAuthorityCustomerOfDeliveryAddress_실패_NOT_FOUND_DELIVERY_ADDRESS() {
        // given
        final String email = "user@gmail.com";
        final Long unknownDeliveryAddressId = 0L;

        // stub
        when(deliveryAddressRepository.findUserEmailById(unknownDeliveryAddressId)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> customerDeliveryAddressService.checkAuthorityCustomerOfDeliveryAddress(email, unknownDeliveryAddressId))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(CustomerErrorCode.NOT_FOUND_DELIVERY_ADDRESS.getMessage());
    }

    /**
     * 배송지 DB id 에 해당하는 배송지의 접근 권한 확인 실패
     * - 실패 사유 : 접근 권한 없음
     */
    @Test
    public void checkAuthorityCustomerOfDeliveryAddress_실패_DENIED_ACCESS_TO_DELIVERY_ADDRESS() {
        // given
        final String email = "user@gmail.com";
        final Long deliveryAddressId = 1L;
        final String emailOfDeliveryAddress = "user-delivery-address@gmail.com";

        // stub
        when(deliveryAddressRepository.findUserEmailById(deliveryAddressId)).thenReturn(Optional.of(emailOfDeliveryAddress));

        // when & then
        Assertions.assertThatThrownBy(() -> customerDeliveryAddressService.checkAuthorityCustomerOfDeliveryAddress(email, deliveryAddressId))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(CustomerErrorCode.DENIED_ACCESS_TO_DELIVERY_ADDRESS.getMessage());
    }
}
