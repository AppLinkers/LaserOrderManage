package com.laser.ordermanage.factory.api;

import com.laser.ordermanage.factory.dto.request.FactoryCreateOrUpdateOrderQuotationRequest;
import com.laser.ordermanage.factory.dto.request.FactoryUpdateOrderIsUrgentRequest;
import com.laser.ordermanage.factory.dto.response.FactoryCreateOrUpdateOrderQuotationResponse;
import com.laser.ordermanage.factory.service.FactoryOrderService;
import com.laser.ordermanage.order.domain.Order;
import com.laser.ordermanage.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping("/factory/order")
@RestController
public class FactoryOrderAPI {

    private final FactoryOrderService factoryOrderService;
    private final OrderService orderService;

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

        Order order = factoryOrderService.updateOrderIsUrgent(orderId, request);

        factoryOrderService.sendMailForUpdateOrderIsUrgent(order);

        return ResponseEntity.ok().build();
    }

    /**
     * 거래 견적서 작성 및 수정
     * - path parameter {order-id} 에 해당하는 거래 조회
     * - 거래 견적서 작성 및 수정 가능 단계 확인 (견적 대기)
     * - 거래 견적서 작성 및 수정
     * - 거래의 고객에게 메일 전송
     */
    @PutMapping("/{order-id}/quotation")
    public ResponseEntity<?> createOrUpdateOrderQuotation(
        @PathVariable("order-id") Long orderId,
        @RequestParam(required = false) MultipartFile file,
        @RequestPart(value = "quotation") @Valid FactoryCreateOrUpdateOrderQuotationRequest request) {

        Order order = orderService.getOrderById(orderId);
        FactoryCreateOrUpdateOrderQuotationResponse response;

        if (order.hasQuotation()) {
            response = factoryOrderService.updateOrderQuotation(order, file, request);
            factoryOrderService.sendMailForUpdateOrderQuotation(order);
        } else {
            response = factoryOrderService.createOrderQuotation(order, file, request);
            factoryOrderService.sendMailForCreateOrderQuotation(order);
        }

        return ResponseEntity.ok(response);
    }

    /**
     * 발주서 승인
     * - path parameter {order-id} 에 해당하는 거래 조회
     * - 거래 발주서 승인 가능 단계 확인 (견적 승인)
     * - 거래 발주서 유무 확인
     * - 거래 단계 변경 : 견적 승인 -> 제작 중
     * - 거래의 고객에게 메일 전송
     */
    @PutMapping("/{order-id}/purchase-order")
    public ResponseEntity<?> approvePurchaseOrder(@PathVariable("order-id") Long orderId) {

        Order order = factoryOrderService.approvePurchaseOrder(orderId);

        factoryOrderService.sendEmailForApprovePurchaseOrder(order);
        return ResponseEntity.ok().build();
    }

    /**
     * 거래 제작 완료
     * - path parameter {order-id} 에 해당하는 거래 조회
     * - 거래 제작 완료 가능 단계 확인 (제작 중)
     * - 거래 단계 변경 : 제작 중 -> 배송 중
     * - 거래의 고객에게 메일 전송
     */
    @PutMapping("/{order-id}/stage/shipping")
    public ResponseEntity<?> changeStageToShipping(@PathVariable("order-id") Long orderId) {

        Order order = factoryOrderService.changeStageToShipping(orderId);

        factoryOrderService.sendEmailForChangeStageToShipping(order);
        return ResponseEntity.ok().build();
    }
}
