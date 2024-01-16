package com.laser.ordermanage.customer.api;

import com.laser.ordermanage.common.exception.CommonErrorCode;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.customer.dto.request.*;
import com.laser.ordermanage.customer.dto.response.CustomerCreateDrawingResponse;
import com.laser.ordermanage.customer.dto.response.CustomerCreateOrUpdateOrderPurchaseOrderResponse;
import com.laser.ordermanage.customer.service.CustomerDeliveryAddressService;
import com.laser.ordermanage.customer.service.CustomerOrderMailService;
import com.laser.ordermanage.customer.service.CustomerOrderService;
import com.laser.ordermanage.order.domain.Order;
import com.laser.ordermanage.order.exception.OrderErrorCode;
import com.laser.ordermanage.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping("/customer/order")
@RestController
public class CustomerOrderAPI {

    private final OrderService orderService;
    private final CustomerOrderService customerOrderService;
    private final CustomerOrderMailService customerOrderMailService;
    private final CustomerDeliveryAddressService customerDeliveryAddressService;

    /**
     * 고객 회원의 거래 생성
     * - 고객 회원의 이메일 기준으로 고객 조회 및 거래 데이터와 연관관계 매핑
     * - 거래 배송지 데이터 생성 및 거래 데이터와 연관관계 매핑
     * - 제조 서비스 및 후처리 서비스 데이터 생성 및 거래 데이터와 연관관계 매핑
     * - 거래 데이터 생성
     * - 도면 데이터 생성 및 거래 데이터와 연관관계 매핑
     */
    @PostMapping("")
    public ResponseEntity<?> createOrder(@RequestBody @Valid CustomerCreateOrderRequest request) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        customerOrderService.createOrder(user, request);

