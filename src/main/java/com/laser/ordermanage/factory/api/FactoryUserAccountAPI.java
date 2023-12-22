package com.laser.ordermanage.factory.api;

import com.laser.ordermanage.factory.dto.request.FactoryUpdateUserAccountRequest;
import com.laser.ordermanage.factory.service.FactoryUserAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/factory/user")
@RestController
public class FactoryUserAccountAPI {

    private final FactoryUserAccountService factoryUserAccountService;

    /**
     * 공장의 마이페이지 계정 정보 조회
     */
    @GetMapping("")
    public ResponseEntity<?> getUserAccount() {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok(factoryUserAccountService.getUserAccount(user.getUsername()));
    }

    /**
     * 공장의 마이페이지 - 계정 기본 정보 변경
     * - 공장 회원의 이메일 기준으로 사용자 조회
     * - 요청 데이터에 맞게 사용자 계정 정보 변경
     */
    @PatchMapping("")
    public ResponseEntity<?> updateUserAccount(@RequestBody @Valid FactoryUpdateUserAccountRequest request) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        factoryUserAccountService.updateUserAccount(user.getUsername(), request);

        return ResponseEntity.ok().build();
    }
}
