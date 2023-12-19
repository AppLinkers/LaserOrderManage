package com.laser.ordermanage.user.api;

import com.laser.ordermanage.common.constants.ExpireTime;
import com.laser.ordermanage.common.security.jwt.dto.TokenInfo;
import com.laser.ordermanage.user.dto.request.LoginRequest;
import com.laser.ordermanage.user.dto.response.TokenInfoResponse;
import com.laser.ordermanage.user.service.UserAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserAuthAPI {

    private final UserAuthService userAuthService;

    /**
     * 로그인
     * - 회원의 이메일 기준으로 회원 조회
     * - 비밀번호 일치 여부 검증
     * - Access Token, Refresh Token 생성
     * - Redis 에 Refresh Token 데이터 저장
     * - 쿠키에 Refresh Token 설정
     * - Access Token 정보 반환
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletRequest httpServletRequest, @RequestBody @Valid LoginRequest request) {
        TokenInfo tokenInfo = userAuthService.login(httpServletRequest, request);

        TokenInfoResponse tokenInfoResponse = tokenInfo.toTokenInfoResponse();

        ResponseCookie responseCookie = generateResponseCookie(tokenInfo.getRefreshToken());

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(tokenInfoResponse);
    }

    /**
     * Access Token 재 발급
     * - 쿠키에 존재하는 Refresh Token 추출 및 검증 수행
     * - 현재 요청 IP 주소와 로그인 시 IP 주소의 일치 여부 검증
     * - Access Token, Refresh Token 생성
     * - Redis 에 Refresh Token 데이터 저장
     * - 쿠키에 Refresh Token 설정
     * - Access Token 정보 반환
     */
    @PostMapping("/re-issue")
    public ResponseEntity<?> reissue(HttpServletRequest httpServletRequest, @CookieValue(value = "refreshToken") String refreshTokenReq) {
        TokenInfo tokenInfo = userAuthService.reissue(httpServletRequest, refreshTokenReq);

        TokenInfoResponse tokenInfoResponse = tokenInfo.toTokenInfoResponse();

        ResponseCookie responseCookie = generateResponseCookie(tokenInfo.getRefreshToken());

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(tokenInfoResponse);
    }

    /**
     * 로그아웃
     * - Request Header 에 존재하는 Access Token 추출 및 검증 수행
     * - Redis 에 저장되어 있는 Refresh Token 제거
     * - Redis black list 에 Access Token 데이터 저장
     * - 쿠키의 Refresh Token 유효기간 0으로 설정
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest httpServletRequest) {
        userAuthService.logout(httpServletRequest);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, removeResponseCookie().toString()).build();
    }

    private ResponseCookie generateResponseCookie(String refreshToken) {
        return ResponseCookie.from("refreshToken", refreshToken)
                .maxAge(ExpireTime.REFRESH_TOKEN_EXPIRE_TIME_FOR_REDIS_AND_COOKIE)
                .path("/") // 모든 곳에서 쿠키열람이 가능하도록 설정
                .secure(true) // true : https 환경에서만 쿠키가 발동합니다.
                .sameSite(Cookie.SameSite.NONE.attributeValue()) // 동일 사이트과 크로스 사이트에 모두 쿠키 전송이 가능합니다.
                .httpOnly(false) // 브라우저에서 쿠키에 접근할 수 없도록 제한
                .build();
    }

    private ResponseCookie removeResponseCookie() {
        return ResponseCookie.from("refreshToken", null)
                .maxAge(0)
                .path("/") // 모든 곳에서 쿠키열람이 가능하도록 설정
                .secure(true) // true : https 환경에서만 쿠키가 발동합니다.
                .sameSite(Cookie.SameSite.NONE.attributeValue()) // 동일 사이트과 크로스 사이트에 모두 쿠키 전송이 가능합니다.
                .httpOnly(false) // 브라우저에서 쿠키에 접근할 수 없도록 제한
                .build();
    }

}