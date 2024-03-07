package com.laser.ordermanage.user.unit.service;

import com.laser.ordermanage.common.ServiceUnitTest;
import com.laser.ordermanage.common.cache.redis.repository.VerifyCodeRedisRepository;
import com.laser.ordermanage.common.email.EmailService;
import com.laser.ordermanage.customer.repository.DeliveryAddressRepository;
import com.laser.ordermanage.user.dto.response.UserJoinStatusResponse;
import com.laser.ordermanage.user.repository.UserEntityRepository;
import com.laser.ordermanage.user.service.UserJoinService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    UserEntityRepository userEntityRepository;

    @Mock
    VerifyCodeRedisRepository verifyCodeRedisRepository;

    /**
     * 이메일 인증코드 생성 및 이메일 전송 성공
     */
    @Test
    public void requestEmailVerify_성공() {
        // given
        final String email = "new-user@gmail.com";

        // when
        final UserJoinStatusResponse userJoinStatusResponse = userJoinService.requestEmailVerify(email);

        // then

    }
}
