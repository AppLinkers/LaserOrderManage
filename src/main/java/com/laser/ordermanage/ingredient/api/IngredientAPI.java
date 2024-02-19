package com.laser.ordermanage.ingredient.api;

import com.laser.ordermanage.common.exception.CommonErrorCode;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.ingredient.service.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
