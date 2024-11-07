package com.laser.ordermanage.ingredient.dto.response;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record GetIngredientStatusResponse (
    GetIngredientPriceResponse averagePrice,
    GetIngredientTotalStockResponse totalStock,
    List<GetIngredientResponse> ingredientList,
    LocalDate date
) {
    public static GetIngredientStatusResponse from(List<GetIngredientResponse> getIngredientResponseList, LocalDate date) {
        // average price 와 total stock 구하기
        int sumPurchasePrice = 0;
        int sumSellPrice = 0;
        int totalStockCount = 0;
        double totalStockWeight = 0.0;

        int averagePurchase = 0;
        int averageSell = 0;

        if (getIngredientResponseList.size() > 0) {
            for (GetIngredientResponse ingredientResponse : getIngredientResponseList) {
                sumPurchasePrice += ingredientResponse.price().purchase();
                sumSellPrice += ingredientResponse.price().sell();
                totalStockCount += (int) ingredientResponse.stockCount().currentDay();
                totalStockWeight += (double) ingredientResponse.stockWeight().currentDay();
            }

            averagePurchase = sumPurchasePrice / getIngredientResponseList.size();
            averageSell = sumSellPrice / getIngredientResponseList.size();
        }

        return GetIngredientStatusResponse.builder()
                .averagePrice(
                        GetIngredientPriceResponse.builder()
                                .purchase(averagePurchase)
                                .sell(averageSell)
                                .build()
                )
                .totalStock(
                        GetIngredientTotalStockResponse.builder()
                                .count(totalStockCount)
                                .weight(totalStockWeight)
                                .build()
                )
                .ingredientList(getIngredientResponseList)
                .date(date)
                .build();
    }

}
