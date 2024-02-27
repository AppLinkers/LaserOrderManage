package com.laser.ordermanage.ingredient.repository;

import com.laser.ordermanage.ingredient.domain.IngredientStock;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface IngredientStockRepository extends CrudRepository<IngredientStock, Long>, IngredientStockRepositoryCustom {
    Optional<IngredientStock> findByIngredientIdAndCreatedAt(Long ingredientId, LocalDate date);

    IngredientStock findFirstByIngredientIdOrderByCreatedAtDesc(Long ingredientId);
}
