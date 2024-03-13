package com.laser.ordermanage.factory.api;

import com.laser.ordermanage.factory.dto.request.FactoryUpdateFactoryAccountRequest;
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
     * 마이페이지 - 계정 공장 정보 조회
     */
    @GetMapping("")
    public ResponseEntity<?> getFactoryAccount() {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok(factoryUserAccountService.getFactoryAccount(user.getUsername()));
    }

    /**
     * 마이페이지 - 계정 공장 정보 변경
     * - 공장 회원의 이메일 기준으로 공장 조회
     * - 요청 데이터에 맞게 공장 정보 변경
     */
    @PatchMapping("")
    public ResponseEntity<?> updateFactoryAccount(@RequestBody @Valid FactoryUpdateFactoryAccountRequest request) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        factoryUserAccountService.updateFactoryAccount(user.getUsername(), request);

        return ResponseEntity.ok().build();
    }
}
