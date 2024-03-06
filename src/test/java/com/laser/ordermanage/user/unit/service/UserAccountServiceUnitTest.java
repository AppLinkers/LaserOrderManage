package com.laser.ordermanage.user.unit.service;

import com.laser.ordermanage.common.ServiceUnitTest;
import com.laser.ordermanage.common.cache.redis.repository.ChangePasswordTokenRedisRepository;
import com.laser.ordermanage.common.email.EmailService;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.paging.ListResponse;
import com.laser.ordermanage.common.security.jwt.component.JwtProvider;
import com.laser.ordermanage.user.domain.UserEntity;
import com.laser.ordermanage.user.domain.UserEntityBuilder;
import com.laser.ordermanage.user.dto.request.RequestChangePasswordRequest;
import com.laser.ordermanage.user.dto.request.RequestChangePasswordRequestBuilder;
import com.laser.ordermanage.user.dto.response.GetUserEmailResponse;
import com.laser.ordermanage.user.exception.UserErrorCode;
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

    /**
     * 비밀번호 변경 링크 요청 성공
     */
    @Test
    public void requestChangePassword_성공() {
        // given
        final RequestChangePasswordRequest request = RequestChangePasswordRequestBuilder.build();
        final UserEntity user = UserEntityBuilder.build();
        final String changePasswordToken = "change-password-token";

        // stub
        when(userAuthService.getUserByEmail(request.email())).thenReturn(user);
        when(jwtProvider.generateChangePasswordToken(user)).thenReturn(changePasswordToken);

        // when & then
        userAccountService.requestChangePassword(request);
    }

    /**
     * 비밀번호 변경 링크 요청 실패
     * - 실패 사유 : 존재하지 않는 사용자
     */
    @Test
    public void requestChangePassword_실패_NOT_FOUND_USER() {
        // given
        final RequestChangePasswordRequest request = RequestChangePasswordRequestBuilder.unknownUserBuild();

        // stub
        when(userAuthService.getUserByEmail(request.email())).thenThrow(new CustomCommonException(UserErrorCode.NOT_FOUND_USER));

        // when & then
        Assertions.assertThatThrownBy(() -> userAccountService.requestChangePassword(request))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(UserErrorCode.NOT_FOUND_USER.getMessage());
    }
}
