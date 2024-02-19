package com.laser.ordermanage.ingredient.repository;

import com.laser.ordermanage.ingredient.domain.IngredientStock;
import org.springframework.data.repository.CrudRepository;

public interface IngredientStockRepository extends CrudRepository<IngredientStock, Long> {
}
