package com.laser.ordermanage.ingredient.api;

import com.laser.ordermanage.common.exception.CommonErrorCode;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.ingredient.dto.request.CreateIngredientRequest;
import com.laser.ordermanage.ingredient.dto.request.UpdateIngredientRequest;
import com.laser.ordermanage.ingredient.service.IngredientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Validated
@RequiredArgsConstructor
@RequestMapping("/factory/ingredient")
@RestController
public class IngredientAPI {

    private final IngredientService ingredientService;

    /**
     * 자재 재고 현황 데이터 조회
     * - 날짜 및 조회 데이터 단위에 맞는 자재 재고 현황 데이터 조회
     */
    @GetMapping("/stock")
    public ResponseEntity<?> getIngredientStock(
            @RequestParam(value = "date") @DateTimeFormat(pattern = "yyyy-mm-dd") LocalDate date,
            @RequestParam(value = "unit") String unit
    ) {

        if (!(unit.equals("count") || unit.equals("weight"))) {
            throw new CustomCommonException(CommonErrorCode.INVALID_PARAMETER, "unit 파라미터가 올바르지 않습니다.");
        }

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok(ingredientService.getIngredientStock(user.getUsername(), date, unit));
    }

    /**
     * 자재 추가
     * - 자재 데이터 생성 및 공장 데이터와 연관관계 매핑
     * - 초기 재고 데이터 생성 및 자재 데이터와 연관관계 매핑
     * - 초기 단가 데이터 생성 및 자재 데이터와 연관관계 매핑
     */
    @PostMapping("")
    public ResponseEntity<?> createIngredient(@RequestBody @Valid CreateIngredientRequest request) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        ingredientService.createIngredient(user.getUsername(), request);

        return ResponseEntity.ok().build();
    }

    /**
     * 자재 재고 및 단가 수정
     * - path parameter {ingredient-id} 에 해당하는 자재 조회
     * - 자재 재고 데이터 생성 및 자재 데이터와 연관관계 매핑
     * - 자재 단가 데이터 생성 및 자재 데이터와 연관관계 매핑
     */
    @PatchMapping("/{ingredient-id}")
    public ResponseEntity<?> updateIngredient(
            @PathVariable("ingredient-id") Long ingredientId,
            @RequestBody @Valid UpdateIngredientRequest request) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        ingredientService.checkAuthorityOfIngredient(user, ingredientId);

        ingredientService.updateIngredient(ingredientId, request);

        return ResponseEntity.ok().build();
    }

    /**
     * 자재 삭제 API 개발
     */
    @DeleteMapping("/{ingredient-id}")
    public ResponseEntity<?> deleteIngredient(
            @PathVariable("ingredient-id") Long ingredientId
    ) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        ingredientService.checkAuthorityOfIngredient(user, ingredientId);

        ingredientService.deleteIngredient(ingredientId);

        return ResponseEntity.ok().build();
    }

}
