package com.laser.ordermanage.ingredient.repository;

import com.laser.ordermanage.ingredient.domain.IngredientPrice;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;

import java.util.List;

@RequiredArgsConstructor
public class IngredientPriceRepositoryCustomImpl implements IngredientPriceRepositoryCustom{

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public void saveAll(List<IngredientPrice> ingredientPriceList) {
        SqlParameterSource[] batchData = SqlParameterSourceUtils.createBatch(ingredientPriceList.toArray());

        String saveQuery = """
                INSERT INTO ingredient_price (ingredient_id, purchase, sell, created_at) 
                VALUES (:ingredient_id, :purchase, :sell, NOW())
                """;

        jdbcTemplate.batchUpdate(saveQuery, batchData);
    }
}
