package com.laser.ordermanage.factory.unit.service;

import com.laser.ordermanage.common.ServiceUnitTest;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.factory.domain.Factory;
import com.laser.ordermanage.factory.domain.FactoryBuilder;
import com.laser.ordermanage.factory.dto.request.FactoryUpdateFactoryAccountRequest;
import com.laser.ordermanage.factory.dto.request.FactoryUpdateFactoryAccountRequestBuilder;
import com.laser.ordermanage.factory.dto.response.FactoryGetFactoryAccountResponse;
import com.laser.ordermanage.factory.dto.response.FactoryGetFactoryAccountResponseBuilder;
import com.laser.ordermanage.factory.exception.FactoryErrorCode;
import com.laser.ordermanage.factory.repository.FactoryRepository;
import com.laser.ordermanage.factory.service.FactoryUserAccountService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.mockito.Mockito.when;

public class FactoryUserAccountServiceUnitTest extends ServiceUnitTest {

    @InjectMocks
    private FactoryUserAccountService factoryUserAccountService;

    @Mock
    private FactoryRepository factoryRepository;

    /**
     * 사용자 이메일 기준으로 공장 조회 성공
     */
    @Test
    public void getFactoryByFactoryManagerUserEmail_성공() {
        // given
        final Factory expectedFactory = FactoryBuilder.build();
        final String factoryManagerUserEmail = "factory@gmail.com";

        // stub
        when(factoryRepository.findFactoryByFactoryManagerUserEmail(factoryManagerUserEmail)).thenReturn(Optional.of(expectedFactory));

        // when
        final Factory actualFactory = factoryUserAccountService.getFactoryByFactoryManagerUserEmail(factoryManagerUserEmail);

        // then
        Assertions.assertThat(actualFactory).isEqualTo(expectedFactory);
    }

    /**
     * 사용자 이메일 기준으로 공장 조회 실패
     * - 실패 사유 : 존재하지 않는 공장
     */
    @Test
    public void getFactoryByFactoryManagerUserEmail_실패_NOT_FOUND_FACTORY() {
        // given
        final String unknownUserEmail = "unknown-user@gmail.com";

        // stub
        when(factoryRepository.findFactoryByFactoryManagerUserEmail(unknownUserEmail)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> factoryUserAccountService.getFactoryByFactoryManagerUserEmail(unknownUserEmail))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(FactoryErrorCode.NOT_FOUND_FACTORY.getMessage());
    }

    /**
     * 사용자 이메일 기준으로 공장 정보 조회 성공
     */
    @Test
    public void getFactoryAccount_성공() {
        // given
        final Factory factory = FactoryBuilder.build();
        final String factoryManagerUserEmail = "factory@gmail.com";
        final FactoryGetFactoryAccountResponse expectedResponse = FactoryGetFactoryAccountResponseBuilder.build();

        // stub
        when(factoryRepository.findFactoryByFactoryManagerUserEmail(factoryManagerUserEmail)).thenReturn(Optional.of(factory));

        // when
        final FactoryGetFactoryAccountResponse actualResponse = factoryUserAccountService.getFactoryAccount(factoryManagerUserEmail);

        // then
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 사용자 이메일 기준으로 공장 정보 조회 실패
     * - 실패 사유 : 존재하지 않는 공장
     */
    @Test
    public void getFactoryAccount_실패_NOT_FOUND_FACTORY() {
        // given
        final String unknownUserEmail = "unknown-user@gmail.com";

        // stub
        when(factoryRepository.findFactoryByFactoryManagerUserEmail(unknownUserEmail)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> factoryUserAccountService.getFactoryAccount(unknownUserEmail))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(FactoryErrorCode.NOT_FOUND_FACTORY.getMessage());
    }

    /**
     * 공장 정보 변경 성공
     */
    @Test
    public void updateFactoryAccount_성공() {
        // given
        final Factory factory = FactoryBuilder.build();
        final String factoryManagerUserEmail = "factory@gmail.com";
        final FactoryUpdateFactoryAccountRequest request = FactoryUpdateFactoryAccountRequestBuilder.build();

        // stub
        when(factoryRepository.findFactoryByFactoryManagerUserEmail(factoryManagerUserEmail)).thenReturn(Optional.of(factory));

        // when
        factoryUserAccountService.updateFactoryAccount(factoryManagerUserEmail, request);

        // then
        Assertions.assertThat(factory.getCompanyName()).isEqualTo(request.companyName());
        Assertions.assertThat(factory.getRepresentative()).isEqualTo(request.representative());
        Assertions.assertThat(factory.getFax()).isEqualTo(request.fax());
    }

    /**
     * 공장 정보 변경 실패
     * - 실패 사유 : 존재하지 않는 공장
     */
    @Test
    public void updateFactoryAccount_실패_NOT_FOUND_FACTORY() {
        // given
        final String unknownUserEmail = "unknown-user@gmail.com";
        final FactoryUpdateFactoryAccountRequest request = FactoryUpdateFactoryAccountRequestBuilder.build();

        // stub
        when(factoryRepository.findFactoryByFactoryManagerUserEmail(unknownUserEmail)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> factoryUserAccountService.updateFactoryAccount(unknownUserEmail, request))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(FactoryErrorCode.NOT_FOUND_FACTORY.getMessage());
    }
}
