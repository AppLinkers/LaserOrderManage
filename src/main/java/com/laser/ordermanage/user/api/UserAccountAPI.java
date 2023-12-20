package com.laser.ordermanage.user.api;

import com.laser.ordermanage.user.dto.request.GetUserEmailRequest;
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
}
