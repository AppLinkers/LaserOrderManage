package com.laser.ordermanage.ingredient.repository;

import com.laser.ordermanage.ingredient.domain.IngredientPrice;
import org.springframework.data.repository.CrudRepository;

public interface IngredientPriceRepository extends CrudRepository<IngredientPrice, Long> {
    IngredientPrice findFirstByIngredient_idOrderByCreatedAtDesc(Long ingredientId);
}
