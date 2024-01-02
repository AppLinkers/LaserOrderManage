package com.laser.ordermanage.user.api;

import com.laser.ordermanage.user.dto.request.LoginRequest;
import com.laser.ordermanage.user.service.UserAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
     * - Token 정보 반환
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletRequest httpServletRequest, @RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(userAuthService.login(httpServletRequest, request));
    }

    /**
     * Access Token 재 발급
     * - 쿠키에 존재하는 Refresh Token 추출 및 검증 수행
     * - 현재 요청 IP 주소와 로그인 시 IP 주소의 일치 여부 검증
     * - Access Token, Refresh Token 생성
     * - Redis 에 Refresh Token 데이터 저장
     * - Token 정보 반환
     */
    @PostMapping("/re-issue")
    public ResponseEntity<?> reissue(HttpServletRequest httpServletRequest, @CookieValue(value = "refreshToken") String refreshTokenReq) {
        return ResponseEntity.ok(userAuthService.reissue(httpServletRequest, refreshTokenReq));
    }

    /**
     * 로그아웃
     * - Request Header 에 존재하는 Access Token 추출 및 검증 수행
     * - Redis 에 저장되어 있는 Refresh Token 제거
     * - Redis black list 에 Access Token 데이터 저장
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest httpServletRequest) {
        userAuthService.logout(httpServletRequest);

        return ResponseEntity.ok().build();
    }

}