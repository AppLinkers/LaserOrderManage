package com.laser.ordermanage.user.controller;

import com.laser.ordermanage.common.config.ExpireTime;
import com.laser.ordermanage.common.jwt.dto.TokenInfo;
import com.laser.ordermanage.user.dto.request.LoginReq;
import com.laser.ordermanage.user.dto.response.TokenInfoRes;
import com.laser.ordermanage.user.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletRequest httpServletRequest, @RequestBody @Valid LoginReq request) {
        TokenInfo tokenInfo = authService.login(httpServletRequest, request);

        TokenInfoRes tokenInfoRes = tokenInfo.toTokenInfoRes();

        ResponseCookie responseCookie = generateResponseCookie(tokenInfo.getRefreshToken());
        log.info("login");
        log.info(responseCookie.getValue());

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(tokenInfoRes);
    }

    @PostMapping("/re-issue")
    public ResponseEntity<?> reissue(HttpServletRequest httpServletRequest, @CookieValue("refreshToken") String refreshToken) {
        log.info("re-issue");
        log.info(refreshToken);
        TokenInfo tokenInfo = authService.reissue(httpServletRequest, refreshToken);

        TokenInfoRes tokenInfoRes = tokenInfo.toTokenInfoRes();

        ResponseCookie responseCookie = generateResponseCookie(tokenInfo.getRefreshToken());

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(tokenInfoRes);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest httpServletRequest) {
        authService.logout(httpServletRequest);

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
                .maxAge(1)
                .path("/") // 모든 곳에서 쿠키열람이 가능하도록 설정
                .secure(false) // true : https 환경에서만 쿠키가 발동합니다.
                .sameSite(Cookie.SameSite.NONE.attributeValue()) // 동일 사이트과 크로스 사이트에 모두 쿠키 전송이 가능합니다.
                .httpOnly(true) // 브라우저에서 쿠키에 접근할 수 없도록 제한
                .build();
    }

}
