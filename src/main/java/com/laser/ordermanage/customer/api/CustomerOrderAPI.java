package com.laser.ordermanage.customer.api;

import com.laser.ordermanage.customer.dto.request.CreateCustomerDrawingRequest;
import com.laser.ordermanage.customer.dto.request.CreateCustomerOrderRequest;
import com.laser.ordermanage.customer.dto.request.CustomerUpdateOrderDeliveryAddressRequest;
import com.laser.ordermanage.customer.service.CustomerDeliveryAddressService;
import com.laser.ordermanage.customer.service.CustomerOrderService;
import com.laser.ordermanage.order.domain.Order;
import com.laser.ordermanage.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/customer/order")
@RestController
public class CustomerOrderAPI {

    private final CustomerOrderService customerOrderService;
    private final OrderService orderService;
    private final CustomerDeliveryAddressService customerDeliveryAddressService;

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

    /**
     * 거래 배송지 수정
     * - path parameter {order-id} 에 해당하는 거래 조회
     * - 거래에 대한 현재 로그인한 회원의 접근 권한 확인 (거래의 고객 회원)
     * - 거래 배송지 수정 가능 단계 확인 (견적 대기, 견적 승인, 제작 중, 배송 중)
     * - 거래 배송지를 deliveryAddressId 에 맞춰서 설정
     * - 공장에게 메일 전송
     */
    @PutMapping("/{order-id}/delivery-address")
    public ResponseEntity<?> updateOrderDeliveryAddress(
            @PathVariable("order-id") Long orderId,
            @RequestBody @Valid CustomerUpdateOrderDeliveryAddressRequest request) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        orderService.checkAuthorityCustomerOfOrder(user, orderId);

        customerDeliveryAddressService.checkAuthorityCustomerOfDeliveryAddress(user, request.getDeliveryAddressId());

        Order order = customerOrderService.updateOrderDeliveryAddress(orderId, request);

        customerOrderService.sendEmailForUpdateOrderDeliveryAddress(order);

        return ResponseEntity.ok().build();
    }

    /**
     * 도면 추가
     * - path parameter {order-id} 에 해당하는 거래 조회
     * - 거래에 대한 현재 로그인한 회원의 접근 권한 확인 (거래의 고객 회원)
     * - 거래 도면 추가 가능 단계 확인 (견적 대기, 견적 승인, 제작 중)
     * - 거래 도면 추가
     * - 공장에게 메일 전송
     */
    @PostMapping("/{order-id}/drawing")
    public ResponseEntity<?> createOrderDrawing(
        @PathVariable("order-id") Long orderId,
        @RequestBody @Valid CreateCustomerDrawingRequest request) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        orderService.checkAuthorityCustomerOfOrder(user, orderId);

        Order order = customerOrderService.createOrderDrawing(orderId, request);

        customerOrderService.sendEmailForCreateOrderDrawing(order);

        return ResponseEntity.ok().build();
    }
}
