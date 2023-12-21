package com.laser.ordermanage.user.api;

import com.laser.ordermanage.user.dto.request.GetUserEmailRequest;
import com.laser.ordermanage.user.dto.request.RequestPasswordChangeRequest;
import com.laser.ordermanage.user.service.UserAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
