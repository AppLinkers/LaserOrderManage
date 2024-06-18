package com.laser.ordermanage.user.unit.service;

import com.laser.ordermanage.common.ServiceUnitTest;
import com.laser.ordermanage.common.cache.redis.dao.RefreshToken;
import com.laser.ordermanage.common.cache.redis.repository.BlackListRedisRepository;
import com.laser.ordermanage.common.cache.redis.repository.RefreshTokenRedisRepository;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.security.jwt.component.JwtProvider;
import com.laser.ordermanage.common.util.NetworkUtil;
import com.laser.ordermanage.user.domain.UserEntity;
import com.laser.ordermanage.user.domain.UserEntityBuilder;
import com.laser.ordermanage.user.domain.type.Authority;
import com.laser.ordermanage.user.domain.type.Role;
import com.laser.ordermanage.user.dto.request.LoginRequest;
import com.laser.ordermanage.user.dto.request.LoginRequestBuilder;
import com.laser.ordermanage.user.dto.response.TokenInfoResponse;
import com.laser.ordermanage.user.dto.response.TokenInfoResponseBuilder;
import com.laser.ordermanage.user.exception.UserErrorCode;
import com.laser.ordermanage.user.repository.UserEntityRepository;
import com.laser.ordermanage.user.service.UserAuthService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

import static org.mockito.Mockito.when;

public class UserAuthServiceUnitTest extends ServiceUnitTest {

    @InjectMocks
    private UserAuthService userAuthService;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private RefreshTokenRedisRepository refreshTokenRedisRepository;

    @Mock
    private BlackListRedisRepository blackListRedisRepository;

    @Mock
    private UserEntityRepository userRepository;

    private MockHttpServletRequest httpServletRequest;

