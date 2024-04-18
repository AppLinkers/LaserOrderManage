package com.laser.ordermanage.customer.unit.service;

import com.laser.ordermanage.common.ServiceUnitTest;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.customer.domain.Customer;
import com.laser.ordermanage.customer.domain.CustomerBuilder;
import com.laser.ordermanage.customer.dto.request.CustomerUpdateCustomerAccountRequest;
import com.laser.ordermanage.customer.dto.request.CustomerUpdateCustomerAccountRequestBuilder;
import com.laser.ordermanage.customer.dto.response.CustomerGetCustomerAccountResponse;
import com.laser.ordermanage.customer.dto.response.CustomerGetCustomerAccountResponseBuilder;
import com.laser.ordermanage.customer.exception.CustomerErrorCode;
import com.laser.ordermanage.customer.repository.CustomerRepository;
import com.laser.ordermanage.customer.repository.DeliveryAddressRepository;
import com.laser.ordermanage.customer.service.CustomerUserAccountService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


public class CustomerUserAccountServiceUnitTest extends ServiceUnitTest {

    @InjectMocks
    private CustomerUserAccountService customerUserAccountService;

    @Mock
    private DeliveryAddressRepository deliveryAddressRepository;

    @Mock
    private CustomerRepository customerRepository;

    /**
     * 사용자 이메일 기준으로 고객 조회 성공
     */
    @Test
    public void getCustomerByUserEmail_성공() {
        // given
        final Customer expectedCustomer = CustomerBuilder.build();
        final String userEmail = expectedCustomer.getUser().getEmail();

        // stub
        when(customerRepository.findFirstByUserEmail(userEmail)).thenReturn(Optional.of(expectedCustomer));

        // when
        final Customer actualCustomer = customerUserAccountService.getCustomerByUserEmail(userEmail);

        // then
        Assertions.assertThat(actualCustomer).isEqualTo(expectedCustomer);
    }

    /**
     * 사용자 이메일 기준으로 고객 조회 실패
     * - 실패 사유 : 존재하지 않는 고객
     */
    @Test
    public void getCustomerByUserEmail_실패_NOT_FOUND_CUSTOMER() {
        // given
        final String unknownUserEmail = "unknown-user@gmail.com";

        // stub
        when(customerRepository.findFirstByUserEmail(unknownUserEmail)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> customerUserAccountService.getCustomerByUserEmail(unknownUserEmail))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(CustomerErrorCode.NOT_FOUND_CUSTOMER.getMessage());
    }

    /**
     * 사용자 이메일 기준으로 고객 정보 조회 성공
     */
    @Test
    public void getCustomerAccount_성공() {
        // given
        final Customer customer = CustomerBuilder.build();
        final String userEmail = customer.getUser().getEmail();
        final CustomerGetCustomerAccountResponse expectedResponse = CustomerGetCustomerAccountResponseBuilder.build();

        // stub
        when(customerRepository.findFirstByUserEmail(userEmail)).thenReturn(Optional.of(customer));

        // when
        final CustomerGetCustomerAccountResponse actualResponse = customerUserAccountService.getCustomerAccount(userEmail);

        // then
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 사용자 이메일 기준으로 고객 정보 조회 실패
     * - 실패 사유 : 존재하지 않는 고객
     */
    @Test
    public void getCustomerAccount_실패_NOT_FOUND_CUSTOMER() {
        // given
        final String unknownUserEmail = "unknown-user@gmail.com";

        // stub
        when(customerRepository.findFirstByUserEmail(unknownUserEmail)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> customerUserAccountService.getCustomerAccount(unknownUserEmail))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(CustomerErrorCode.NOT_FOUND_CUSTOMER.getMessage());
    }

    /**
     * 고객 정보 변경 성공
     */
    @Test
    public void updateCustomerAccount_성공() {
        // given
        final Customer customer = CustomerBuilder.build();
        final String userEmail = customer.getUser().getEmail();
        final CustomerUpdateCustomerAccountRequest request = CustomerUpdateCustomerAccountRequestBuilder.build();

        // stub
        when(customerRepository.findFirstByUserEmail(userEmail)).thenReturn(Optional.of(customer));

        // when
        customerUserAccountService.updateCustomerAccount(userEmail, request);

        // then
        Assertions.assertThat(customer.getCompanyName()).isEqualTo(request.companyName());
    }

    /**
     * 고객 정보 변경 실패
     * - 실패 사유 : 존재하지 않는 고객
     */
    @Test
    public void updateCustomerAccount_실패_NOT_FOUND_CUSTOMER() {
        // given
        final String unknownUserEmail = "unknown-user@gmail.com";
        final CustomerUpdateCustomerAccountRequest request = CustomerUpdateCustomerAccountRequestBuilder.build();

        // stub
        when(customerRepository.findFirstByUserEmail(unknownUserEmail)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> customerUserAccountService.updateCustomerAccount(unknownUserEmail, request))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(CustomerErrorCode.NOT_FOUND_CUSTOMER.getMessage());
    }

    /**
     * (고객, 사용자) 제거 기능
     */
    @Test
    public void deleteUser_성공() {
        // given
        final Customer customer = CustomerBuilder.build();
        final String userEmail = customer.getUser().getEmail();

        // stub
        when(customerRepository.findFirstByUserEmail(userEmail)).thenReturn(Optional.of(customer));
        doNothing().when(deliveryAddressRepository).deleteByCustomerId(customer.getId());
        doNothing().when(customerRepository).delete(customer);

        // when
        customerUserAccountService.deleteUser(userEmail);
    }

    /**
     * (고객, 사용자) 제거 기능
     * - 실패 사유 : 존재하지 않는 고객
     */
    @Test
    public void deleteUser_실패_NOT_FOUND_CUSTOMER() {
        // given
        final String unknownUserEmail = "unknown-user@gmail.com";

        // stub
        when(customerRepository.findFirstByUserEmail(unknownUserEmail)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> customerUserAccountService.deleteUser(unknownUserEmail))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(CustomerErrorCode.NOT_FOUND_CUSTOMER.getMessage());
    }
}
