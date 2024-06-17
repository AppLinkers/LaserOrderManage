package com.laser.ordermanage.user.unit.service;

import com.laser.ordermanage.common.ServiceUnitTest;
import com.laser.ordermanage.common.cache.redis.dao.VerifyCode;
import com.laser.ordermanage.common.cache.redis.repository.VerifyCodeRedisRepository;
import com.laser.ordermanage.common.email.EmailService;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.customer.dto.request.JoinCustomerRequest;
import com.laser.ordermanage.customer.repository.DeliveryAddressRepository;
import com.laser.ordermanage.user.domain.UserEntity;
import com.laser.ordermanage.user.domain.UserEntityBuilder;
import com.laser.ordermanage.user.domain.type.SignupMethod;
import com.laser.ordermanage.user.dto.request.JoinCustomerRequestBuilder;
import com.laser.ordermanage.user.dto.request.VerifyEmailRequest;
import com.laser.ordermanage.user.dto.request.VerifyEmailRequestBuilder;
import com.laser.ordermanage.user.dto.response.UserJoinStatusResponse;
import com.laser.ordermanage.user.dto.response.UserJoinStatusResponseBuilder;
import com.laser.ordermanage.user.exception.UserErrorCode;
import com.laser.ordermanage.user.repository.UserEntityRepository;
import com.laser.ordermanage.user.service.UserJoinService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.when;

public class UserJoinServiceUnitTest extends ServiceUnitTest {

    @InjectMocks
    private UserJoinService userJoinService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    DeliveryAddressRepository deliveryAddressRepository;

    @Mock
    UserEntityRepository userRepository;

    @Mock
    VerifyCodeRedisRepository verifyCodeRedisRepository;

    @Mock
    EmailService emailService;

    /**
     * 이메일 인증코드 생성 및 이메일 전송 성공
     * - 신규 회원
     */
    @Test
    public void requestEmailVerify_성공_신규회원() {
        // given
        final UserJoinStatusResponse expectedResponse = UserJoinStatusResponseBuilder.buildPossibleWithOutUserEntity();
        final String email = "new-user@gmail.com";

        // stub
        when(userRepository.findFirstByEmail(email)).thenReturn(Optional.empty());

        // when
        final UserJoinStatusResponse actualResponse = userJoinService.requestEmailVerify(email);

        // then
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 이메일 인증코드 생성 및 이메일 전송 성공
     * - 이메일 중복
     */
    @Test
    public void requestEmailVerify_성공_이메일_중복() {
        // given
        final UserEntity user = UserEntityBuilder.build();
        final UserJoinStatusResponse expectedResponse = UserJoinStatusResponseBuilder.buildImpossibleWithUserEntity(user);

        // stub
        when(userRepository.findFirstByEmail(user.getEmail())).thenReturn(Optional.of(user));

        // when
        final UserJoinStatusResponse actualResponse = userJoinService.requestEmailVerify(user.getEmail());

        // then
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 이메일 인증 코드 검증 성공
     * - 신규 회원 및 인증 코드 검증 성공
     */
    @Test
    public void verifyEmail_성공_신규회원_및_인증코드_검증_성공() {
        // given
        final VerifyEmailRequest request = VerifyEmailRequestBuilder.build();
        final VerifyCode verifyCode = VerifyCode.builder()
                .email(request.email())
                .code(request.code())
                .build();
        final UserJoinStatusResponse expectedResponse = UserJoinStatusResponseBuilder.buildPossibleWithOutUserEntity();

        // stub
        when(userRepository.findFirstByEmail(request.email())).thenReturn(Optional.empty());
        when(verifyCodeRedisRepository.findById(request.email())).thenReturn(Optional.of(verifyCode));

        // when
        final UserJoinStatusResponse actualResponse = userJoinService.verifyEmail(request);

        // then
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 이메일 인증 코드 검증 성공
     * - 이메일 중복
     */
    @Test
    public void verifyEmail_성공_이메일_중복() {
        // given
        final UserEntity user = UserEntityBuilder.build();
        final UserJoinStatusResponse expectedResponse = UserJoinStatusResponseBuilder.buildImpossibleWithUserEntity(user);
        final VerifyEmailRequest request = VerifyEmailRequest.builder()
                .email(user.getEmail())
                .code("123456")
                .build();

        // stub
        when(userRepository.findFirstByEmail(request.email())).thenReturn(Optional.of(user));

        // when
        final UserJoinStatusResponse actualResponse = userJoinService.verifyEmail(request);

        // then
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 이메일 인증 코드 검증 실패
     * - 실패 사유 : 요청 시, 이메일에 해당하는 인증 코드가 존재하지 않음
     */
    @Test
    public void verifyEmail_실패_인증코드_존재_X() {
        // given
        final VerifyEmailRequest request = VerifyEmailRequestBuilder.unknownUserBuild();

        // stub
        when(userRepository.findFirstByEmail(request.email())).thenReturn(Optional.empty());
        when(verifyCodeRedisRepository.findById(request.email())).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> userJoinService.verifyEmail(request))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(UserErrorCode.NOT_FOUND_VERIFY_CODE.getMessage());
    }

    /**
     * 이메일 인증 코드 검증 실패
     * - 실패 사유 : 인증 코드 검증 실패
     */
    @Test
    public void verifyEmail_실패_인증코드_검증() {
        // given
        final VerifyEmailRequest request = VerifyEmailRequestBuilder.build();
        final VerifyCode verifyCode = VerifyCode.builder()
                .email(request.email())
                .code("234567")
                .build();

        // stub
        when(userRepository.findFirstByEmail(request.email())).thenReturn(Optional.empty());
        when(verifyCodeRedisRepository.findById(request.email())).thenReturn(Optional.of(verifyCode));

        // when & then
        Assertions.assertThatThrownBy(() -> userJoinService.verifyEmail(request))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(UserErrorCode.INVALID_VERIFY_CODE.getMessage());
    }

    /**
     * 고객 회원가입 성공
     * - 신규 회원
     */
    @Test
    public void joinCustomer_성공_신규회원() {
        // given
        final JoinCustomerRequest request = JoinCustomerRequestBuilder.build();
        final UserEntity user = UserEntityBuilder.newUserBuild();
        final UserJoinStatusResponse expectedResponse = UserJoinStatusResponseBuilder.buildCompletedWithUserEntity(user);

        // stub
        when(userRepository.findFirstByEmail(request.email())).thenReturn(Optional.empty());

        // when
        UserJoinStatusResponse actualResponse = userJoinService.joinCustomer(request, SignupMethod.BASIC);

        // then
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 고객 회원가입 성공
     * - 이메일 중복
     */
    @Test
    public void joinCustomer_성공_이메일_중복() {
        // given
        final JoinCustomerRequest request = JoinCustomerRequestBuilder.duplicateEmailBuild();
        final UserEntity user = UserEntityBuilder.build();
        final UserJoinStatusResponse expectedResponse = UserJoinStatusResponseBuilder.buildImpossibleWithUserEntity(user);

        // stub
        when(userRepository.findFirstByEmail(request.email())).thenReturn(Optional.of(user));

        // when
        UserJoinStatusResponse actualResponse = userJoinService.joinCustomer(request, SignupMethod.BASIC);

        // then
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }
}
