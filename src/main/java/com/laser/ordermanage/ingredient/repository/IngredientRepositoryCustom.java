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

    List<GetIngredientInfoResponse> findIngredientByFactoryManager(String email);

    List<GetIngredientAnalysisItemResponse> findIngredientAnalysisAsTotalAndMonthAndStockByFactoryManager(String email, LocalDate startDate, LocalDate endDate, List<String> itemTypeList, String stockUnit);

    List<GetIngredientAnalysisItemResponse> findIngredientAnalysisAsTotalAndMonthAndPriceByFactoryManager(String email, LocalDate startDate, LocalDate endDate, List<String> itemTypeList);

    List<GetIngredientAnalysisItemResponse> findIngredientAnalysisAsTotalAndYearAndStockByFactoryManager(String email, LocalDate startDate, LocalDate endDate, List<String> itemTypeList, String stockUnit);

    List<GetIngredientAnalysisItemResponse> findIngredientAnalysisAsTotalAndYearAndPriceByFactoryManager(String email, LocalDate startDate, LocalDate endDate, List<String> itemTypeList);

    List<GetIngredientAnalysisItemResponse> findIngredientAnalysisAsAverageAndMonthAndStockByFactoryManager(String email, LocalDate startDate, LocalDate endDate, List<String> itemTypeList, String stockUnit);

    List<GetIngredientAnalysisItemResponse> findIngredientAnalysisAsAverageAndMonthAndPriceByFactoryManager(String email, LocalDate startDate, LocalDate endDate, List<String> itemTypeList);

    List<GetIngredientAnalysisItemResponse> findIngredientAnalysisAsAverageAndYearAndStockByFactoryManager(String email, LocalDate startDate, LocalDate endDate, List<String> itemTypeList, String stockUnit);

    List<GetIngredientAnalysisItemResponse> findIngredientAnalysisAsAverageAndYearAndPriceByFactoryManager(String email, LocalDate startDate, LocalDate endDate, List<String> itemTypeList);

    List<GetIngredientAnalysisItemResponse> findIngredientAnalysisAsIngredientAndMonthAndStock(Long ingredientId, LocalDate startDate, LocalDate endDate, List<String> itemTypeList, String stockUnit);

    List<GetIngredientAnalysisItemResponse> findIngredientAnalysisAsIngredientAndMonthAndPrice(Long ingredientId, LocalDate startDate, LocalDate endDate, List<String> itemTypeList);

    List<GetIngredientAnalysisItemResponse> findIngredientAnalysisAsIngredientAndYearAndStock(Long ingredientId, LocalDate startDate, LocalDate endDate, List<String> itemTypeList, String stockUnit);

    List<GetIngredientAnalysisItemResponse> findIngredientAnalysisAsIngredientAndYearAndPrice(Long ingredientId, LocalDate startDate, LocalDate endDate, List<String> itemTypeList);
}
