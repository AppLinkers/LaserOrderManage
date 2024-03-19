package com.laser.ordermanage.ingredient.repository;

import com.laser.ordermanage.ingredient.domain.IngredientPrice;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface IngredientPriceRepository extends CrudRepository<IngredientPrice, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<IngredientPrice> findByIngredientIdAndCreatedAt(Long ingredientId, LocalDate date);

    IngredientPrice findFirstByIngredientIdOrderByCreatedAtDesc(Long ingredientId);
}
