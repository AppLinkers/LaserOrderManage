package com.laser.ordermanage.ingredient.repository;

import com.laser.ordermanage.ingredient.domain.IngredientStock;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import static com.laser.ordermanage.ingredient.domain.QIngredient.ingredient;
import static com.laser.ordermanage.ingredient.domain.QIngredientStock.ingredientStock;

@RequiredArgsConstructor
public class IngredientStockRepositoryCustomImpl implements IngredientStockRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public IngredientStock findPreviousByIngredientIdAndDate(Long ingredientId, LocalDate date) {
        return queryFactory
                .select(ingredientStock)
                .from(ingredientStock)
                .join(ingredientStock.ingredient, ingredient)
                .where(
                        ingredient.id.eq(ingredientId),
                        ingredientStock.createdAt.before(date)
                )
                .orderBy(ingredientStock.createdAt.desc())
                .fetchFirst();
    }
}
