package com.laser.ordermanage.customer.api;

import com.laser.ordermanage.customer.dto.request.CustomerUpdateCustomerAccountRequest;
import com.laser.ordermanage.customer.service.CustomerOrderService;
import com.laser.ordermanage.customer.service.CustomerUserAccountService;
import com.laser.ordermanage.user.service.UserAuthService;
import jakarta.servlet.http.HttpServletRequest;
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

    private final UserAuthService userAuthService;
    private final CustomerOrderService customerOrderService;
    private final CustomerUserAccountService customerUserAccountService;

    /**
     * 마이페이지 - 계정 고객 정보 조회
     */
    @GetMapping("")
    public ResponseEntity<?> getCustomerAccount() {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok(customerUserAccountService.getCustomerAccount(user.getUsername()));
    }

    /**
     * 마이페이지 - 계정 고객 정보 변경
     * - 고객 회원의 이메일 기준으로 사용자 조회
     * - 요청 데이터에 맞게 고객 정보 변경
     */
    @PatchMapping("")
    public ResponseEntity<?> updateCustomerAccount(@RequestBody @Valid CustomerUpdateCustomerAccountRequest request) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        customerUserAccountService.updateCustomerAccount(user.getUsername(), request);

        return ResponseEntity.ok().build();
    }

    /**
     * 고객 회원의 회원 탈퇴
     * - 로그아웃 수행
     * - 거래 데이터 삭제 및 고객과의 연관관계 제거
     *      - 거래 단계 (견적 대기, 견적 승인, 제작 중, 제작 완료) : 데이터 삭제
     *      - 거래 단계 (거래 완료) : 고객과의 연관관계 제거 및 삭제 표시
     * - 고객 데이터삭제 (고객, 배송지, 사용자)
     */
    @DeleteMapping("")
    public ResponseEntity<?> deleteUserAccount(HttpServletRequest httpServletRequest) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        userAuthService.logout(httpServletRequest);

        customerOrderService.deleteOrderByStageNotCompleted(user.getUsername());

        customerOrderService.deleteOrderByStageCompleted(user.getUsername());

        customerUserAccountService.deleteUser(user.getUsername());

        return ResponseEntity.ok().build();
    }
}
