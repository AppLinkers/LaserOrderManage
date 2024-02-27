package com.laser.ordermanage.ingredient.repository;

import com.laser.ordermanage.ingredient.domain.IngredientStock;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;

import java.time.LocalDate;
import java.util.List;

import static com.laser.ordermanage.ingredient.domain.QIngredient.ingredient;
import static com.laser.ordermanage.ingredient.domain.QIngredientStock.ingredientStock;

@RequiredArgsConstructor
public class IngredientStockRepositoryCustomImpl implements IngredientStockRepositoryCustom{

    private final NamedParameterJdbcTemplate jdbcTemplate;
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

    @Override
    public void saveAll(List<IngredientStock> ingredientStockList) {
        SqlParameterSource[] batchData = SqlParameterSourceUtils.createBatch(ingredientStockList.toArray());

        String saveQuery = """
                INSERT INTO ingredient_stock (ingredient_id, incoming, production, stock, optimal, created_at) 
                VALUES (:ingredient_id, :incoming, :production, :stock, :optimal, NOW())
                """;

        jdbcTemplate.batchUpdate(saveQuery, batchData);
    }
}
