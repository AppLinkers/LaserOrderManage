package com.laser.ordermanage.customer.api;

import com.laser.ordermanage.customer.dto.request.CustomerUpdateUserAccountRequest;
import com.laser.ordermanage.customer.service.CustomerUserAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/customer/user")
@RestController
public class CustomerUserAccountAPI {

    private final CustomerUserAccountService customerUserAccountService;

    /**
     * 고객의 마이페이지 계정 정보 조회
     */
    @GetMapping("")
    public ResponseEntity<?> getUserAccount() {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok(customerUserAccountService.getUserAccount(user.getUsername()));
    }

    /**
     * 고객의 마이페이지 - 계정 기본 정보 변경
     * - 고객 회원의 이메일 기준으로 사용자 조회
     * - 요청 데이터에 맞게 사용자 계정 정보 변경
     */
    @PatchMapping("")
    public ResponseEntity<?> updateUserAccount(@RequestBody @Valid CustomerUpdateUserAccountRequest request) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        customerUserAccountService.updateUserAccount(user.getUsername(), request);

        return ResponseEntity.ok().build();
    }
}
