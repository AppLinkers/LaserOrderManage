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

    @GetMapping("/{order-id}/detail")
    public ResponseEntity<?> getOrderDetail(@PathVariable("order-id") Long orderId) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok(orderService.getOrderDetail(user, orderId));
    }

}
