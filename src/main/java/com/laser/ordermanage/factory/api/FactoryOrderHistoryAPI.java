package com.laser.ordermanage.factory.api;

import com.laser.ordermanage.common.paging.PageResponse;
import com.laser.ordermanage.factory.dto.response.FactoryGetOrderHistoryResponse;
import com.laser.ordermanage.factory.dto.response.FactoryGetOrderIsNewAndIsNewIssueHistoryResponse;
import com.laser.ordermanage.factory.dto.response.FactoryGetOrderIsNewAndIsReIssueHistoryResponse;
import com.laser.ordermanage.factory.service.FactoryOrderHistoryService;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;


@Validated
@RequiredArgsConstructor
@RequestMapping("/factory/order")
@RestController
public class FactoryOrderHistoryAPI {

    private final FactoryOrderHistoryService factoryOrderHistoryService;

    /**
     * 견적 대기 단계 및 재 발행의 거래 목록 조회
     * - page, size 기준으로 pagination 수행
     * - has-quotation : 견적서 유무 기준
     * - is-urgent : 거래 긴급 기준
     */
    @GetMapping("/new/re-issue")
    public ResponseEntity<?> getOrderIsNewAndIsReIssueHistory(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "has-quotation", required = false) Boolean hasQuotation,
            @RequestParam(value = "is-urgent", required = false) Boolean isUrgent) {

        Pageable pageable = PageRequest.of(page - 1, size);

        PageResponse<FactoryGetOrderIsNewAndIsReIssueHistoryResponse> response = factoryOrderHistoryService.getOrderIsNewAndIsReIssueHistory(pageable, hasQuotation, isUrgent);

        return ResponseEntity.ok(response);
    }

    /**
     * 견적 대기 단계 및 신규 발행의 거래 목록 조회
     * - page, size 기준으로 pagination 수행
     * - has-quotation : 견적서 유무 기준
     * - is-new-customer : 거래의 고객 - 신규 고객 기준
     * - is-urgent : 거래 긴급 기준
     */
    @GetMapping("/new/new-issue")
    public ResponseEntity<?> getOrderIsNewAndIsNewIssueHistory(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "has-quotation", required = false) Boolean hasQuotation,
            @RequestParam(value = "is-new-customer", required = false) Boolean isNewCustomer,
            @RequestParam(value = "is-urgent", required = false) Boolean isUrgent) {

        Pageable pageable = PageRequest.of(page - 1, size);

        PageResponse<FactoryGetOrderIsNewAndIsNewIssueHistoryResponse> response = factoryOrderHistoryService.getOrderIsNewAndIsNewIssueHistory(pageable, hasQuotation, isNewCustomer, isUrgent);

        return ResponseEntity.ok(response);
    }

    /**
     * 거래 목록 조회
     * - page, size 기준으로 pagination 수행
     * - is-completed : 거래 완료 단계 기준
     * - is-urgent : 거래 긴급 기준
     * - date-criterion : (create, delivery) 날짜 기준 선택
     * - start-date, end-date : date-criterion 의 날짜 기준
     * - query : 거래 이름 및 고객 이름 및 고객 회사 이름 기준
     */
    @GetMapping("")
    public ResponseEntity<?> getOrderHistory(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "is-completed", required = false, defaultValue = "false") Boolean isCompleted,
            @RequestParam(value = "is-urgent", required = false) Boolean isUrgent,
            @RequestParam(value = "date-criterion", required = false) String dateCriterion,
            @RequestParam(value = "start-date", required = false) @DateTimeFormat(pattern = "yyyy-mm-dd") LocalDate startDate,
            @RequestParam(value = "end-date", required = false) @DateTimeFormat(pattern = "yyyy-mm-dd") LocalDate endDate,
            @RequestParam(value = "query", required = false) @Pattern(regexp = "^.{0,20}$", message = "검색 단어수의 최대 글자수는 20자입니다.") String query) {

        Pageable pageable = PageRequest.of(page - 1, size);

        PageResponse<FactoryGetOrderHistoryResponse> response = factoryOrderHistoryService.getOrderHistory(pageable, isCompleted, isUrgent, dateCriterion, startDate, endDate, query);

        return ResponseEntity.ok(response);
    }
}
