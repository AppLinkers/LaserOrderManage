package com.laser.ordermanage.factory.controller;

import com.laser.ordermanage.common.dto.response.PageRes;
import com.laser.ordermanage.factory.dto.response.GetFactoryOrderRes;
import com.laser.ordermanage.factory.dto.response.GetNewIssueNewOrderRes;
import com.laser.ordermanage.factory.dto.response.GetReIssueNewOrderRes;
import com.laser.ordermanage.order.service.OrderReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;


@RequiredArgsConstructor
@RequestMapping("/factory/order")
@RestController
public class FactoryOrderController {

    private final OrderReadService orderReadService;

    @GetMapping("/new/re-issue")
    public ResponseEntity<?> getReIssueNewOrderList(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "has-quotation", required = false) Boolean hasQuotation,
            @RequestParam(value = "is-urgent", required = false) Boolean isUrgent) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Pageable pageable = PageRequest.of(page - 1, size);

        PageRes<GetReIssueNewOrderRes> response = orderReadService.readReIssueNewByFactory(user.getUsername(), pageable, hasQuotation, isUrgent);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/new/new-issue")
    public ResponseEntity<?> getNewIssueNewOrderList(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "has-quotation", required = false) Boolean hasQuotation,
            @RequestParam(value = "is-new-customer", required = false) Boolean isNewCustomer,
            @RequestParam(value = "is-urgent", required = false) Boolean isUrgent) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Pageable pageable = PageRequest.of(page - 1, size);

        PageRes<GetNewIssueNewOrderRes> response = orderReadService.readNewIssueNewByFactory(user.getUsername(), pageable, hasQuotation, isNewCustomer, isUrgent);

        return ResponseEntity.ok(response);
    }

    @GetMapping("")
    public ResponseEntity<?> getOrderList(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "is-completed", required = false, defaultValue = "false") Boolean isCompleted,
            @RequestParam(value = "is-urgent", required = false) Boolean isUrgent,
            @RequestParam(value = "date-criterion", required = false) String dateCriterion,
            @RequestParam(value = "start-date", required = false) @DateTimeFormat(pattern = "yyyy-mm-dd") LocalDate startDate,
            @RequestParam(value = "end-date", required = false) @DateTimeFormat(pattern = "yyyy-mm-dd") LocalDate endDate,
            @RequestParam(value = "query", required = false) String query) {

        Pageable pageable = PageRequest.of(page - 1, size);

        PageRes<GetFactoryOrderRes> response = orderReadService.readByFactory(pageable, isCompleted, isUrgent, dateCriterion, startDate, endDate, query);

        return ResponseEntity.ok(response);
    }
}
