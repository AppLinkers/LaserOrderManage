package com.laser.ordermanage.user.api;

import com.laser.ordermanage.user.dto.request.JoinCustomerRequest;
import com.laser.ordermanage.user.dto.request.VerifyEmailRequest;
import com.laser.ordermanage.user.service.UserJoinService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserJoinAPI {

    private final UserJoinService userJoinService;

    @PostMapping("/request-verify")
    public ResponseEntity<?> requestEmailVerify(
            @NotEmpty(message = "이메일은 필수 입력값입니다.")
            @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
            @RequestParam(value = "email") String email
    ) {
        return ResponseEntity.ok().body(userJoinService.requestEmailVerify(email));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestBody @Valid VerifyEmailRequest request) {
        return ResponseEntity.ok().body(userJoinService.verifyEmail(request));
    }

    @PostMapping("/join/customer")
    public ResponseEntity<?> joinCustomer(@RequestBody @Valid JoinCustomerRequest request) {
        return ResponseEntity.ok().body(userJoinService.joinCustomer(request));
    }
}
