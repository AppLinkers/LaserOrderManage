package com.laser.ordermanage.user.api;

import com.laser.ordermanage.user.dto.request.ChangePasswordRequest;
import com.laser.ordermanage.user.dto.request.GetUserEmailRequest;
import com.laser.ordermanage.user.dto.request.RequestPasswordChangeRequest;
import com.laser.ordermanage.user.service.UserAccountService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserAccountAPI {

    private final UserAccountService userAccountService;

    /**
     * 이메일 찾기
     * - 이름 및 휴대폰 번호를 통해 이메일 목록을 조회합니다.
     */
    @GetMapping("/email")
    public ResponseEntity<?> getUserEmail(@RequestBody @Valid GetUserEmailRequest request) {
        return ResponseEntity.ok(userAccountService.getUserEmail(request));
    }

    /**
     * 비밀번호 찾기 - 이메일로 비밀번호 변경 링크 전송
     * - 이메일 기준으로 사용자 조회
     * - 비밀번호 변경 임시 인증 토큰 생성
     * - 비밀번호 변경 링크(baseUrl?token={비밀번호 변경 임시 인증 토큰 값})를 사용자 이메일로 전송
     */
    @GetMapping("/password/email-link/without-auth")
    public ResponseEntity<?> requestPasswordChangeWithOutAuthentication(@RequestBody @Valid RequestPasswordChangeRequest request) {

        userAccountService.requestPasswordChange(request);

        return ResponseEntity.ok().build();
    }

    /**
     * 비밀번호 변경
     * - Request Header 에 존재하는 Change Password Token 추출 및 검증 수행
     * - Redis 에 있는 Change Password Token 조회
     * - Change Password Token 의 이메일 기준으로 사용자 조회
     * - 해당 사용자 비밀번호 변경
     * - Redis 에 있는 Change Password Token 삭제
     */
    @PatchMapping("/password")
    public ResponseEntity<?> changePassword(
            HttpServletRequest httpServletRequest,
            @RequestBody @Valid ChangePasswordRequest request
    ) {

        userAccountService.changePassword(httpServletRequest, request);

        return ResponseEntity.ok().build();
    }
}
