package com.laser.ordermanage.customer.api;

import com.laser.ordermanage.common.paging.PageResponse;
import com.laser.ordermanage.customer.dto.response.CustomerGetOrderHistoryResponse;
import com.laser.ordermanage.customer.dto.response.CustomerGetOrderIsCompletedHistoryResponse;
import com.laser.ordermanage.customer.service.CustomerOrderHistoryService;
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
public class CustomerOrderHistoryAPI {

    private final CustomerOrderHistoryService customerOrderHistoryService;

    @GetMapping("")
    public ResponseEntity<?> getOrderHistory(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "stage", required = false) List<String> stageList,
            @RequestParam(value = "manufacturing", required = false) List<String> manufacturingList,
            @RequestParam(value = "query", required = false) String query) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Pageable pageable = PageRequest.of(page - 1, size);

        PageResponse<CustomerGetOrderHistoryResponse> response = customerOrderHistoryService.getOrderHistory(user.getUsername(), pageable, stageList, manufacturingList, query);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/history")
    public ResponseEntity<?> getOrderIsCompletedHistory(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "5") Integer size,
            @RequestParam(value = "query", required = false) String query) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Pageable pageable = PageRequest.of(page - 1, size);

        PageResponse<CustomerGetOrderIsCompletedHistoryResponse> response = customerOrderHistoryService.getOrderIsCompletedHistory(user.getUsername(), pageable, query);

        return ResponseEntity.ok(response);
    }
}
