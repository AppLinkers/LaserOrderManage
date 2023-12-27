package com.laser.ordermanage.user.unit.service;

import com.laser.ordermanage.common.ServiceUnitTest;
import com.laser.ordermanage.common.cache.redis.repository.RefreshTokenRedisRepository;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.exception.ErrorCode;
import com.laser.ordermanage.common.security.jwt.component.JwtProvider;
import com.laser.ordermanage.user.domain.UserEntity;
import com.laser.ordermanage.user.domain.UserEntityBuilder;
import com.laser.ordermanage.user.domain.type.Role;
import com.laser.ordermanage.user.dto.request.LoginRequest;
import com.laser.ordermanage.user.dto.request.LoginRequestBuilder;
import com.laser.ordermanage.user.dto.response.TokenInfoResponse;
import com.laser.ordermanage.user.dto.response.TokenInfoResponseBuilder;
import com.laser.ordermanage.user.repository.UserEntityRepository;
import com.laser.ordermanage.user.service.UserAuthService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.when;

public class UserAuthServiceUnitTest extends ServiceUnitTest {

    @InjectMocks
    private UserAuthService userAuthService;

    @Mock
    private RefreshTokenRedisRepository refreshTokenRedisRepository;

    @Mock
    private UserEntityRepository userRepository;

    @Mock
    JwtProvider jwtProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    /**
     * 이메일 기준으로 사용자 객체 조회 성공
     */
    @Test
    public void getUserByEmail_성공() {
        // given
        final UserEntity expectedUser = UserEntityBuilder.build();

        // stub
        when(userRepository.findFirstByEmail(expectedUser.getEmail())).thenReturn(Optional.of(expectedUser));

        // when
        final UserEntity actualUser = userAuthService.getUserByEmail(expectedUser.getEmail());

        // then
        Assertions.assertThat(actualUser).isSameAs(expectedUser);
    }

    /**
     * 이메일 기준으로 사용자 객체 조회 실패
     * - 실패 사유 : 존재하지 않는 사용자
     */
    @Test
    public void getUserByEmail_실패_NOT_FOUND_ENTITY() {
        // given
        String invalidUserEmail = "invalid-user@gmail.com";

        // stub
        when(userRepository.findFirstByEmail(invalidUserEmail)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> userAuthService.getUserByEmail(invalidUserEmail))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage("user" + ErrorCode.NOT_FOUND_ENTITY.getMessage());
    }

    /**
     * 사용자 로그인 성공
     */
    @Test
    public void login_성공() {
        // given
       final MockHttpServletRequest servletRequest = new MockHttpServletRequest();

        final LoginRequest loginRequest = LoginRequestBuilder.build();
        final Authentication authentication = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), null, Collections.singleton(new SimpleGrantedAuthority(Role.ROLE_CUSTOMER.name())));
        final TokenInfoResponse expectedTokenInfoResponse = TokenInfoResponseBuilder.build();

        // stub
        when(authenticationManager.authenticate(loginRequest.toAuthentication())).thenReturn(authentication);
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        when(jwtProvider.generateToken(authentication.getName(), role)).thenReturn(expectedTokenInfoResponse);

        // when
        final TokenInfoResponse actualTokenInfoResponse = userAuthService.login(servletRequest, loginRequest);

        // then
        Assertions.assertThat(actualTokenInfoResponse).isSameAs(expectedTokenInfoResponse);
    }

    /**
     * 사용자 로그인 실패
     * - 실패 사유 : 요청 데이터 인증 실패
     * - 존재하지 않는 이메일
     * - 회원 정보와 일치하지 않는 비밀번호
     */
    @Test
    public void login_실패_INVALID_CREDENTIALS() {
        // given
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();

        LoginRequest invalidLoginRequest = LoginRequestBuilder.invalidBuild();

        // stub
        when(authenticationManager.authenticate(invalidLoginRequest.toAuthentication())).thenThrow(new CustomCommonException(ErrorCode.INVALID_CREDENTIALS));

        // when & then
        Assertions.assertThatThrownBy(() -> userAuthService.login(servletRequest, invalidLoginRequest))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(ErrorCode.INVALID_CREDENTIALS.getMessage());

    }

}
