package com.laser.ordermanage.customer.api;

import com.laser.ordermanage.customer.dto.request.CreateCustomerOrderRequest;
import com.laser.ordermanage.customer.service.CustomerOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/customer/order")
@RestController
public class CustomerOrderAPI {

    private final CustomerOrderService customerOrderService;

    @PostMapping("")
    public ResponseEntity<?> createOrder(@RequestBody @Valid CreateCustomerOrderRequest request) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        customerOrderService.createOrder(user, request);

        return ResponseEntity.ok().build();
    }
}
