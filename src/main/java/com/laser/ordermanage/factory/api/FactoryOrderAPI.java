package com.laser.ordermanage.factory.api;

import com.laser.ordermanage.factory.dto.request.FactoryUpdateOrderIsUrgentRequest;
import com.laser.ordermanage.factory.service.FactoryOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/factory/order")
@RestController
public class FactoryOrderAPI {

    private final FactoryOrderService factoryOrderService;

    /**
     * 거래 긴급 설정
     * - path parameter {order-id} 에 해당하는 거래 조회
     * - 거래 긴급 설정 가능 단계 확인 (견적 대기, 견적 승인, 제작 중, 배송 중)
     * - 거래 긴급을 isUrgent 에 맞춰서 설정
     * - 거래의 고객에게 메일 전송
     */
    @PutMapping("/{order-id}/urgent")
    public ResponseEntity<?> updateOrderIsUrgent(
        @PathVariable("order-id") Long orderId,
        @RequestBody @Valid FactoryUpdateOrderIsUrgentRequest request) {

        factoryOrderService.updateOrderIsUrgent(orderId, request);

        return ResponseEntity.ok().build();
    }
}
