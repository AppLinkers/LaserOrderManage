package com.laser.ordermanage.user.unit.service;

import com.laser.ordermanage.common.ServiceUnitTest;
import com.laser.ordermanage.common.cache.redis.repository.VerifyCodeRedisRepository;
import com.laser.ordermanage.common.email.EmailService;
import com.laser.ordermanage.customer.repository.DeliveryAddressRepository;
import com.laser.ordermanage.user.domain.UserEntity;
import com.laser.ordermanage.user.domain.UserEntityBuilder;
import com.laser.ordermanage.user.dto.response.UserJoinStatusResponse;
import com.laser.ordermanage.user.dto.type.JoinStatus;
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
    EmailService emailService;

    @Mock
    DeliveryAddressRepository deliveryAddressRepository;

    @Mock
    UserEntityRepository userRepository;

    @Mock
    VerifyCodeRedisRepository verifyCodeRedisRepository;

    /**
     * 이메일 인증코드 생성 및 이메일 전송 성공
     * - 신규 회원
     */
    @Test
    public void requestEmailVerify_성공_신규회원() throws Exception {
        // given
        final UserJoinStatusResponse expectedResponse = UserJoinStatusResponse.builderWithOutUserEntity()
                .status(JoinStatus.POSSIBLE)
                .buildWithOutUserEntity();
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
    public void requestEmailVerify_성공_이메일_중복() throws Exception {
        // given
        final UserEntity user = UserEntityBuilder.build();
        final UserJoinStatusResponse expectedResponse = UserJoinStatusResponse.builderWithUserEntity()
                .userEntity(user)
                .status(JoinStatus.IMPOSSIBLE)
                .buildWithUserEntity();

        // stub
        when(userRepository.findFirstByEmail(user.getEmail())).thenReturn(Optional.of(user));

        // when
        final UserJoinStatusResponse actualResponse = userJoinService.requestEmailVerify(user.getEmail());

        // then
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }


}
