package com.laser.ordermanage.ingredient.repository;

import com.laser.ordermanage.ingredient.domain.IngredientStock;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface IngredientStockRepository extends CrudRepository<IngredientStock, Long>, IngredientStockRepositoryCustom {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<IngredientStock> findByIngredientIdAndCreatedAt(Long ingredientId, LocalDate date);

    IngredientStock findFirstByIngredientIdOrderByCreatedAtDesc(Long ingredientId);
}
