package com.laser.ordermanage.customer.api;

import com.laser.ordermanage.customer.dto.request.CustomerCreateOrUpdateDeliveryAddressRequest;
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
     * - 배송지 데이터 생성 (기본 배송지 설정)
     */
    @PostMapping("")
    public ResponseEntity<?> createDeliveryAddress(@RequestBody @Valid CustomerCreateOrUpdateDeliveryAddressRequest request) {

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

    /**
     * 고객의 배송지 항목 수정
     * - path parameter {delivery-address-id} 에 해당하는 배송지 조회
     * - 배송지에 대한 현재 로그인한 회원의 접근 권한 확인 (배송지의 고객 회원)
     * - 배송지 항목 수정 (기본 배송지 설정)
     */
    @PutMapping("{delivery-address-id}")
    public ResponseEntity<?> updateDeliveryAddress(
            @PathVariable("delivery-address-id") Long deliveryAddressId,
            @RequestBody @Valid CustomerCreateOrUpdateDeliveryAddressRequest request
    ) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        customerDeliveryAddressService.checkAuthorityCustomerOfDeliveryAddress(user, deliveryAddressId);

        customerDeliveryAddressService.updateDeliveryAddress(user.getUsername(), deliveryAddressId, request);

        return ResponseEntity.ok().build();
    }

    /**
     * 고객의 배송지 삭제
     * - path parameter {delivery-address-id} 에 해당하는 배송지 조회
     * - 배송지에 대한 현재 로그인한 회원의 접근 권한 확인 (배송지의 고객 회원)
     * - 기본 배송지 삭제 시도 시, 에러 반환
     * - 배송지 삭제
     */
    @DeleteMapping("{delivery-address-id}")
    public ResponseEntity<?> deleteDeliveryAddress(@PathVariable("delivery-address-id") Long deliveryAddressId) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        customerDeliveryAddressService.checkAuthorityCustomerOfDeliveryAddress(user, deliveryAddressId);

        customerDeliveryAddressService.deleteDeliveryAddress(deliveryAddressId);

        return ResponseEntity.ok().build();
    }
}