    @BeforeEach
    public void setUp() {
        httpServletRequest = new MockHttpServletRequest();
    }

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
        Assertions.assertThat(actualUser).isEqualTo(expectedUser);
    }

    /**
     * 이메일 기준으로 사용자 객체 조회 실패
     * - 실패 사유 : 존재하지 않는 사용자
     */
    @Test
    public void getUserByEmail_실패_NOT_FOUND_USER() {
        // given
        final String unknownUserEmail = "unknown-user@gmail.com";

        // stub
        when(userRepository.findFirstByEmail(unknownUserEmail)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> userAuthService.getUserByEmail(unknownUserEmail))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(UserErrorCode.NOT_FOUND_USER.getMessage());
    }

    /**
     * 사용자 기본 인증 성공
     */
    @Test
    public void authenticateBasic_성공() {
        // given
        final LoginRequest loginRequest = LoginRequestBuilder.build();
        final Authentication expectedAuthentication = new UsernamePasswordAuthenticationToken(loginRequest.email(), null, Collections.singleton(new SimpleGrantedAuthority(Role.ROLE_CUSTOMER.name())));

        // stub
        when(authenticationManager.authenticate(loginRequest.toAuthentication())).thenReturn(expectedAuthentication);;

        // when
        final Authentication actualAuthentication = userAuthService.authenticateBasic(loginRequest);

        // then
        Assertions.assertThat(actualAuthentication).isEqualTo(expectedAuthentication);
    }

    /**
     * 사용자 기본 인증 실패
     * - 실패 사유 : 요청 데이터 인증 실패
     * - 이메일에 해당하는 사용자가 존재하지 않음
     * - 회원 정보와 일치하지 않는 비밀번호
     */
    @Test
    public void authenticateBasic_실패_INVALID_CREDENTIALS() {
        // given
        final LoginRequest invalidLoginRequest = LoginRequestBuilder.invalidBuild();

        // stub
        when(authenticationManager.authenticate(invalidLoginRequest.toAuthentication())).thenThrow(new CustomCommonException(UserErrorCode.INVALID_CREDENTIALS));

        // when & then
        Assertions.assertThatThrownBy(() -> userAuthService.authenticateBasic(invalidLoginRequest))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(UserErrorCode.INVALID_CREDENTIALS.getMessage());

    }

    /**
     * 사용자 Refresh Token 을 활용한 Access Token 재발급 성공
     */
    @Test
    public void reissue_성공() {
        // given
        final RefreshToken refreshToken = RefreshToken.builder()
                .id("user1@gmail.com")
                .ip(NetworkUtil.getClientIp(httpServletRequest))
                .authorityList(List.of(Role.ROLE_CUSTOMER.name(), Authority.AUTHORITY_ADMIN.name()))
                .refreshToken("refreshToken")
                .build();

        final TokenInfoResponse expectedResponse = TokenInfoResponseBuilder.build();

        // stub
        when(jwtProvider.validateToken(refreshToken.getRefreshToken())).thenReturn(true);

        when(refreshTokenRedisRepository.findByRefreshToken(refreshToken.getRefreshToken())).thenReturn(refreshToken);
        when(jwtProvider.generateToken(refreshToken.getId(), refreshToken.getAuthorityList())).thenReturn(expectedResponse);

        // when
        final TokenInfoResponse actualResponse = userAuthService.reissue(httpServletRequest, refreshToken.getRefreshToken());

        // then
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 사용자 Refresh Token 을 활용한 Access Token 재발급 실패
     * - 실패 사유 : Refresh Token 의 값이 비어있음
     */
    @Test
    public void reissue_실패_Refresh_Token_값_존재() {
        // given
        final String emptyRefreshToken = "";

        // when & then
        Assertions.assertThatThrownBy(() -> userAuthService.reissue(httpServletRequest, emptyRefreshToken))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(UserErrorCode.INVALID_REFRESH_TOKEN.getMessage());
    }

    /**
     * 사용자 Refresh Token 을 활용한 Access Token 재발급 실패
     * - 실패 사유 : 유효하지 않은 Refresh Token 사용
     */
    @Test
    public void reissue_실패_Invalid_Refresh_Token() {
        // given
        final String invalidRefreshToken = "invalidRefreshToken";

        // stub
        when(jwtProvider.validateToken(invalidRefreshToken)).thenReturn(false);

        // when & then
        Assertions.assertThatThrownBy(() -> userAuthService.reissue(httpServletRequest, invalidRefreshToken))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(UserErrorCode.INVALID_REFRESH_TOKEN.getMessage());
    }

    /**
     * 사용자 Refresh Token 을 활용한 Access Token 재발급 실패
     * - 실패 사유 : refreshTokenRedisRepository 에 존재하지 않는 Refresh Token 사용
     */
    @Test
    public void reissue_실패_refreshTokenRedisRepository_존재() {
        // given
        final String invalidRefreshToken = "invalidRefreshToken";

        // stub
        when(jwtProvider.validateToken(invalidRefreshToken)).thenReturn(true);

        when(refreshTokenRedisRepository.findByRefreshToken(invalidRefreshToken)).thenReturn(null);

        // when & then
        Assertions.assertThatThrownBy(() -> userAuthService.reissue(httpServletRequest, invalidRefreshToken))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(UserErrorCode.INVALID_REFRESH_TOKEN.getMessage());
    }

    /**
     * 사용자 Refresh Token 을 활용한 Access Token 재발급 실패
     * - 실패 사유 : 최초 로그인한 IP 와 다른 IP 로부터의 요청
     */
    @Test
    public void reissue_실패_IP_Address() {
        // given
        final RefreshToken refreshToken = RefreshToken.builder()
                .id("user1@gmail.com")
                .ip("256.100.100.100")
                .authorityList(List.of(Role.ROLE_CUSTOMER.name()))
                .refreshToken("refreshToken")
                .build();

        // stub
        when(jwtProvider.validateToken(refreshToken.getRefreshToken())).thenReturn(true);

        when(refreshTokenRedisRepository.findByRefreshToken(refreshToken.getRefreshToken())).thenReturn(refreshToken);

        // when & then
        Assertions.assertThatThrownBy(() -> userAuthService.reissue(httpServletRequest, refreshToken.getRefreshToken()))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(UserErrorCode.INVALID_REFRESH_TOKEN.getMessage());
    }

    /**
     * 사용자 Access Token 을 활용한 로그아웃 성공
     */
    @Test
    public void logout_성공() {
        // given
        final String accessToken = "accessToken";

        httpServletRequest.setAttribute("resolvedToken", accessToken);

        final Collection<? extends GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(Role.ROLE_CUSTOMER.name()));
        final Authentication authentication = new UsernamePasswordAuthenticationToken("user1@gmail.com", "", authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when & then
        userAuthService.logout(httpServletRequest);
    }

}
