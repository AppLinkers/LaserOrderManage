package com.laser.ordermanage.customer.api;

import com.laser.ordermanage.customer.service.CustomerUserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
