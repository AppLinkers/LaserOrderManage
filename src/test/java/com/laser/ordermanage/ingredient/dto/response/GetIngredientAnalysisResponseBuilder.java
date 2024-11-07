package com.laser.ordermanage.ingredient.dto.response;

import com.laser.ordermanage.common.paging.ListResponse;

import java.time.LocalDate;
import java.util.List;

public class GetIngredientAnalysisResponseBuilder {
    // 전체 합계, 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 재고 - 전체 보기, 수량
    // 전체 합계, 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 재고 - 전체 선택, 수량
    public static GetIngredientAnalysisResponse build1() {
        return GetIngredientAnalysisResponse.builder()
                .timeUnit("year")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2024, 1, 1))
                .itemList(new ListResponse<>(GetIngredientAnalysisItemResponseBuilder.buildList1()))
                .build();
    }

    // 전체 합계, 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 재고 - 전체 보기, 무게
    // 전체 합계, 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 재고 - 전체 선택, 무게
    public static GetIngredientAnalysisResponse build2() {
        return GetIngredientAnalysisResponse.builder()
                .timeUnit("year")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2024, 1, 1))
                .itemList(new ListResponse<>(GetIngredientAnalysisItemResponseBuilder.buildList2()))
                .build();
    }

    // 전체 합계, 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 단가 - 전체 보기
    // 전체 합계, 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 단가 - 전체 선택
    public static GetIngredientAnalysisResponse build3() {
        return GetIngredientAnalysisResponse.builder()
                .timeUnit("year")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2024, 1, 1))
                .itemList(new ListResponse<>(GetIngredientAnalysisItemResponseBuilder.buildList3()))
                .build();
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    // 전체 합계, 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 재고 - 전체 보기, 수량
    // 전체 합계, 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 재고 - 전체 선택, 수량
    public static GetIngredientAnalysisResponse build4() {return GetIngredientAnalysisResponse.builder()
                .timeUnit("month")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 12, 1))
                .itemList(new ListResponse<>(GetIngredientAnalysisItemResponseBuilder.buildList4()))
                .build();
    }

    // 전체 합계, 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 재고 - 전체 보기, 무게
    // 전체 합계, 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 재고 - 전체 선택, 무게
    public static GetIngredientAnalysisResponse build5() {
        return GetIngredientAnalysisResponse.builder()
                .timeUnit("month")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 12, 1))
                .itemList(new ListResponse<>(GetIngredientAnalysisItemResponseBuilder.buildList5()))
                .build();
    }


    // 전체 합계, 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 단가 - 전체 보기
    // 전체 합계, 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 단가 - 전체 선택
    public static GetIngredientAnalysisResponse build6() {
        return GetIngredientAnalysisResponse.builder()
                .timeUnit("month")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 12, 1))
                .itemList(new ListResponse<>(GetIngredientAnalysisItemResponseBuilder.buildList6()))
                .build();
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    // 평균, 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 재고 - 전체 보기, 수량
    // 평균, 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 재고 - 전체 선택, 수량
    public static GetIngredientAnalysisResponse build7() {
        return GetIngredientAnalysisResponse.builder()
                .timeUnit("year")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2024, 1, 1))
                .itemList(new ListResponse<>(GetIngredientAnalysisItemResponseBuilder.buildList7()))
                .build();
    }

    // 평균, 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 재고 - 전체 보기, 무게
    // 평균, 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 재고 - 전체 선택, 무게
    public static GetIngredientAnalysisResponse build8() {
        return GetIngredientAnalysisResponse.builder()
                .timeUnit("year")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2024, 1, 1))
                .itemList(new ListResponse<>(GetIngredientAnalysisItemResponseBuilder.buildList8()))
                .build();
    }

    // 평균, 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 단가 - 전체 보기
    // 평균, 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 단가 - 전체 선택
    public static GetIngredientAnalysisResponse build9() {
        return GetIngredientAnalysisResponse.builder()
                .timeUnit("year")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2024, 1, 1))
                .itemList(new ListResponse<>(GetIngredientAnalysisItemResponseBuilder.buildList9()))
                .build();
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    // 평균, 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 재고 - 전체 보기, 수량
    // 평균, 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 재고 - 전체 선택, 수량
    public static GetIngredientAnalysisResponse build10() {
        return GetIngredientAnalysisResponse.builder()
                .timeUnit("month")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 12, 1))
                .itemList(new ListResponse<>(GetIngredientAnalysisItemResponseBuilder.buildList10()))
                .build();
    }

    // 평균, 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 재고 - 전체 보기, 무게
    // 평균, 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 재고 - 전체 선택, 무게
    public static GetIngredientAnalysisResponse build11() {
        return GetIngredientAnalysisResponse.builder()
                .timeUnit("month")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 12, 1))
                .itemList(new ListResponse<>(GetIngredientAnalysisItemResponseBuilder.buildList11()))
                .build();
    }

    // 평균, 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 단가 - 전체 보기
    // 평균, 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 단가 - 전체 선택
    public static GetIngredientAnalysisResponse build12() {
        return GetIngredientAnalysisResponse.builder()
                .timeUnit("month")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 12, 1))
                .itemList(new ListResponse<>(GetIngredientAnalysisItemResponseBuilder.buildList12()))
                .build();
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    // 자재 선택 (id = 1), 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 재고 - 전체 보기, 수량
    // 자재 선택 (id = 1), 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 재고 - 전체 선택, 수량
    public static GetIngredientAnalysisResponse build13() {
        return GetIngredientAnalysisResponse.builder()
                .timeUnit("year")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2024, 1, 1))
                .itemList(new ListResponse<>(GetIngredientAnalysisItemResponseBuilder.buildList13()))
                .build();
    }

    // 자재 선택 (id = 1), 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 재고 - 전체 보기, 무게
    // 자재 선택 (id = 1), 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 재고 - 전체 선택, 무게
    public static GetIngredientAnalysisResponse build14() {
        return GetIngredientAnalysisResponse.builder()
                .timeUnit("year")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2024, 1, 1))
                .itemList(new ListResponse<>(GetIngredientAnalysisItemResponseBuilder.buildList14()))
                .build();
    }

    // 자재 선택 (id = 1), 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 단가 - 전체 보기
    // 자재 선택 (id = 1), 연간, 시작 날짜 - 2023, 종료 날짜 - 2024, 단가 - 전체 선택
    public static GetIngredientAnalysisResponse build15() {
        return GetIngredientAnalysisResponse.builder()
                .timeUnit("year")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2024, 1, 1))
                .itemList(new ListResponse<>(GetIngredientAnalysisItemResponseBuilder.buildList15()))
                .build();
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    // 자재 선택 (id = 1), 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 재고 - 전체 보기, 수량
    // 자재 선택 (id = 1), 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 재고 - 전체 선택, 수량
    public static GetIngredientAnalysisResponse build16() {
        return GetIngredientAnalysisResponse.builder()
                .timeUnit("month")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 12, 1))
                .itemList(new ListResponse<>(GetIngredientAnalysisItemResponseBuilder.buildList16()))
                .build();
    }

    // 자재 선택 (id = 1), 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 재고 - 전체 보기, 무게
    // 자재 선택 (id = 1), 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 재고 - 전체 선택, 무게
    public static GetIngredientAnalysisResponse build17() {
        return GetIngredientAnalysisResponse.builder()
                .timeUnit("month")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 12, 1))
                .itemList(new ListResponse<>(GetIngredientAnalysisItemResponseBuilder.buildList17()))
                .build();
    }

    // 자재 선택 (id = 1), 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 단가 - 전체 보기
    // 자재 선택 (id = 1), 월간, 시작 날짜 - 2023-01, 종료 날짜 - 2023-12, 단가 - 전체 선택
    public static GetIngredientAnalysisResponse build18() {
        return GetIngredientAnalysisResponse.builder()
                .timeUnit("month")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 12, 1))
                .itemList(new ListResponse<>(GetIngredientAnalysisItemResponseBuilder.buildList18()))
                .build();
    }
}
