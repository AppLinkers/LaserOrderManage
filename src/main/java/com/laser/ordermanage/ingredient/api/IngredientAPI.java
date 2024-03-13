package com.laser.ordermanage.ingredient.api;

import com.laser.ordermanage.common.exception.CommonErrorCode;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.ingredient.domain.type.IngredientPriceType;
import com.laser.ordermanage.ingredient.domain.type.IngredientStockType;
import com.laser.ordermanage.ingredient.dto.request.CreateIngredientRequest;
import com.laser.ordermanage.ingredient.dto.request.UpdateIngredientRequest;
import com.laser.ordermanage.ingredient.service.IngredientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RequestMapping("/factory/ingredient")
@RestController
public class IngredientAPI {

    private final IngredientService ingredientService;

    /**
     * 자재 현황 데이터 조회
     * - 날짜 및 조회 데이터 단위에 맞는 자재 현황 데이터 조회 (자재 정보, 단가, 재고)
     */
    @GetMapping("/status")
    public ResponseEntity<?> getIngredientStatus(
            @RequestParam(value = "date") @DateTimeFormat(pattern = "yyyy-mm-dd") LocalDate date
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok(ingredientService.getIngredientStatus(user.getUsername(), date));
    }

    /**
     * 자재 추가
     * - 자재 데이터 생성 및 공장 데이터와 연관관계 매핑
     * - 초기 재고 데이터 생성 및 자재 데이터와 연관관계 매핑
     * - 초기 단가 데이터 생성 및 자재 데이터와 연관관계 매핑
     */
    @PreAuthorize("hasAuthority('AUTHORITY_ADMIN')")
    @PostMapping("")
    public ResponseEntity<?> createIngredient(@RequestBody @Valid CreateIngredientRequest request) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        ingredientService.createIngredient(user.getUsername(), request);

