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

    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletRequest httpServletRequest, @RequestBody @Valid LoginRequest request) {
        TokenInfo tokenInfo = userAuthService.login(httpServletRequest, request);

        TokenInfoResponse tokenInfoResponse = tokenInfo.toTokenInfoResponse();

        ResponseCookie responseCookie = generateResponseCookie(tokenInfo.getRefreshToken());

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(tokenInfoResponse);
    }

    @PostMapping("/re-issue")
    public ResponseEntity<?> reissue(HttpServletRequest httpServletRequest, @CookieValue(value = "refreshToken") String refreshTokenReq) {
        TokenInfo tokenInfo = userAuthService.reissue(httpServletRequest, refreshTokenReq);

        TokenInfoResponse tokenInfoResponse = tokenInfo.toTokenInfoResponse();

        ResponseCookie responseCookie = generateResponseCookie(tokenInfo.getRefreshToken());

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(tokenInfoResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest httpServletRequest) {
        userAuthService.logout(httpServletRequest);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, removeResponseCookie().toString()).build();
    }

    private ResponseCookie generateResponseCookie(String refreshToken) {
        return ResponseCookie.from("refreshToken", refreshToken)
                .maxAge(ExpireTime.REFRESH_TOKEN_EXPIRE_TIME_FOR_REDIS_AND_COOKIE)
                .path("/") // 모든 곳에서 쿠키열람이 가능하도록 설정
                .secure(false) // true : https 환경에서만 쿠키가 발동합니다.
                .sameSite(Cookie.SameSite.NONE.attributeValue()) // 동일 사이트과 크로스 사이트에 모두 쿠키 전송이 가능합니다.
                .httpOnly(true) // 브라우저에서 쿠키에 접근할 수 없도록 제한
                .build();
    }

    private ResponseCookie removeResponseCookie() {
        return ResponseCookie.from("refreshToken", null)
                .maxAge(ExpireTime.REFRESH_TOKEN_EXPIRE_TIME_FOR_REDIS_AND_COOKIE)
                .path("/") // 모든 곳에서 쿠키열람이 가능하도록 설정
                .secure(false) // true : https 환경에서만 쿠키가 발동합니다.
                .sameSite(Cookie.SameSite.NONE.attributeValue()) // 동일 사이트과 크로스 사이트에 모두 쿠키 전송이 가능합니다.
                .httpOnly(true) // 브라우저에서 쿠키에 접근할 수 없도록 제한
                .build();
    }

}