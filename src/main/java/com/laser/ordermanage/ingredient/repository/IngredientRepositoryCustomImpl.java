package com.laser.ordermanage.ingredient.repository;

import com.laser.ordermanage.ingredient.dto.response.*;
import com.laser.ordermanage.ingredient.repository.mapper.IngredientRowMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.laser.ordermanage.factory.domain.QFactory.factory;
import static com.laser.ordermanage.ingredient.domain.QIngredient.ingredient;
import static com.laser.ordermanage.user.domain.QUserEntity.userEntity;

@RequiredArgsConstructor
public class IngredientRepositoryCustomImpl implements IngredientRepositoryCustom{

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final JPAQueryFactory queryFactory;

    @Override
    public List<GetIngredientResponse> findIngredientStatusByFactoryAndDate(String email, LocalDate date) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("email", email)
                .addValue("date", date);

        String getIngredientQuery = """
            SELECT
                ingredient.id AS id,
                ingredient.texture AS texture,
                ingredient.thickness AS thickness,
                ingredient.width AS width,
                ingredient.height AS height,
                ingredient.weight AS weight,
                ISNULL(ingredient.deleted_at) AS isDeleted,
                ingredient_price_data.purchase AS purchase,
                ingredient_price_data.sell AS sell,
                COALESCE(ingredient_previous_stock_data.stock, 0) AS previousDay,
                COALESCE(ingredient_stock_data.incoming, 0) AS incoming,
                COALESCE(ingredient_stock_data.production, 0) AS production,
                COALESCE(ingredient_stock_data.stock, COALESCE(ingredient_previous_stock_data.stock, 0)) AS currentDay,
                COALESCE(ingredient_stock_data.optimal, ingredient_previous_stock_data.optimal) AS optimal
            FROM ingredient
            JOIN factory ON ingredient.factory_id = factory.id
            JOIN user_table on factory.user_id = user_table.id
            JOIN (
                SELECT
                    ranked_data.ingredient_id,
                    ranked_data.purchase,
                    ranked_data.sell
                FROM (
                    SELECT
                        ingredient_price.ingredient_id,
                        ingredient_price.purchase,
                        ingredient_price.sell,
                        ROW_NUMBER() OVER (PARTITION BY ingredient_price.ingredient_id ORDER BY ingredient_price.created_at DESC) AS rn
                    FROM ingredient_price
                    WHERE ingredient_price.created_at <= :date
                ) AS ranked_data
                WHERE ranked_data.rn = 1
            ) AS ingredient_price_data ON ingredient_price_data.ingredient_id = ingredient.id
            LEFT OUTER JOIN (
                SELECT
                    ranked_data.ingredient_id,
                    ranked_data.stock,
                    ranked_data.optimal
                FROM (
                    SELECT
                        ingredient_stock.ingredient_id,
                        ingredient_stock.stock,
                        ingredient_stock.optimal,
                        ROW_NUMBER() OVER (PARTITION BY ingredient_stock.ingredient_id ORDER BY ingredient_stock.created_at DESC) AS rn
                    FROM ingredient_stock
                    WHERE ingredient_stock.created_at <= DATE_SUB(:date, INTERVAL 1 DAY )
                ) AS ranked_data
                WHERE ranked_data.rn = 1
            ) AS ingredient_previous_stock_data ON ingredient_previous_stock_data.ingredient_id = ingredient.id
            LEFT OUTER JOIN (
                SELECT
                    ingredient_stock.ingredient_id,
                    ingredient_stock.incoming,
                    ingredient_stock.production,
                    ingredient_stock.stock,
                    ingredient_stock.optimal
                FROM ingredient_stock
                WHERE ingredient_stock.created_at = :date
            ) AS ingredient_stock_data ON ingredient_stock_data.ingredient_id = ingredient.id
            WHERE
                ingredient.created_at < DATE_ADD(:date, INTERVAL 1 DAY ) AND
                (ISNULL(ingredient.deleted_at) or ingredient.deleted_at >= :date) AND
                user_table.email = :email
            """;

        return jdbcTemplate.query(getIngredientQuery, namedParameters, new IngredientRowMapper());
    }

    @Override
    public Optional<String> findUserEmailById(Long ingredientId) {
        String userEmail = queryFactory
                .select(userEntity.email)
                .from(ingredient)
                .join(ingredient.factory, factory)
                .join(factory.user, userEntity)
                .where(ingredient.id.eq(ingredientId))
                .fetchOne();

        return Optional.ofNullable(userEmail);
    }

    @Override
    public List<GetIngredientInfoResponse> findIngredientByFactory(String email) {
        List<GetIngredientInfoResponse> ingredientInfoResponseList = queryFactory
                .select(new QGetIngredientInfoResponse(
                        ingredient.id,
                        ingredient.texture,
                        ingredient.thickness
                ))
                .from(ingredient)
                .join(ingredient.factory, factory)
                .join(factory.user, userEntity)
                .where(userEntity.email.eq(email))
                .fetch();

        return ingredientInfoResponseList;
    }
}
