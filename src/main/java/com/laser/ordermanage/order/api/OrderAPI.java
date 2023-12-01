package com.laser.ordermanage.order.api;

import com.laser.ordermanage.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/order")
@RestController
public class OrderAPI {

    private final OrderService orderService;

    /**
     * 거래의 상세 정보 조회
     * - 현재 로그인한 회원 권한 정보 및 거래 PK 기준으로 거래 상세 정보 조회
     * - 거래 상세 정보 : 고객 정보, 거래 기본 정보, 도면 정보, 견적서 정보, 발주서 정보
     */
    @GetMapping("/{order-id}/detail")
    public ResponseEntity<?> getOrderDetail(@PathVariable("order-id") Long orderId) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok(orderService.getOrderDetail(user, orderId));
    }

    /**
     * 거래의 댓글 목록 조회
     * - 현재 로그인한 회원 권한 정보 및 거래 PK 기준으로 거래의 댓글 목록 조회
     */
    @GetMapping("/{order-id}/comment")
    public ResponseEntity<?> getOrderComment(@PathVariable("order-id") Long orderId) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok(orderService.getOrderComment(user, orderId));
    }
}
