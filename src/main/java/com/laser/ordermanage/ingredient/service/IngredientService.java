package com.laser.ordermanage.ingredient.service;

import com.laser.ordermanage.ingredient.dto.response.GetIngredientStockResponse;
import com.laser.ordermanage.ingredient.repository.IngredientRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class IngredientService {

    private final IngredientRepositoryCustom ingredientRepository;

    public GetIngredientStockResponse getIngredientStock(String email, LocalDate date, String unit) {
        return ingredientRepository.findIngredientStockByFactoryAndDate(email, date, unit);
    }
}
