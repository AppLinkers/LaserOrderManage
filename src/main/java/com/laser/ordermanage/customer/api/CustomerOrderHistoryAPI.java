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
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RequestMapping("/customer/order")
@RestController
public class CustomerOrderHistoryAPI {

    private final CustomerOrderHistoryService customerOrderHistoryService;

    /**
     * 고객 회원의 거래 목록 조회
     * - 고객 회원의 이메일 기준으로 거래 목록 조회
     * - page, size 기준으로 pagination 수행
     * - stageList : 거래 단계 기준
     * - manufacturing : 거래 제조 서비스 기준
     * - query : 거래 이름 기준
     */
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

    /**
     * 고객 회원의 거래 완료 단계인 거래 목록 조회
     * - 고객 회원의 이메일 기준으로 거래 완료 단계인 거래 목록 조회
     * - page, size 기준으로 pagination 수행
     * - query : 거래 이름 기준
     */
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

    /**
     * 고객 회원의 특정 거래의 생성 정보 조회
     * - 고객 회원의 이메일 및 거래 PK 기준으로 거래 생성 정보 및 도면 정보 조회
     */
    @GetMapping("/history/{orderId}")
    public ResponseEntity<?> getOrderCreateInformation(@PathVariable Long orderId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok(customerOrderHistoryService.getOrderCreateInformation(user.getUsername(), orderId));
    }
}
