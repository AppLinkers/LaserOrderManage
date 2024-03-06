package com.laser.ordermanage.user.unit.service;

import com.laser.ordermanage.common.ServiceUnitTest;
import com.laser.ordermanage.common.cache.redis.repository.ChangePasswordTokenRedisRepository;
import com.laser.ordermanage.common.email.EmailService;
import com.laser.ordermanage.common.paging.ListResponse;
import com.laser.ordermanage.common.security.jwt.component.JwtProvider;
import com.laser.ordermanage.user.dto.response.GetUserEmailResponse;
import com.laser.ordermanage.user.repository.UserEntityRepository;
import com.laser.ordermanage.user.service.UserAccountService;
import com.laser.ordermanage.user.service.UserAuthService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.mockito.Mockito.when;

public class UserAccountServiceUnitTest extends ServiceUnitTest {

    @InjectMocks
    private UserAccountService userAccountService;

    @Mock
    private EmailService emailService;

    @Mock
    private UserAuthService userAuthService;

    @Mock
    private ChangePasswordTokenRedisRepository changePasswordTokenRedisRepository;

    @Mock
    private UserEntityRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    /**
     * 이름 및 휴대폰 번호로 사용자 이메일 및 이름 목록 조회 성공
     */
    @Test
    public void getUserEmail_성공() {
        // given
        final GetUserEmailResponse expectedGetUserEmailResponse = GetUserEmailResponse.builder()
                .email("user@gmail.com")
                .name("사용자 이름")
                .build();
        final String phone = "01011111111";

        // stub
        when(userRepository.findEmailByNameAndPhone(expectedGetUserEmailResponse.name(), phone)).thenReturn(List.of(expectedGetUserEmailResponse));

        // when
        final ListResponse<GetUserEmailResponse> actualUserEmailResponseListResponse = userAccountService.getUserEmail(expectedGetUserEmailResponse.name(), phone);

        // then
        Assertions.assertThat(actualUserEmailResponseListResponse.totalElements()).isEqualTo(1);
        Assertions.assertThat(actualUserEmailResponseListResponse.contents().size()).isEqualTo(1);
        Assertions.assertThat(actualUserEmailResponseListResponse.contents().get(0)).isSameAs(expectedGetUserEmailResponse);
    }
}
