package com.laser.ordermanage.user.service;

import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.exception.ErrorCode;
import com.laser.ordermanage.common.jwt.dto.TokenInfo;
import com.laser.ordermanage.common.jwt.util.JwtUtil;
import com.laser.ordermanage.common.redis.domain.BlackList;
import com.laser.ordermanage.common.redis.domain.RefreshToken;
import com.laser.ordermanage.common.redis.repository.BlackListRedisRepository;
import com.laser.ordermanage.common.redis.repository.RefreshTokenRedisRepository;
import com.laser.ordermanage.common.util.Helper;
import com.laser.ordermanage.user.dto.request.LoginReq;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final BlackListRedisRepository blackListRedisRepository;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    private final JwtUtil jwtUtil;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public TokenInfo login(HttpServletRequest httpServletRequest, LoginReq request) {

        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = request.toAuthentication();

        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 메서드가 실행될 때 CustomUserDetailService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenInfo response = jwtUtil.generateToken(authentication);

        // 4. RefreshToken 을 Redis 에 저장
        refreshTokenRedisRepository.save(RefreshToken.builder()
                .id(authentication.getName())
                .ip(Helper.getClientIp(httpServletRequest))
                .authorities(authentication.getAuthorities())
                .refreshToken(response.getRefreshToken())
                .build());

        return response;
    }

    public TokenInfo reissue(HttpServletRequest httpServletRequest, String refreshTokenReq) {
        // 1. refresh token 인지 확인
        if (StringUtils.hasText(refreshTokenReq) && jwtUtil.validateToken(refreshTokenReq) && jwtUtil.isRefreshToken(refreshTokenReq)) {
            RefreshToken refreshToken = refreshTokenRedisRepository.findByRefreshToken(refreshTokenReq);
            if (refreshToken != null) {
                // 2. 최초 로그인한 ip 와 같은지 확인 (처리 방식에 따라 재발급을 하지 않거나 메일 등의 알림을 주는 방법이 있음)
                String currentIpAddress = Helper.getClientIp(httpServletRequest);
                if (refreshToken.getIp().equals(currentIpAddress)) {
                    // 3. Redis 에 저장된 RefreshToken 정보를 기반으로 JWT Token 생성
                    TokenInfo response = jwtUtil.generateToken(refreshToken.getId(), refreshToken.getAuthorities());

                    // 4. Redis RefreshToken update
                    refreshTokenRedisRepository.save(RefreshToken.builder()
                            .id(refreshToken.getId())
                            .ip(currentIpAddress)
                            .authorities(refreshToken.getAuthorities())
                            .refreshToken(response.getRefreshToken())
                            .build());

                    return response;
                }
            }
        }

        throw new CustomCommonException(ErrorCode.INVALID_REFRESH_JWT_TOKEN);
    }

    public void logout(HttpServletRequest httpServletRequest) {

        String resolvedToken = (String)httpServletRequest.getAttribute("resolvedToken");

        // 1. access token 인지 확인
        if (StringUtils.hasText(resolvedToken) && jwtUtil.isAccessToken(resolvedToken)) {

            // 2. Access Token 에서 User email 을 가져옵니다.
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // 3. Redis 에서 해당 User email 로 저장된 Refresh Token 이 있는지 여부를 확인 후 있을 경우 삭제합니다.
            refreshTokenRedisRepository.deleteById(authentication.getName());

            // 4. Redis 에서 해당 Access Token 을 Black List 로 저장합니다.
            blackListRedisRepository.save(BlackList.builder()
                    .id(authentication.getName())
                    .accessToken(resolvedToken)
                    .expiration(jwtUtil.getExpiration(resolvedToken))
                    .build());

        } else {
            throw new CustomCommonException(ErrorCode.INVALID_ACCESS_JWT_TOKEN);
        }

    }
}
