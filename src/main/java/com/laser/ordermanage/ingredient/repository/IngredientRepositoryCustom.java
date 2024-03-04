package com.laser.ordermanage.ingredient.repository;

import com.laser.ordermanage.ingredient.dto.response.GetIngredientAnalysisItemResponse;
import com.laser.ordermanage.ingredient.dto.response.GetIngredientInfoResponse;
import com.laser.ordermanage.ingredient.dto.response.GetIngredientResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IngredientRepositoryCustom {
    List<GetIngredientResponse> findIngredientStatusByFactoryAndDate(String email, LocalDate date);

    Optional<String> findUserEmailById(Long ingredientId);

    List<GetIngredientInfoResponse> findIngredientByFactory(String email);

    List<GetIngredientAnalysisItemResponse> findIngredientAnalysisAsTotalAndMonthAndStockByFactory(String email, LocalDate startDate, LocalDate endDate, List<String> itemTypeList, String stockUnit);

    List<GetIngredientAnalysisItemResponse> findIngredientAnalysisAsTotalAndMonthAndPriceByFactory(String email, LocalDate startDate, LocalDate endDate, List<String> itemTypeList);

    List<GetIngredientAnalysisItemResponse> findIngredientAnalysisAsTotalAndYearAndStockByFactory(String email, LocalDate startDate, LocalDate endDate, List<String> itemTypeList, String stockUnit);

    List<GetIngredientAnalysisItemResponse> findIngredientAnalysisAsTotalAndYearAndPriceByFactory(String email, LocalDate startDate, LocalDate endDate, List<String> itemTypeList);

    List<GetIngredientAnalysisItemResponse> findIngredientAnalysisAsAverageAndMonthAndStockByFactory(String email, LocalDate startDate, LocalDate endDate, List<String> itemTypeList, String stockUnit);

    List<GetIngredientAnalysisItemResponse> findIngredientAnalysisAsAverageAndMonthAndPriceByFactory(String email, LocalDate startDate, LocalDate endDate, List<String> itemTypeList);

    List<GetIngredientAnalysisItemResponse> findIngredientAnalysisAsAverageAndYearAndStockByFactory(String email, LocalDate startDate, LocalDate endDate, List<String> itemTypeList, String stockUnit);

    List<GetIngredientAnalysisItemResponse> findIngredientAnalysisAsAverageAndYearAndPriceByFactory(String email, LocalDate startDate, LocalDate endDate, List<String> itemTypeList);

    List<GetIngredientAnalysisItemResponse> findIngredientAnalysisAsIngredientAndMonthAndStock(Long ingredientId, LocalDate startDate, LocalDate endDate, List<String> itemTypeList, String stockUnit);

    List<GetIngredientAnalysisItemResponse> findIngredientAnalysisAsIngredientAndMonthAndPrice(Long ingredientId, LocalDate startDate, LocalDate endDate, List<String> itemTypeList);

    List<GetIngredientAnalysisItemResponse> findIngredientAnalysisAsIngredientAndYearAndStock(Long ingredientId, LocalDate startDate, LocalDate endDate, List<String> itemTypeList, String stockUnit);

    List<GetIngredientAnalysisItemResponse> findIngredientAnalysisAsIngredientAndYearAndPrice(Long ingredientId, LocalDate startDate, LocalDate endDate, List<String> itemTypeList);
}
