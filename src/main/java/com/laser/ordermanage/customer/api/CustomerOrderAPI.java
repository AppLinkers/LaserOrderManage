package com.laser.ordermanage.customer.api;

import com.laser.ordermanage.customer.dto.request.CreateCustomerOrderRequest;
import com.laser.ordermanage.customer.service.CustomerOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/customer/order")
@RestController
public class CustomerOrderAPI {

    private final CustomerOrderService customerOrderService;

    /**
     * 고객 회원의 거래 생성
     * - 고객 회원의 이메일 기준으로 고객 조회 및 거래 데이터와 연관관계 매핑
     * - 배송지 PK 기준으로 배송지 조회 및 거래 데이터와 연관관계 매핑
     * - 제조 서비스 및 후처리 서비스 데이터 생성 및 거래 데이터와 연관관계 매핑
     * - 도면 데이터 생성 및 거래 데이터와 연관관계 매핑
     * - 거래 데이터 생성
     */
    @PostMapping("")
    public ResponseEntity<?> createOrder(@RequestBody @Valid CreateCustomerOrderRequest request) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        customerOrderService.createOrder(user, request);

        return ResponseEntity.ok().build();
    }
}
