package com.laser.ordermanage.customer.api;

import com.laser.ordermanage.customer.dto.request.CustomerCreateDeliveryAddressRequest;
import com.laser.ordermanage.customer.service.CustomerDeliveryAddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/customer/delivery-address")
@RestController
public class CustomerDeliveryAddressAPI {

    private final CustomerDeliveryAddressService customerDeliveryAddressService;

    /**
     * 고객 회원의 배송지 생성
     * - 기본 배송지 설정
     * - 고객 회원의 이메일 기준으로 고객 조회 및 배송지 데이터와 연관관계 매핑
     * - 배송지 데이터 생성
     */
    @PostMapping("")
    public ResponseEntity<?> createDeliveryAddress(@RequestBody @Valid CustomerCreateDeliveryAddressRequest request) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        customerDeliveryAddressService.createDeliveryAddress(user.getUsername(), request);

        return ResponseEntity.ok().build();
    }

    /**
     * 고객 회원의 배송지 목록 조회
     * - 고객 회원의 이메일 기준으로 배송지 목록 조회
     * - 기본 배송지 여부를 기준으로 정렬 수행
     */
    @GetMapping("")
    public ResponseEntity<?> getDeliveryAddress() {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok(customerDeliveryAddressService.getDeliveryAddress(user.getUsername()));
    }
}
