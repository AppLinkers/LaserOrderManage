package com.laser.ordermanage.factory.api;

import com.laser.ordermanage.factory.service.FactoryUserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
