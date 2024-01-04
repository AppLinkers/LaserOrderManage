package com.laser.ordermanage.factory.api;

import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.exception.ErrorCode;
import com.laser.ordermanage.common.scheduler.service.ScheduleService;
import com.laser.ordermanage.factory.dto.request.FactoryCreateOrUpdateOrderQuotationRequest;
import com.laser.ordermanage.factory.dto.request.FactoryUpdateOrderIsUrgentRequest;
import com.laser.ordermanage.factory.dto.response.FactoryCreateOrUpdateOrderQuotationResponse;
import com.laser.ordermanage.factory.service.FactoryOrderService;
import com.laser.ordermanage.order.domain.Order;
import com.laser.ordermanage.order.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
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
    private final ScheduleService scheduleService;

    /**
     * 거래 긴급 설정
     * - path parameter {order-id} 에 해당하는 거래 조회
     * - 거래 긴급 설정 가능 단계 확인 (견적 대기, 견적 승인, 제작 중, 배송 중)
     * - 거래 긴급을 isUrgent 에 맞춰서 설정
     * - 거래의 고객에게 메일 전송
     */
    @PatchMapping("/{order-id}/urgent")
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
     * - 거래 견적서의 납기일이 거래 생성일 이후인지 확인
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

        if (!order.enableManageQuotation()) {
            throw new CustomCommonException(ErrorCode.INVALID_ORDER_STAGE, order.getStage().getValue());
        }

        if (order.getCreatedAt().toLocalDate().isAfter(request.deliveryDate())) {
            throw new CustomCommonException(ErrorCode.INVALID_FIELDS, "견적서의 납기일은 거래 생성일 이후이어야 합니다.");
        }

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
    @PatchMapping("/{order-id}/purchase-order")
    public ResponseEntity<?> approvePurchaseOrder(@PathVariable("order-id") Long orderId) {

        Order order = factoryOrderService.approvePurchaseOrder(orderId);

        factoryOrderService.sendEmailForApprovePurchaseOrder(order);
        return ResponseEntity.ok().build();
    }

    /**
     * 거래 제작 완료
     * - path parameter {order-id} 에 해당하는 거래 조회
     * - 거래 제작 완료 가능 단계 확인 (제작 중)
     * - 거래 단계 변경 : 제작 중 -> 제작 완료
     * - 거래의 고객에게 메일 전송
     * - 7일 후, 거래 단계 변경 (제작 완료 -> 거래 완료) 를 위한 Job 을 Schedule 에 등록
     */
    @PatchMapping("/{order-id}/stage/production-completed")
    public ResponseEntity<?> changeStageToProductionCompleted(@PathVariable("order-id") Long orderId) {

        Order order = factoryOrderService.changeStageToProductionCompleted(orderId);

        factoryOrderService.sendEmailForChangeStageToProductionCompleted(order);

        scheduleService.addJobForChangeStageToCompleted(order.getId());

        return ResponseEntity.ok().build();
    }

    /**
     * 거래 완료 - 이메일로 인수자 확인 및 서명 링크 전송
     * - path parameter {order-id} 에 해당하는 거래 조회
     * - 거래 완료 가능 단계 확인 (제작 완료)
     * - 공장에게 인수자 확인 및 서명 링크를 메일로 전송합니다.
     */
    @PostMapping("/{order-id}/acquirer/email-link")
    public ResponseEntity<?> sendEmailForAcquirer(
            @PathVariable("order-id") Long orderId,
            @NotEmpty(message = "base URL 은 필수 입력값입니다.")
            @Pattern(regexp = "^((http(s?))\\:\\/\\/)([0-9a-zA-Z\\-]+\\.)+[a-zA-Z]{2,6}(\\:[0-9]+)?(\\/\\S*)?$", message = "base URL 형식이 유효하지 않습니다.")
            @RequestParam(value = "base-url") String baseUrl
    ) {
        factoryOrderService.sendEmailForAcquirer(orderId, baseUrl);

        return ResponseEntity.ok().build();
    }
}
