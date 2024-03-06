package com.laser.ordermanage.user.unit.service;

import com.laser.ordermanage.common.ServiceUnitTest;
import com.laser.ordermanage.common.cache.redis.repository.ChangePasswordTokenRedisRepository;
import com.laser.ordermanage.common.email.EmailService;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.paging.ListResponse;
import com.laser.ordermanage.common.security.jwt.component.JwtProvider;
import com.laser.ordermanage.user.domain.UserEntity;
import com.laser.ordermanage.user.domain.UserEntityBuilder;
import com.laser.ordermanage.user.domain.type.Role;
import com.laser.ordermanage.user.dto.request.ChangePasswordRequest;
import com.laser.ordermanage.user.dto.request.ChangePasswordRequestBuilder;
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
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.Collections;
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

    private MockHttpServletRequest httpServletRequest;

    public void setUp() {
        httpServletRequest = new MockHttpServletRequest();
    }

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

    /**
     * 비밀번호 변경 성공
     */
    @Test
    public void changePassword_성공() {
        // given
        setUp();
        final String changePasswordToken = "change-password-token";
        final ChangePasswordRequest request = ChangePasswordRequestBuilder.build();

        httpServletRequest.setAttribute("resolvedToken", changePasswordToken);

        final UserEntity user = UserEntityBuilder.build();

        final Collection<? extends GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(Role.ROLE_CUSTOMER.name()));
        final Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), "", authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // stub
        when(jwtProvider.getType(changePasswordToken)).thenReturn(JwtProvider.TYPE_CHANGE_PASSWORD);
        when(userAuthService.getUserByEmail(user.getEmail())).thenReturn(user);
        when(passwordEncoder.encode(request.password())).thenReturn(request.password());

        // when
        userAccountService.changePassword(httpServletRequest, request);

        // then
        Assertions.assertThat(user.getPassword()).isEqualTo(request.password());
    }

    /**
     * 비밀번호 변경 실패
     * - 실패 사유 : Access Token 을 활용해 비밀번호 변경 시도
     */
    @Test
    public void changePassword_실패_Token_Type() {
        // given
        setUp();
        final String accessToken = "access-token";
        final ChangePasswordRequest request = ChangePasswordRequestBuilder.build();

        httpServletRequest.setAttribute("resolvedToken", accessToken);

        // stub
        when(jwtProvider.getType(accessToken)).thenReturn(JwtProvider.TYPE_ACCESS);

        // when & then
        Assertions.assertThatThrownBy(() -> userAccountService.changePassword(httpServletRequest, request))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(UserErrorCode.INVALID_CHANGE_PASSWORD_TOKEN.getMessage());
    }
}
