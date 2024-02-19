package com.laser.ordermanage.ingredient.repository;

import com.laser.ordermanage.ingredient.domain.Ingredient;
import org.springframework.data.repository.CrudRepository;

public interface IngredientRepository extends CrudRepository<Ingredient, Long>, IngredientRepositoryCustom{
}
