package com.laser.ordermanage.common.security.jwt.component;

import com.laser.ordermanage.common.cache.redis.repository.BlackListRedisRepository;
import com.laser.ordermanage.common.constants.ExpireTime;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.exception.ErrorCode;
import com.laser.ordermanage.common.security.jwt.dto.TokenInfo;
import com.laser.ordermanage.user.domain.UserEntity;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

@Slf4j
@Component
public class JwtProvider {

    private static final String ROLE_KEY = "role";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TYPE = "Bearer";
    private static final String TYPE_KEY = "type";

    public static final String TYPE_ACCESS = "access";
    public static final String TYPE_REFRESH = "refresh";
    public static final String TYPE_CHANGE_PASSWORD = "changePassword";

    private final BlackListRedisRepository blackListRedisRepository;
    private final Key key;

    public JwtProvider(@Value("${jwt.secret.key}") String secretKey, BlackListRedisRepository blackListRedisRepository) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.blackListRedisRepository = blackListRedisRepository;
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * email, role 을 가지고 AccessToken, RefreshToken 을 생성
     */
    public TokenInfo generateToken(String email, String role) {

        Date now = new Date();

        // Access JWT Token 생성
        String accessToken = generateJWT(email, role, TYPE_ACCESS, now, ExpireTime.ACCESS_TOKEN_EXPIRE_TIME);

        // Refresh JWT Token 생성
        String refreshToken = generateJWT(email, role, TYPE_REFRESH, now, ExpireTime.REFRESH_TOKEN_EXPIRE_TIME);

        return TokenInfo.builder()
                .role(role)
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpirationTime(ExpireTime.ACCESS_TOKEN_EXPIRE_TIME)
                .refreshToken(refreshToken)
                .refreshTokenExpirationTime(ExpireTime.REFRESH_TOKEN_EXPIRE_TIME)
                .build();
    }

    /**
     * 사용자 정보를 활용하여 비밀번호 변경 인증 토큰 생성
     */
    public String generateChangePasswordToken(UserEntity user) {
        return generateJWT(user.getEmail(), user.getRole().name(), TYPE_CHANGE_PASSWORD, new Date(), ExpireTime.CHANGE_PASSWORD_TOKEN_EXPIRE_TIME);
    }

    public String generateJWT(String subject, String role, String type, Date issuedAt, long expireTime) {
        return Jwts.builder()
                .setSubject(subject)
                .claim(ROLE_KEY, role)
                .claim(TYPE_KEY, type)
                .setIssuedAt(issuedAt)
                .setExpiration(new Date(issuedAt.getTime() + expireTime)) //토큰 만료 시간 설정
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Jwt 토큰을 복호화
     */
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    /**
     * JWT 토큰을 복호화하여 토큰에 들어있는 정보를 추출하여 Authentication 생성
     */
    public Authentication getAuthentication(String jwtToken) {
        // 토큰 복호화
        Claims claims = parseClaims(jwtToken);

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(claims.get(ROLE_KEY).toString()));

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    // 토큰 정보를 검증하는 메서드
    public boolean validateToken(ServletRequest request, String token) {
        try {
            Claims claims = parseClaims(token);

            if (claims.get(ROLE_KEY).toString().isBlank()) {
                throw new CustomCommonException(ErrorCode.UNAUTHORIZED_JWT_TOKEN);
            }

            // access token 이 black list 에 저장되어 있는지 확인
            if (blackListRedisRepository.findByAccessToken(token).isPresent()) {
                throw new CustomCommonException(ErrorCode.INVALID_ACCESS_JWT_TOKEN);
            }
            return true;
        } catch (ExpiredJwtException e) {
            request.setAttribute("exception", ErrorCode.EXPIRED_JWT_TOKEN.getCode());
        } catch (UnsupportedJwtException e) {
            request.setAttribute("exception", ErrorCode.UNSUPPORTED_JWT_TOKEN.getCode());
        } catch (CustomCommonException e) {
            request.setAttribute("exception", e.getErrorCode());
        } catch (Exception e) {
            request.setAttribute("exception", ErrorCode.INVALID_JWT_TOKEN.getCode());
        }

        return false;
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = parseClaims(token);

            if (claims.get(ROLE_KEY).toString().isBlank()) {
                throw new CustomCommonException(ErrorCode.UNAUTHORIZED_JWT_TOKEN);
            }
            return true;
        } catch (ExpiredJwtException e) {
            throw new CustomCommonException(ErrorCode.EXPIRED_JWT_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw new CustomCommonException(ErrorCode.UNSUPPORTED_JWT_TOKEN);
        } catch (CustomCommonException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomCommonException(ErrorCode.INVALID_JWT_TOKEN);
        }
    }

    public Long getExpiration(String token) {
        Date expiration = parseClaims(token).getExpiration();
        // 현재 시간
        Long now = new Date().getTime();
        return (expiration.getTime() - now);
    }

    public String getType(String token) {
        return (String) parseClaims(token).get(TYPE_KEY);
    }

    /**
     * Request Header 에서 토큰 정보 추출
     */
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_TYPE)) {
            try {
                return bearerToken.substring(7);
            } catch (StringIndexOutOfBoundsException e) {}
        }

        request.setAttribute("exception", ErrorCode.MISSING_JWT_TOKEN.getCode());
        return null;
    }
}