        return ResponseEntity.ok().build();
    }

    /**
     * 거래 배송지 수정
     * - path parameter {order-id} 에 해당하는 거래 조회
     * - 거래에 대한 현재 로그인한 회원의 접근 권한 확인 (거래의 고객 회원)
     * - 거래 배송지 수정 가능 단계 확인 (견적 대기, 견적 승인, 제작 중, 배송 중)
     * - 거래 배송지를 deliveryAddressId 에 맞춰서 설정
     * - 공장에게 메일 전송
     */
    @PatchMapping("/{order-id}/delivery-address")
    public ResponseEntity<?> updateOrderDeliveryAddress(
            @PathVariable("order-id") Long orderId,
            @RequestBody @Valid CustomerUpdateOrderDeliveryAddressRequest request) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        customerOrderService.checkAuthorityOfOrder(user, orderId);

        customerDeliveryAddressService.checkAuthorityCustomerOfDeliveryAddress(user, request.deliveryAddressId());

        customerOrderService.updateOrderDeliveryAddress(orderId, request);

        customerOrderMailService.sendEmailForUpdateOrderDeliveryAddress(orderId);

        return ResponseEntity.ok().build();
    }

    /**
     * 도면 항목 추가
     * - path parameter {order-id} 에 해당하는 거래 조회
     * - 거래에 대한 현재 로그인한 회원의 접근 권한 확인 (거래의 고객 회원)
     * - 거래 도면 항목 추가 가능 단계 확인 (견적 대기, 견적 승인, 제작 중)
     * - 거래 도면 항목 추가
     * - 공장에게 메일 전송
     */
    @PostMapping("/{order-id}/drawing")
    public ResponseEntity<?> createOrderDrawing(
        @PathVariable("order-id") Long orderId,
        @RequestBody @Valid CustomerCreateDrawingRequest request) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        customerOrderService.checkAuthorityOfOrder(user, orderId);

        Long drawingId = customerOrderService.createOrderDrawing(orderId, request);

        customerOrderMailService.sendEmailForCreateOrderDrawing(orderId);

        return ResponseEntity.ok(
                CustomerCreateDrawingResponse.builder()
                        .id(drawingId)
                        .build()
        );
    }

    /**
     * 도면 항목 수정
     * - path parameter {order-id} 에 해당하는 거래 조회
     * - 거래에 대한 현재 로그인한 회원의 접근 권한 확인 (거래의 고객 회원)
     * - 거래 도면 항목 수정 가능 단계 확인 (견적 대기, 견적 승인, 제작 중)
     * - path parameter {drawing-id} 에 해당하는 도면 항목 조회
     * - 거래 도면 항목 수정
     * - 공장에게 메일 전송
     */
    @PatchMapping("/{order-id}/drawing/{drawing-id}")
    public ResponseEntity<?> updateOrderDrawing(
        @PathVariable("order-id") Long orderId,
        @PathVariable("drawing-id") Long drawingId,
        @RequestBody @Valid CustomerUpdateDrawingRequest request) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        customerOrderService.checkAuthorityOfOrder(user, orderId);

        customerOrderService.updateOrderDrawing(orderId, drawingId, request);

        customerOrderMailService.sendEmailForUpdateOrderDrawing(orderId);

        return ResponseEntity.ok().build();
    }

    /**
     * 도면 삭제
     * - path parameter {order-id} 에 해당하는 거래 조회
     * - 거래에 대한 현재 로그인한 회원의 접근 권한 확인 (거래의 고객 회원)
     * - 거래 도면 항목 삭제 가능 단계 확인 (견적 대기, 견적 승인, 제작 중)
     * - 거래 도면 개수 조건 확인 (1개 초과)
     * - path parameter {drawing-id} 에 해당하는 도면 삭제
     * - 공장에게 메일 전송
     */
    @DeleteMapping("/{order-id}/drawing/{drawing-id}")
    public ResponseEntity<?> deleteOrderDrawing(
            @PathVariable("order-id") Long orderId,
            @PathVariable("drawing-id") Long drawingId) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        customerOrderService.checkAuthorityOfOrder(user, orderId);

        customerOrderService.deleteOrderDrawing(orderId, drawingId);

        customerOrderMailService.sendEmailForDeleteOrderDrawing(orderId);

        return ResponseEntity.ok().build();
    }

    /**
     * 견적서 승인
     * - path parameter {order-id} 에 해당하는 거래 조회
     * - 거래에 대한 현재 로그인한 회원의 접근 권한 확인 (거래의 고객 회원)
     * - 거래 견적서 승인 가능 단계 확인 (견적 대기)
     * - 거래 견적서 유무 확인
     * - 거래 단계 변경 : 견적 대기 -> 견적 승인
     * - 공장에게 메일 전송
     */
    @PatchMapping("/{order-id}/quotation")
    public ResponseEntity<?> approveQuotation(@PathVariable("order-id") Long orderId) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        customerOrderService.checkAuthorityOfOrder(user, orderId);

        customerOrderService.approveQuotation(orderId);

        customerOrderMailService.sendEmailForApproveQuotation(orderId);
        return ResponseEntity.ok().build();
    }

    /**
     * 거래 발주서 작성 및 수정
     * - path parameter {order-id} 에 해당하는 거래 조회
     * - 거래에 대한 현재 로그인한 회원의 접근 권한 확인 (거래의 고객 회원)
     * - 거래 발주서 작성 및 수정 가능 단계 확인 (견적 승인)
     * - 거래 발주서의 검수 기간 및 지급일이 거래의 견적서 납기일 이후인지 확인
     * - 거래 발주서 작성 및 수정
     * - 공장에게 메일 전송
     */
    @PutMapping("/{order-id}/purchase-order")
    public ResponseEntity<?> createOrUpdateOrderPurchaseOrder(
        @PathVariable("order-id") Long orderId,
        @RequestParam(required = false) MultipartFile file,
        @RequestPart(value = "purchaseOrder") @Valid CustomerCreateOrUpdateOrderPurchaseOrderRequest request) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        customerOrderService.checkAuthorityOfOrder(user, orderId);

        Order order = orderService.getOrderById(orderId);

        if (!order.enableManagePurchaseOrder()) {
            throw new CustomCommonException(OrderErrorCode.INVALID_ORDER_STAGE, order.getStage().getValue());
        }

        if (order.getQuotation().getDeliveryDate().isAfter(request.inspectionPeriod()) || order.getQuotation().getDeliveryDate().isAfter(request.paymentDate())) {
            throw new CustomCommonException(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, "발주서의 검수기간 및 지급일은 거래 납기일 이후이어야 합니다.");
        }

        CustomerCreateOrUpdateOrderPurchaseOrderResponse response;

        if (order.hasPurchaseOrder()) {
            response = customerOrderService.updateOrderPurchaseOrder(orderId, file, request);
            customerOrderMailService.sendEmailForUpdateOrderPurchaseOrder(orderId);
        } else {
            response = customerOrderService.createOrderPurchaseOrder(orderId, file, request);
            customerOrderMailService.sendEmailForCreateOrderPurchaseOrder(orderId);
        }

        return ResponseEntity.ok(response);
    }

}
