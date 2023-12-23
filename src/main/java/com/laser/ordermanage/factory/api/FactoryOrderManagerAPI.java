package com.laser.ordermanage.factory.api;

import com.laser.ordermanage.factory.dto.request.FactoryCreateOrUpdateOrderManagerRequest;
import com.laser.ordermanage.factory.service.FactoryOrderManagerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/factory/order-manager")
@RestController
public class FactoryOrderManagerAPI {

    private final FactoryOrderManagerService factoryOrderManagerService;

    /**
     * 공장 회원의 거래 담당자 생성
     * - 공장 회원의 이메일 기준으로 공장 조회 및 거래 담당자 데이터와 연관관계 매핑
     * - 거래 담당자 데이터 생성
     */
    @PostMapping("")
    public ResponseEntity<?> createOrderManager(@RequestBody @Valid FactoryCreateOrUpdateOrderManagerRequest request) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        factoryOrderManagerService.createOrderManger(user.getUsername(), request);

        return ResponseEntity.ok().build();
    }

    /**
     * 공장 회원의 거래 담당자 목록 조회
     * - 공장 회원의 이메일 기준으로 거래 담당자 목록 조회
     * - 거래 담당자 생성일을 기준으로 내림차순 정렬 수행
     */
    @GetMapping("")
    public ResponseEntity<?> getOrderManagerList() {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok(factoryOrderManagerService.getOrderManagerList(user.getUsername()));
    }

    /**
     * 공장 회원의 거래 담당자 항목 수정
     * - path parameter {order-manager-id} 에 해당하는 거래 담당자 조회
     * - 거래 담당자에 대한 현재 로그인한 회원의 접근 권한 확인 (거래 담당자의 공장 회원)
     * - 거래 담당자 항목 수정
     */
    @PutMapping("{order-manager-id}")
    public ResponseEntity<?> updateOrderManager(
            @PathVariable("order-manager-id") Long orderManagerId,
            @RequestBody @Valid FactoryCreateOrUpdateOrderManagerRequest request
    ) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        factoryOrderManagerService.checkAuthorityFactoryOfOrderManager(user, orderManagerId);

        factoryOrderManagerService.updateOrderManager(orderManagerId, request);

        return ResponseEntity.ok().build();
    }

    /**
     * 공장 회원의 거래 담당자 삭제
     * - path parameter {order-manager-id} 에 해당하는 거래 담당자 조회
     * - 거래 담당자에 대한 현재 로그인한 회원의 접근 권한 확인 (거래 담당자의 공장 회원)
     * - 거래 담당자 삭제
     */
    @DeleteMapping("{order-manager-id}")
    public ResponseEntity<?> deleteOrderManager(@PathVariable("order-manager-id") Long orderManagerId) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        factoryOrderManagerService.checkAuthorityFactoryOfOrderManager(user, orderManagerId);

        factoryOrderManagerService.deleteOrderManager(orderManagerId);

        return ResponseEntity.ok().build();
    }
}
