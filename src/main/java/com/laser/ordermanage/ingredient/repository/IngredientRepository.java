package com.laser.ordermanage.ingredient.repository;

import com.laser.ordermanage.ingredient.domain.Ingredient;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface IngredientRepository extends CrudRepository<Ingredient, Long>, IngredientRepositoryCustom{
    Optional<Ingredient> findFirstById(Long ingredientId);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT i FROM Ingredient i where i.id = :ingredientId")
    Optional<Ingredient> findFirstByIdForUpdate(Long ingredientId);

    List<Ingredient> findByDeletedAtIsNull();
}
