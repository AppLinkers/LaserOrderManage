package com.laser.ordermanage.ingredient.repository;

import com.laser.ordermanage.ingredient.domain.Ingredient;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IngredientRepository extends CrudRepository<Ingredient, Long>, IngredientRepositoryCustom{
    Optional<Ingredient> findFirstById(Long ingredientId);
}
