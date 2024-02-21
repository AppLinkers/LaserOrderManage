package com.laser.ordermanage.ingredient.repository;

import com.laser.ordermanage.ingredient.domain.IngredientStock;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IngredientStockRepository extends CrudRepository<IngredientStock, Long> {
    IngredientStock findFirstByIngredient_idOrderByCreatedAtDesc(Long ingredientId);
}
