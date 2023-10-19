package com.laser.ordermanage.factory.controller;

import com.laser.ordermanage.common.dto.response.PageRes;
import com.laser.ordermanage.customer.dto.response.GetOrderRes;
import com.laser.ordermanage.factory.dto.response.GetOrderReIssueRes;
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
@RequestMapping("/factory/order")
@RestController
public class FactoryOrderController {

    private final OrderReadService orderReadService;

    @GetMapping("/new/re-issue")
    public ResponseEntity<?> getOrderList(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "has-quotation", required = false) Boolean hasQuotation,
            @RequestParam(value = "is-urgent", required = false) Boolean isUrgent) {

        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Pageable pageable = PageRequest.of(page - 1, size);

        PageRes<GetOrderReIssueRes> response = orderReadService.readNewReIssueByFactory(user.getUsername(), pageable, hasQuotation, isUrgent);

        return ResponseEntity.ok(response);
    }
}
