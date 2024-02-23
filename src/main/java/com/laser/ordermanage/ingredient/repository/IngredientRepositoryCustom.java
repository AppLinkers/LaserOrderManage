package com.laser.ordermanage.ingredient.repository;

import com.laser.ordermanage.ingredient.dto.response.GetIngredientInfoResponse;
import com.laser.ordermanage.ingredient.dto.response.GetIngredientResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IngredientRepositoryCustom {
    List<GetIngredientResponse> findIngredientStatusByFactoryAndDate(String email, LocalDate date);

    Optional<String> findUserEmailById(Long ingredientId);

    List<GetIngredientInfoResponse> findIngredientByFactory(String email);
}
