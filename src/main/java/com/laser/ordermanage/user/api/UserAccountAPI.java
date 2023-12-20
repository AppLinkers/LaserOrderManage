package com.laser.ordermanage.user.api;

import com.laser.ordermanage.user.dto.request.GetUserEmailRequest;
import com.laser.ordermanage.user.service.UserAccountService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
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
     * 비밀번호 찾기 - 이메일 인증코드 요청
     * - 이메일 기준으로 사용자 조회
     * - 이메일 인증 코드 생성
     * - 해당 사용자의 이메일로 인증코드 전송
     * - Redis Verify Code 에 인증 코드 데이터 저장
     */
    @PostMapping("/password/request-verify")
    public ResponseEntity<?> requestEmailVerifyForGetPassword(
            @NotEmpty(message = "이메일은 필수 입력값입니다.")
            @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
            @RequestParam(value = "email") String email
    ) {

        userAccountService.requestEmailVerifyForGetPassword(email);

        return ResponseEntity.ok().build();
    }
}
