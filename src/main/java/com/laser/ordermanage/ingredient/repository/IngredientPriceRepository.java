package com.laser.ordermanage.ingredient.repository;

import com.laser.ordermanage.ingredient.domain.IngredientPrice;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface IngredientPriceRepository extends CrudRepository<IngredientPrice, Long> {
    Optional<IngredientPrice> findByIngredientIdAndCreatedAt(Long ingredientId, LocalDate date);
}