        return ResponseEntity.ok().build();
    }

    /**
     * 자재 재고 및 단가 수정
     * - path parameter {ingredient-id} 에 해당하는 자재 조회
     * - 자재에 대한 현재 로그인한 회원의 접근 권한 확인 (자재의 공장 회원)
     * - 자재 삭제 여부 확인
     * - 자재 재고 데이터 계산 검증
     * - 자재 재고 데이터 수정 또는 생성 및 자재 데이터와 연관관계 매핑
     * - 자재 단가 데이터 수정 또는 생성 및 자재 데이터와 연관관계 매핑
     */
    @PreAuthorize("hasAuthority('AUTHORITY_ADMIN')")
    @PatchMapping("/{ingredient-id}")
    public ResponseEntity<?> updateIngredient(
            @PathVariable("ingredient-id") Long ingredientId,
            @RequestBody @Valid UpdateIngredientRequest request) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        ingredientService.checkAuthorityOfIngredient(user.getUsername(), ingredientId);

        ingredientService.updateIngredient(ingredientId, request);

        return ResponseEntity.ok().build();
    }

    /**
     * 자재 삭제
     * - path parameter {ingredient-id} 에 해당하는 자재 조회
     * - 자재에 대한 현재 로그인한 회원의 접근 권한 확인 (자재의 공장 회원)
     * - 자재 삭제 여부 확인 및 삭제 수행 (삭제 날짜 표시)
     */
    @PreAuthorize("hasAuthority('AUTHORITY_ADMIN')")
    @DeleteMapping("/{ingredient-id}")
    public ResponseEntity<?> deleteIngredient(
            @PathVariable("ingredient-id") Long ingredientId
    ) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        ingredientService.checkAuthorityOfIngredient(user.getUsername(), ingredientId);

        ingredientService.deleteIngredient(ingredientId);

        return ResponseEntity.ok().build();
    }

    /**
     * 자재 목록 조회
     * - 현재 로그인한 공장 회원의 자재 목록 조회
     */
    @GetMapping("")
    public ResponseEntity<?> getIngredientInfo() {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok(ingredientService.getIngredientInfoByFactoryManager(user.getUsername()));
    }

    /**
     * 자재 재고 분석 데이터 조회
     * - 조회 기준
     * - 조회 데이터 (전체 합계, 평균, 특정 자재)
     * - 기간 (월간, 연간)
     * - 조회 항목 (재고, 단가)
     * - 재고 -> 단위 (수량, 무게)
     */
    @GetMapping("/analysis")
    public ResponseEntity<?> getIngredientAnalysis(
            @RequestParam(value = "data") String data,
            @RequestParam(value = "ingredient-id", required = false) Long ingredientId,
            @RequestParam(value = "time-unit") String timeUnit,
            @RequestParam(value = "start-date") @DateTimeFormat(pattern = "yyyy-mm-dd") LocalDate startDate,
            @RequestParam(value = "end-date") @DateTimeFormat(pattern = "yyyy-mm-dd") LocalDate endDate,
            @RequestParam(value = "item-unit") String itemUnit,
            @RequestParam(value = "stock-item", required = false, defaultValue = "all") List<String> stockItem,
            @RequestParam(value = "stock-unit", required = false) String stockUnit,
            @RequestParam(value = "price-item", required = false, defaultValue = "all") List<String> priceItem) {

        // validate parameter
        if (!(data.equals("total") || data.equals("average") || data.equals("ingredient"))) {
            throw new CustomCommonException(CommonErrorCode.INVALID_PARAMETER, "data 파라미터가 올바르지 않습니다.");
        }

        if (data.equals("ingredient") && (ingredientId == null)) {
            throw new CustomCommonException(CommonErrorCode.INVALID_PARAMETER, "ingredient-id 파라미터는 필수 입력값입니다.");
        }

        if (!(timeUnit.equals("year") || timeUnit.equals("month"))) {
            throw new CustomCommonException(CommonErrorCode.INVALID_PARAMETER, "time-unit 파라미터가 올바르지 않습니다.");
        }

        if (startDate.isAfter(endDate)) {
            throw new CustomCommonException(CommonErrorCode.INVALID_PARAMETER, "조회 시작 날짜는 종료 날짜 이전이어야 합니다.");
        }

        // todo: 조회 시작 날짜 검증
        LocalDate nowDate = LocalDate.now();
        if (timeUnit.equals("year") && (nowDate.getYear() < startDate.getYear() || nowDate.getYear() < endDate.getYear())) {
            throw new CustomCommonException(CommonErrorCode.INVALID_PARAMETER, "조회 시작 및 종료 날짜는 현재 날짜 이전이어야 합니다.");
        }

        if (timeUnit.equals("month") && (YearMonth.from(nowDate).isBefore(YearMonth.from(startDate)) || YearMonth.from(nowDate).isBefore(YearMonth.from(endDate)))) {
            throw new CustomCommonException(CommonErrorCode.INVALID_PARAMETER, "조회 시작 및 종료 날짜는 현재 날짜 이전이어야 합니다.");
        }

        if (!(itemUnit.equals("stock") || itemUnit.equals("price"))) {
            throw new CustomCommonException(CommonErrorCode.INVALID_PARAMETER, "item-unit 파라미터가 올바르지 않습니다.");
        }

        List<String> ingredientItemTypeList = null;
        if (itemUnit.equals("stock")) {
            ingredientItemTypeList = IngredientStockType.ofRequest(stockItem);

            if (stockUnit == null) {
                throw new CustomCommonException(CommonErrorCode.INVALID_PARAMETER, "stock-unit 파라미터는 필수 입력값입니다.");
            }

            if (!(stockUnit.equals("count") || stockUnit.equals("weight"))) {
                throw new CustomCommonException(CommonErrorCode.INVALID_PARAMETER, "stock-unit 파라미터가 올바르지 않습니다.");
            }
        }

        if (itemUnit.equals("price")) {
            ingredientItemTypeList = IngredientPriceType.ofRequest(priceItem);
        }

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok(ingredientService.getIngredientAnalysisByFactoryManager(user.getUsername(), data, ingredientId, timeUnit, startDate, endDate, itemUnit, ingredientItemTypeList, stockUnit));

    }
}
