package com.laser.ordermanage.customer.controller;

import com.laser.ordermanage.common.dto.response.PageRes;
import com.laser.ordermanage.customer.dto.response.GetOrderRes;
import com.laser.ordermanage.order.service.OrderReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RequiredArgsConstructor
@RequestMapping("/customer/order")
@RestController
public class CustomerOrderController {

    private final OrderReadService orderReadService;

    @GetMapping("")
    public ResponseEntity<?> getOrderList(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "stage", required = false) List<String> stageList,
            @RequestParam(value = "manufacturing", required = false) List<String> manufacturingList,
            @RequestParam(value = "query", required = false) String query) {

        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Pageable pageable = PageRequest.of(page - 1, size);

        PageRes<GetOrderRes> response = orderReadService.readByCustomer(user.getUsername(), pageable, stageList, manufacturingList, query);

        return ResponseEntity.ok(response);
    }
}
