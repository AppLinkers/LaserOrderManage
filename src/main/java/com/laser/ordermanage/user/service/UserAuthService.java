package com.laser.ordermanage.user.service;

import com.laser.ordermanage.common.cache.redis.dao.BlackList;
import com.laser.ordermanage.common.cache.redis.dao.RefreshToken;
import com.laser.ordermanage.common.cache.redis.repository.BlackListRedisRepository;
import com.laser.ordermanage.common.cache.redis.repository.RefreshTokenRedisRepository;
import com.laser.ordermanage.common.exception.CommonErrorCode;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.security.jwt.component.JwtProvider;
import com.laser.ordermanage.common.util.NetworkUtil;
import com.laser.ordermanage.user.domain.UserEntity;
import com.laser.ordermanage.user.domain.type.SignupMethod;
import com.laser.ordermanage.user.dto.request.LoginKakaoRequest;
import com.laser.ordermanage.user.dto.request.LoginRequest;
import com.laser.ordermanage.user.dto.response.KakaoAccountResponse;
import com.laser.ordermanage.user.dto.response.TokenInfoResponse;
import com.laser.ordermanage.user.exception.UserErrorCode;
import com.laser.ordermanage.user.repository.UserEntityRepository;
import io.netty.handler.codec.http.HttpHeaderValues;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;

    private final BlackListRedisRepository blackListRedisRepository;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    private final UserEntityRepository userRepository;

    @Value("${kakao.account-uri}")
    private String KAKAO_ACCOUNT_URI;

    @Transactional(readOnly = true)
    public UserEntity getUserByEmail(String email) {
        return userRepository.findFirstByEmail(email).orElseThrow(() -> new CustomCommonException(UserErrorCode.NOT_FOUND_USER));
    }

    public Authentication authenticateBasic(LoginRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = request.toAuthentication();

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        return authentication;
    }

    public Authentication authenticateKakao(LoginKakaoRequest request) {
        KakaoAccountResponse kakaoAccountResponse = getKakaoAccount(request.kakaoAccessToken());

        UserEntity userEntity = getUserByEmail(kakaoAccountResponse.kakao_account().email());

        if (userEntity.getSignupMethod().equals(SignupMethod.BASIC)) {
            throw new CustomCommonException(UserErrorCode.EXIST_BASIC_DUPLICATED_EMAIL_USER);
        }

        return new PreAuthenticatedAuthenticationToken(userEntity.getEmail(), null, userEntity.getAuthorities());
    }

    public TokenInfoResponse login(HttpServletRequest httpServletRequest, Authentication authentication) {
        // 1. 인증 정보를 기반으로 JWT 토큰 생성
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        List<String> authorityList = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        TokenInfoResponse response = jwtProvider.generateToken(authentication.getName(), authorityList);

        // 2. RefreshToken 을 Redis 에 저장
        refreshTokenRedisRepository.save(RefreshToken.builder()
                .id(authentication.getName())
                .ip(NetworkUtil.getClientIp(httpServletRequest))
                .authorityList(response.authorityList())
                .refreshToken(response.refreshToken())
                .build());

        // 3. BlackList 에 저장되어 있는 항목 제거
        blackListRedisRepository.findByAccessToken(response.accessToken()).ifPresent(blackListRedisRepository::delete);

        return response;
    }

    public KakaoAccountResponse getKakaoAccount(String kakaoAccessToken) {
        return WebClient.create(KAKAO_ACCOUNT_URI)
                .get()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + kakaoAccessToken) // access token 인가
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> Mono.error(new CustomCommonException(CommonErrorCode.INTERNAL_SERVER_ERROR)))
                .bodyToMono(KakaoAccountResponse.class)
                .block();
    }

    public TokenInfoResponse reissue(HttpServletRequest httpServletRequest, String refreshTokenReq) {
        // 1. refresh token 인지 확인
        if (StringUtils.hasText(refreshTokenReq) && jwtProvider.validateToken(refreshTokenReq)) {
            jwtProvider.validateTokenType(refreshTokenReq, JwtProvider.TYPE_REFRESH);
            RefreshToken refreshToken = refreshTokenRedisRepository.findByRefreshToken(refreshTokenReq);
            if (refreshToken != null) {
                // 2. 최초 로그인한 ip 와 같은지 확인 (처리 방식에 따라 재발급을 하지 않거나 메일 등의 알림을 주는 방법이 있음)
                String currentIpAddress = NetworkUtil.getClientIp(httpServletRequest);
                if (refreshToken.getIp().equals(currentIpAddress)) {
                    // 3. Redis 에 저장된 RefreshToken 정보를 기반으로 JWT Token 생성
                    TokenInfoResponse response = jwtProvider.generateToken(refreshToken.getId(), refreshToken.getAuthorityList());

                    // 4. Redis RefreshToken update
                    refreshTokenRedisRepository.save(RefreshToken.builder()
                            .id(refreshToken.getId())
                            .ip(currentIpAddress)
                            .authorityList(response.authorityList())
                            .refreshToken(response.refreshToken())
                            .build());

                    return response;
                }
            }
        }

        throw new CustomCommonException(UserErrorCode.INVALID_REFRESH_TOKEN);
    }

    public void logout(HttpServletRequest httpServletRequest) {

        String resolvedToken = (String)httpServletRequest.getAttribute("resolvedToken");

        // 1. Access Token 에서 User email 을 가져옵니다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 2. Redis 에서 해당 User email 로 저장된 Refresh Token 이 있는지 여부를 확인 후 있을 경우 삭제합니다.
        refreshTokenRedisRepository.deleteById(authentication.getName());
        // 3. Redis 에서 해당 Access Token 을 Black List 로 저장합니다.
        blackListRedisRepository.save(BlackList.builder()
                .id(authentication.getName())
                .accessToken(resolvedToken)
                .expiration(jwtProvider.getExpiration(resolvedToken))
                .build());

    }
}
