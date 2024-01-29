package com.laser.ordermanage.order.api;

import com.laser.ordermanage.order.dto.request.CreateCommentRequest;
import com.laser.ordermanage.order.dto.response.DeleteOrderResponse;
import com.laser.ordermanage.order.service.OrderEmailService;
import com.laser.ordermanage.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/order")
@RestController
public class OrderAPI {

    private final OrderService orderService;
    private final OrderEmailService orderEmailService;

    /**
     * 거래의 상세 정보 조회
     * - 거래에 대한 현재 로그인한 회원의 접근 권한 확인 (공장 or 거래의 고객 회원)
     * - 거래 PK 기준으로 거래 상세 정보 조회
     * - 거래 상세 정보 : 고객 정보, 거래 기본 정보, 도면 정보, 견적서 정보, 발주서 정보, 인수자 정보
     */
    @GetMapping("/{order-id}/detail")
    public ResponseEntity<?> getOrderDetail(@PathVariable("order-id") Long orderId) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        orderService.checkAuthorityCustomerOfOrderOrFactory(user, orderId);

        return ResponseEntity.ok(orderService.getOrderDetail(orderId));
    }

    /**
     * 거래의 댓글 목록 조회
     * - 거래에 대한 현재 로그인한 회원의 접근 권한 확인 (공장 or 거래의 고객 회원)
     * - 거래 PK 기준으로 거래의 댓글 목록 조회
     */
    @GetMapping("/{order-id}/comment")
    public ResponseEntity<?> getOrderComment(@PathVariable("order-id") Long orderId) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        orderService.checkAuthorityCustomerOfOrderOrFactory(user, orderId);

        return ResponseEntity.ok(orderService.getCommentByOrder(orderId));
    }

    /**
     * 거래에 댓글 작성
     * - 거래에 대한 현재 로그인한 회원의 접근 권한 확인 (공장 or 거래의 고객 회원)
     * - 거래 PK에 해당하는 거래에 댓글 데이터 생성
     * - 댓글 수신자 (공장 or 고객) 에게 이메일 전송
     */
    @PostMapping("/{order-id}/comment")
    public ResponseEntity<?> createOrderComment(
            @PathVariable("order-id") Long orderId,
            @RequestBody @Valid CreateCommentRequest request
    ) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        orderService.checkAuthorityCustomerOfOrderOrFactory(user, orderId);

        Long commentId = orderService.createOrderComment(user.getUsername(), orderId, request);

        orderEmailService.sendEmailForCreateOrderComment(commentId);

        return ResponseEntity.ok().build();
    }

    /**
     * 거래 삭제
     * - 거래에 대한 현재 로그인한 회원의 접근 권한 확인 (공장 or 거래의 고객 회원)
     * - 거래 삭제 가능 단계 확인 (견적 대기, 견적 승인)
     * - 거래 데이터 삭제 및 연관 데이터 삭제 (거래 제조 서비스, 거래 후처리 서비스, 도면, 거래 배송지, 견적서, 발주서, 댓글)
     * - 거래 삭제에 대한 이메일 전송
     */
    @DeleteMapping("/{order-id}")
    public ResponseEntity<?> deleteOrder(@PathVariable("order-id") Long orderId) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        orderService.checkAuthorityCustomerOfOrderOrFactory(user, orderId);

        DeleteOrderResponse deletedOrder = orderService.deleteOrder(orderId);

        orderEmailService.sendEmailForDeleteOrder(user, deletedOrder);

        return ResponseEntity.ok().build();
    }
}
