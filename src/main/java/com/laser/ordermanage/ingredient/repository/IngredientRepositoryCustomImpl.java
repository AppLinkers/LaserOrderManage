package com.laser.ordermanage.ingredient.repository;

import com.laser.ordermanage.ingredient.domain.type.IngredientPriceType;
import com.laser.ordermanage.ingredient.domain.type.IngredientStockType;
import com.laser.ordermanage.ingredient.dto.response.GetIngredientAnalysisItemResponse;
import com.laser.ordermanage.ingredient.dto.response.GetIngredientInfoResponse;
import com.laser.ordermanage.ingredient.dto.response.GetIngredientResponse;
import com.laser.ordermanage.ingredient.dto.response.QGetIngredientInfoResponse;
import com.laser.ordermanage.ingredient.repository.mapper.IngredientRowMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.time.LocalDate;
import java.util.*;

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

        String findIngredientQuery = """
            SELECT
                ingredient.id AS id,
                ingredient.texture AS texture,
                ingredient.thickness AS thickness,
                ingredient.width AS width,
                ingredient.height AS height,
                ingredient.weight AS weight,
                NOT ISNULL(ingredient.deleted_at) AS isDeleted,
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

        return jdbcTemplate.query(findIngredientQuery, namedParameters, new IngredientRowMapper());
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

    @Override
    public List<GetIngredientAnalysisItemResponse> findIngredientAnalysisAsTotalAndMonthAndStockByFactory(String email, LocalDate startDate, LocalDate endDate, List<IngredientStockType> stockTypeList, String stockUnit) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("email", email)
                .addValue("startDate", startDate)
                .addValue("endDate", endDate);

        String findIngredientAnalysisQuery = """
                SELECT
                    date.yearmonth,
                    ingredient_stock_data.incoming,
                    ingredient_stock_data.production,
                    ingredient_stock_data.stock,
                    ingredient_stock_data.optimal
                FROM
                    (
                        WITH RECURSIVE T_TEMP_DATES AS (
                           SELECT :startDate AS DT
                        UNION
                           SELECT DATE_ADD(T_TEMP_DATES.DT, INTERVAL 1 MONTH)
                           FROM T_TEMP_DATES
                           WHERE DATE_ADD(T_TEMP_DATES.DT, INTERVAL 1 MONTH) <= :endDate
                        )
                        SELECT DATE_FORMAT(DT, '%Y-%m') AS yearmonth FROM T_TEMP_DATES
                    )  AS date LEFT OUTER JOIN (
                        SELECT
                            ingredient_stock_data_1.yearmonth AS yearmonth,
                            SUM(ingredient_stock_data_1.incoming) AS incoming,
                            SUM(ingredient_stock_data_1.production) AS production,
                            SUM(ingredient_stock_data_2.stock) AS stock,
                            SUM(ingredient_stock_data_2.optimal) AS optimal
                        FROM
                            (
                                SELECT
                                    ingredient_stock.ingredient_id,
                                    DATE_FORMAT(ingredient_stock.created_at, '%Y-%m') AS yearmonth,
                """ +
                (stockUnit.equals("count") ? """
                                    SUM(ingredient_stock.incoming) AS incoming,
                                    SUM(ingredient_stock.production) AS production
                """ :
                """
                                    ROUND(CAST(SUM(ingredient_stock.incoming) AS DECIMAL(10, 2)) * CAST(ingredient.weight AS DECIMAL (10, 2)), 2) AS incoming,
                                    ROUND(CAST(SUM(ingredient_stock.production) AS DECIMAL(10, 2)) * CAST(ingredient.weight AS DECIMAL(10, 2)), 2) AS production
                """) +
                """
                                FROM ingredient_stock
                                JOIN ingredient ON ingredient.id = ingredient_stock.ingredient_id
                                JOIN factory ON factory.id = ingredient.factory_id
                                JOIN user_table ON user_table.id = factory.user_id
                                WHERE
                                    ingredient_stock.created_at >= :startDate AND ingredient_stock.created_at < DATE_ADD(:endDate, INTERVAL 1 MONTH ) AND
                                    user_table.email = :email
                                GROUP BY ingredient_stock.ingredient_id, yearmonth
                            ) AS ingredient_stock_data_1
                            JOIN
                            (
                                SELECT
                                    ranked_data.ingredient_id as ingredient_id,
                                    ranked_data.yearmonth,
                                    ranked_data.stock as stock,
                                    ranked_data.optimal as optimal
                                FROM (
                                    SELECT
                                        ingredient_stock.ingredient_id,
                                        DATE_FORMAT(ingredient_stock.created_at, '%Y-%m') AS yearmonth,
                """ +
                (stockUnit.equals("count") ? """
                                        ingredient_stock.stock,
                                        ingredient_stock.optimal,
                """ :
                """
                                        ROUND(CAST(ingredient_stock.stock AS DECIMAL(10, 2)) * CAST(ingredient.weight AS DECIMAL(10, 2)), 2) AS stock,
                                        ROUND(CAST(ingredient_stock.optimal AS DECIMAL(10, 2)) * CAST(ingredient.weight AS DECIMAL(10, 2)), 2) AS optimal,
                """) +
                """
                                        ROW_NUMBER() over (PARTITION BY ingredient_stock.ingredient_id, DATE_FORMAT(ingredient_stock.created_at, '%Y-%m') ORDER BY ingredient_stock.created_at DESC) AS rn
                                    FROM ingredient_stock
                                    JOIN ingredient ON ingredient.id = ingredient_stock.ingredient_id
                                    JOIN factory ON factory.id = ingredient.factory_id
                                    JOIN user_table ON user_table.id = factory.user_id
                                    WHERE
                                        ingredient_stock.created_at >= :startDate AND ingredient_stock.created_at < DATE_ADD(:endDate, INTERVAL 1 MONTH ) AND
                                        user_table.email = :email
                                ) AS ranked_data
                                WHERE ranked_data.rn = 1
                            ) AS ingredient_stock_data_2
                            ON (ingredient_stock_data_1.ingredient_id = ingredient_stock_data_2.ingredient_id and ingredient_stock_data_1.yearmonth = ingredient_stock_data_2.yearmonth)
                        GROUP BY ingredient_stock_data_1.yearmonth
                    ) AS ingredient_stock_data
                ON (date.yearmonth = ingredient_stock_data.yearmonth)
                ORDER BY date.yearmonth
                """;

        ColumnMapRowMapper rowMapper = new ColumnMapRowMapper();
        List<Map<String, Object>> ingredientAnalysisDataList = jdbcTemplate.query(findIngredientAnalysisQuery, namedParameters, rowMapper);

        Map<String, List<Number>> ingredientAnalysisDataMap = new HashMap<>();
        for (IngredientStockType ingredientStockType : stockTypeList) {
            ingredientAnalysisDataMap.put(ingredientStockType.getRequest(), new ArrayList<>());
        }

        for(Map<String, Object> ingredientAnalysisData: ingredientAnalysisDataList){
            for (IngredientStockType ingredientStockType : stockTypeList) {
                ingredientAnalysisDataMap.get(ingredientStockType.getRequest()).add((Number) ingredientAnalysisData.get(ingredientStockType.getRequest()));
            }
        }

        List<GetIngredientAnalysisItemResponse> getIngredientAnalysisItemResponseList = new ArrayList<>();
        for (Map.Entry<String, List<Number>> entry : ingredientAnalysisDataMap.entrySet()) {
            getIngredientAnalysisItemResponseList.add(GetIngredientAnalysisItemResponse.builder()
                    .item(entry.getKey())
                    .data(entry.getValue())
                    .build());
        }

        return getIngredientAnalysisItemResponseList;
    }

    @Override
    public List<GetIngredientAnalysisItemResponse> findIngredientAnalysisAsTotalAndMonthAndPriceByFactory(String email, LocalDate startDate, LocalDate endDate, List<IngredientPriceType> priceTypeList) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("email", email)
                .addValue("startDate", startDate)
                .addValue("endDate", endDate);

        String findIngredientAnalysisQuery = """
                SELECT
                    date.yearmonth,
                    ingredient_price_data.purchase,
                    ingredient_price_data.sell
                FROM
                    (
                        WITH RECURSIVE T_TEMP_DATES AS (
                           SELECT :startDate AS DT
                        UNION
                           SELECT DATE_ADD(T_TEMP_DATES.DT, INTERVAL 1 MONTH)
                           FROM T_TEMP_DATES
                           WHERE DATE_ADD(T_TEMP_DATES.DT, INTERVAL 1 MONTH) <= :endDate
                        )
                        SELECT DT, DATE_FORMAT(DT, '%Y-%m') AS yearmonth FROM T_TEMP_DATES
                    )  AS date LEFT OUTER JOIN (
                        SELECT
                            ranked_data.yearmonth,
                            SUM(ranked_data.purchase) as purchase,
                            SUM(ranked_data.sell) as sell
                        FROM (
                            SELECT
                                DATE_FORMAT(ingredient_price.created_at, '%Y-%m') AS yearmonth,
                                ingredient_price.purchase,
                                ingredient_price.sell,
                                ROW_NUMBER() over (PARTITION BY ingredient_price.ingredient_id, DATE_FORMAT(ingredient_price.created_at, '%Y-%m') ORDER BY ingredient_price.created_at DESC) AS rn
                            FROM ingredient_price
                            JOIN ingredient ON ingredient.id = ingredient_price.ingredient_id
                            JOIN factory ON factory.id = ingredient.factory_id
                            JOIN user_table ON user_table.id = factory.user_id
                            WHERE
                                ingredient_price.created_at >= :startDate AND ingredient_price.created_at < DATE_ADD(:endDate, INTERVAL 1 MONTH ) AND
                                user_table.email = :email
                        ) AS ranked_data
                        WHERE ranked_data.rn = 1
                        GROUP BY yearmonth) AS ingredient_price_data
                ON (date.yearmonth = ingredient_price_data.yearmonth)
                ORDER BY date.yearmonth
                """;

        ColumnMapRowMapper rowMapper = new ColumnMapRowMapper();
        List<Map<String, Object>> ingredientAnalysisDataList = jdbcTemplate.query(findIngredientAnalysisQuery, namedParameters, rowMapper);

        Map<String, List<Number>> ingredientAnalysisDataMap = new HashMap<>();
        for (IngredientPriceType ingredientPriceType : priceTypeList) {
            ingredientAnalysisDataMap.put(ingredientPriceType.getRequest(), new ArrayList<>());
        }

        for(Map<String, Object> ingredientAnalysisData: ingredientAnalysisDataList){
            for (IngredientPriceType ingredientPriceType : priceTypeList) {
                ingredientAnalysisDataMap.get(ingredientPriceType.getRequest()).add((Number) ingredientAnalysisData.get(ingredientPriceType.getRequest()));
            }
        }

        List<GetIngredientAnalysisItemResponse> getIngredientAnalysisItemResponseList = new ArrayList<>();
        for (Map.Entry<String, List<Number>> entry : ingredientAnalysisDataMap.entrySet()) {
            getIngredientAnalysisItemResponseList.add(GetIngredientAnalysisItemResponse.builder()
                    .item(entry.getKey())
                    .data(entry.getValue())
                    .build());
        }

        return getIngredientAnalysisItemResponseList;
    }

    @Override
    public List<GetIngredientAnalysisItemResponse> findIngredientAnalysisAsTotalAndYearAndStockByFactory(String email, LocalDate startDate, LocalDate endDate, List<IngredientStockType> stockTypeList, String stockUnit) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("email", email)
                .addValue("startDate", startDate)
                .addValue("endDate", endDate);

        String findIngredientAnalysisQuery = """
                SELECT
                    date.year,
                    ingredient_stock_data.incoming,
                    ingredient_stock_data.production,
                    ingredient_stock_data.stock,
                    ingredient_stock_data.optimal
                FROM
                    (
                        WITH RECURSIVE T_TEMP_DATES AS (
                           SELECT :startDate AS DT
                        UNION
                           SELECT DATE_ADD(T_TEMP_DATES.DT, INTERVAL 1 YEAR)
                           FROM T_TEMP_DATES
                           WHERE DATE_ADD(T_TEMP_DATES.DT, INTERVAL 1 YEAR) <= :endDate
                        )
                        SELECT DT, DATE_FORMAT(DT, '%Y') AS year FROM T_TEMP_DATES
                    )  AS date LEFT OUTER JOIN (
                        SELECT
                            ingredient_stock_data_1.year AS year,
                            SUM(ingredient_stock_data_1.incoming) AS incoming,
                            SUM(ingredient_stock_data_1.production) AS production,
                            SUM(ingredient_stock_data_2.stock) AS stock,
                            SUM(ingredient_stock_data_2.optimal) AS optimal
                        FROM
                            (
                                SELECT
                                    ingredient_stock.ingredient_id,
                                    DATE_FORMAT(ingredient_stock.created_at, '%Y') AS year,
                """ +
                (stockUnit.equals("count") ? """
                                    SUM(ingredient_stock.incoming) AS incoming,
                                    SUM(ingredient_stock.production) AS production
                """ :
                """
                                    ROUND(CAST(SUM(ingredient_stock.incoming) AS DECIMAL(10, 2)) * CAST(ingredient.weight AS DECIMAL (10, 2)), 2) AS incoming,
                                    ROUND(CAST(SUM(ingredient_stock.production) AS DECIMAL(10, 2)) * CAST(ingredient.weight AS DECIMAL(10, 2)), 2) AS production
                """) +
                """
                                FROM ingredient_stock
                                JOIN ingredient ON ingredient.id = ingredient_stock.ingredient_id
                                JOIN factory ON factory.id = ingredient.factory_id
                                JOIN user_table ON user_table.id = factory.user_id
                                WHERE
                                    ingredient_stock.created_at >= :startDate AND ingredient_stock.created_at < DATE_ADD(:endDate, INTERVAL 1 YEAR ) AND
                                    user_table.email = :email
                                GROUP BY ingredient_stock.ingredient_id, year
                            ) AS ingredient_stock_data_1
                            JOIN
                            (
                                SELECT
                                    ranked_data.ingredient_id as ingredient_id,
                                    ranked_data.year,
                                    ranked_data.stock as stock,
                                    ranked_data.optimal as optimal
                                FROM (
                                    SELECT
                                        ingredient_stock.ingredient_id,
                                        DATE_FORMAT(ingredient_stock.created_at, '%Y') AS year,
                """ +
                (stockUnit.equals("count") ? """
                                        ingredient_stock.stock,
                                        ingredient_stock.optimal,
                """ :
                """
                                        ROUND(CAST(ingredient_stock.stock AS DECIMAL(10, 2)) * CAST(ingredient.weight AS DECIMAL(10, 2)), 2) AS stock,
                                        ROUND(CAST(ingredient_stock.optimal AS DECIMAL(10, 2)) * CAST(ingredient.weight AS DECIMAL(10, 2)), 2) AS optimal,
                """) +
                """
                                        ROW_NUMBER() over (PARTITION BY ingredient_stock.ingredient_id, DATE_FORMAT(ingredient_stock.created_at, '%Y') ORDER BY ingredient_stock.created_at DESC) AS rn
                                    FROM ingredient_stock
                                    JOIN ingredient ON ingredient.id = ingredient_stock.ingredient_id
                                    JOIN factory ON factory.id = ingredient.factory_id
                                    JOIN user_table ON user_table.id = factory.user_id
                                    WHERE
                                        ingredient_stock.created_at >= :startDate AND ingredient_stock.created_at < DATE_ADD(:endDate, INTERVAL 1 YEAR ) AND
                                        user_table.email = :email
                                ) AS ranked_data
                                WHERE ranked_data.rn = 1
                            ) AS ingredient_stock_data_2
                            ON (ingredient_stock_data_1.ingredient_id = ingredient_stock_data_2.ingredient_id and ingredient_stock_data_1.year = ingredient_stock_data_2.year)
                        GROUP BY ingredient_stock_data_1.year
                    ) AS ingredient_stock_data
                ON (date.year = ingredient_stock_data.year)
                ORDER BY date.year
                """;

        ColumnMapRowMapper rowMapper = new ColumnMapRowMapper();
        List<Map<String, Object>> ingredientAnalysisDataList = jdbcTemplate.query(findIngredientAnalysisQuery, namedParameters, rowMapper);

        Map<String, List<Number>> ingredientAnalysisDataMap = new HashMap<>();
        for (IngredientStockType ingredientStockType : stockTypeList) {
            ingredientAnalysisDataMap.put(ingredientStockType.getRequest(), new ArrayList<>());
        }

        for(Map<String, Object> ingredientAnalysisData: ingredientAnalysisDataList){
            for (IngredientStockType ingredientStockType : stockTypeList) {
                ingredientAnalysisDataMap.get(ingredientStockType.getRequest()).add((Number) ingredientAnalysisData.get(ingredientStockType.getRequest()));
            }
        }

        List<GetIngredientAnalysisItemResponse> getIngredientAnalysisItemResponseList = new ArrayList<>();
        for (Map.Entry<String, List<Number>> entry : ingredientAnalysisDataMap.entrySet()) {
            getIngredientAnalysisItemResponseList.add(GetIngredientAnalysisItemResponse.builder()
                    .item(entry.getKey())
                    .data(entry.getValue())
                    .build());
        }

        return getIngredientAnalysisItemResponseList;
    }

    @Override
    public List<GetIngredientAnalysisItemResponse> findIngredientAnalysisAsTotalAndYearAndPriceByFactory(String email, LocalDate startDate, LocalDate endDate, List<IngredientPriceType> priceTypeList) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("email", email)
                .addValue("startDate", startDate)
                .addValue("endDate", endDate);

        String findIngredientAnalysisQuery = """
                SELECT
                   date.year,
                   ingredient_price_data.purchase,
                   ingredient_price_data.sell
                FROM
                   (
                       WITH RECURSIVE T_TEMP_DATES AS (
                          SELECT :startDate AS DT
                       UNION
                          SELECT DATE_ADD(T_TEMP_DATES.DT, INTERVAL 1 YEAR)
                          FROM T_TEMP_DATES
                          WHERE DATE_ADD(T_TEMP_DATES.DT, INTERVAL 1 YEAR) <= :endDate
                       )
                       SELECT DT, DATE_FORMAT(DT, '%Y') AS year FROM T_TEMP_DATES
                   )  AS date LEFT OUTER JOIN (
                       SELECT
                           ranked_data.year,
                           SUM(ranked_data.purchase) as purchase,
                           SUM(ranked_data.sell) as sell
                       FROM (
                           SELECT
                               DATE_FORMAT(ingredient_price.created_at, '%Y') as year,
                               ingredient_price.purchase,
                               ingredient_price.sell,
                               ROW_NUMBER() over (PARTITION BY ingredient_price.ingredient_id, DATE_FORMAT(ingredient_price.created_at, '%Y') ORDER BY ingredient_price.created_at DESC) AS rn
                           FROM ingredient_price
                           JOIN ingredient ON ingredient.id = ingredient_price.ingredient_id
                           JOIN factory ON factory.id = ingredient.factory_id
                           JOIN user_table ON user_table.id = factory.user_id
                           WHERE
                               ingredient_price.created_at >= :startDate AND ingredient_price.created_at < DATE_ADD(:endDate, INTERVAL 1 YEAR ) AND
                               user_table.email = :email
                       ) AS ranked_data
                       WHERE ranked_data.rn = 1
                       GROUP BY year) AS ingredient_price_data
                ON (date.year = ingredient_price_data.year)
                ORDER BY date.year
                """;

        ColumnMapRowMapper rowMapper = new ColumnMapRowMapper();
        List<Map<String, Object>> ingredientAnalysisDataList = jdbcTemplate.query(findIngredientAnalysisQuery, namedParameters, rowMapper);

        Map<String, List<Number>> ingredientAnalysisDataMap = new HashMap<>();
        for (IngredientPriceType ingredientPriceType : priceTypeList) {
            ingredientAnalysisDataMap.put(ingredientPriceType.getRequest(), new ArrayList<>());
        }

        for(Map<String, Object> ingredientAnalysisData: ingredientAnalysisDataList){
            for (IngredientPriceType ingredientPriceType : priceTypeList) {
                ingredientAnalysisDataMap.get(ingredientPriceType.getRequest()).add((Number) ingredientAnalysisData.get(ingredientPriceType.getRequest()));
            }
        }

        List<GetIngredientAnalysisItemResponse> getIngredientAnalysisItemResponseList = new ArrayList<>();
        for (Map.Entry<String, List<Number>> entry : ingredientAnalysisDataMap.entrySet()) {
            getIngredientAnalysisItemResponseList.add(GetIngredientAnalysisItemResponse.builder()
                    .item(entry.getKey())
                    .data(entry.getValue())
                    .build());
        }

        return getIngredientAnalysisItemResponseList;
    }

    @Override
    public List<GetIngredientAnalysisItemResponse> findIngredientAnalysisAsAverageAndMonthAndStockByFactory(String email, LocalDate startDate, LocalDate endDate, List<IngredientStockType> stockTypeList, String stockUnit) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("email", email)
                .addValue("startDate", startDate)
                .addValue("endDate", endDate);

        String findIngredientAnalysisQuery = """
                SELECT
                    date.yearmonth,
                    ingredient_stock_data.incoming,
                    ingredient_stock_data.production,
                    ingredient_stock_data.stock,
                    ingredient_stock_data.optimal
                FROM
                    (
                        WITH RECURSIVE T_TEMP_DATES AS (
                           SELECT :startDate AS DT
                        UNION
                           SELECT DATE_ADD(T_TEMP_DATES.DT, INTERVAL 1 MONTH)
                           FROM T_TEMP_DATES
                           WHERE DATE_ADD(T_TEMP_DATES.DT, INTERVAL 1 MONTH) <= :endDate
                        )
                        SELECT DATE_FORMAT(DT, '%Y-%m') AS yearmonth FROM T_TEMP_DATES
                    )  AS date LEFT OUTER JOIN (
                        SELECT
                            ingredient_stock_data_1.yearmonth AS yearmonth,
                            ROUND(AVG(ingredient_stock_data_1.incoming), 2) AS incoming,
                            ROUND(AVG(ingredient_stock_data_1.production), 2) AS production,
                            ROUND(AVG(ingredient_stock_data_2.stock), 2) AS stock,
                            ROUND(AVG(ingredient_stock_data_2.optimal), 2) AS optimal
                        FROM
                            (
                                SELECT
                                    ingredient_stock.ingredient_id,
                                    DATE_FORMAT(ingredient_stock.created_at, '%Y-%m') AS yearmonth,
                """ +
                (stockUnit.equals("count") ? """
                                    SUM(ingredient_stock.incoming) AS incoming,
                                    SUM(ingredient_stock.production) AS production
                """ :
                """
                                    ROUND(CAST(SUM(ingredient_stock.incoming) AS DECIMAL(10, 2)) * CAST(ingredient.weight AS DECIMAL (10, 2)), 2) AS incoming,
                                    ROUND(CAST(SUM(ingredient_stock.production) AS DECIMAL(10, 2)) * CAST(ingredient.weight AS DECIMAL(10, 2)), 2) AS production
                """) +
                """
                                FROM ingredient_stock
                                JOIN ingredient ON ingredient.id = ingredient_stock.ingredient_id
                                JOIN factory ON factory.id = ingredient.factory_id
                                JOIN user_table ON user_table.id = factory.user_id
                                WHERE
                                    ingredient_stock.created_at >= :startDate AND ingredient_stock.created_at < DATE_ADD(:endDate, INTERVAL 1 MONTH ) AND
                                    user_table.email = :email
                                GROUP BY ingredient_stock.ingredient_id, yearmonth
                            ) AS ingredient_stock_data_1
                            JOIN
                            (
                                SELECT
                                    ranked_data.ingredient_id as ingredient_id,
                                    ranked_data.yearmonth,
                                    ranked_data.stock as stock,
                                    ranked_data.optimal as optimal
                                FROM (
                                    SELECT
                                        ingredient_stock.ingredient_id,
                                        DATE_FORMAT(ingredient_stock.created_at, '%Y-%m') AS yearmonth,
                """ +
                (stockUnit.equals("count") ? """
                                        ingredient_stock.stock,
                                        ingredient_stock.optimal,
                """ :
                """
                                        ROUND(CAST(ingredient_stock.stock AS DECIMAL(10, 2)) * CAST(ingredient.weight AS DECIMAL(10, 2)), 2) AS stock,
                                        ROUND(CAST(ingredient_stock.optimal AS DECIMAL(10, 2)) * CAST(ingredient.weight AS DECIMAL(10, 2)), 2) AS optimal,
                """) +
                """
                                        ROW_NUMBER() over (PARTITION BY ingredient_stock.ingredient_id, DATE_FORMAT(ingredient_stock.created_at, '%Y-%m') ORDER BY ingredient_stock.created_at DESC) AS rn
                                    FROM ingredient_stock
                                    JOIN ingredient ON ingredient.id = ingredient_stock.ingredient_id
                                    JOIN factory ON factory.id = ingredient.factory_id
                                    JOIN user_table ON user_table.id = factory.user_id
                                    WHERE
                                        ingredient_stock.created_at >= :startDate AND ingredient_stock.created_at < DATE_ADD(:endDate, INTERVAL 1 MONTH ) AND
                                        user_table.email = :email
                                ) AS ranked_data
                                WHERE ranked_data.rn = 1
                            ) AS ingredient_stock_data_2
                            ON (ingredient_stock_data_1.ingredient_id = ingredient_stock_data_2.ingredient_id and ingredient_stock_data_1.yearmonth = ingredient_stock_data_2.yearmonth)
                        GROUP BY ingredient_stock_data_1.yearmonth
                    ) AS ingredient_stock_data
                ON (date.yearmonth = ingredient_stock_data.yearmonth)
                ORDER BY date.yearmonth
                """;

        ColumnMapRowMapper rowMapper = new ColumnMapRowMapper();
        List<Map<String, Object>> ingredientAnalysisDataList = jdbcTemplate.query(findIngredientAnalysisQuery, namedParameters, rowMapper);

        Map<String, List<Number>> ingredientAnalysisDataMap = new HashMap<>();
        for (IngredientStockType ingredientStockType : stockTypeList) {
            ingredientAnalysisDataMap.put(ingredientStockType.getRequest(), new ArrayList<>());
        }

        for(Map<String, Object> ingredientAnalysisData: ingredientAnalysisDataList){
            for (IngredientStockType ingredientStockType : stockTypeList) {
                ingredientAnalysisDataMap.get(ingredientStockType.getRequest()).add((Number) ingredientAnalysisData.get(ingredientStockType.getRequest()));
            }
        }

        List<GetIngredientAnalysisItemResponse> getIngredientAnalysisItemResponseList = new ArrayList<>();
        for (Map.Entry<String, List<Number>> entry : ingredientAnalysisDataMap.entrySet()) {
            getIngredientAnalysisItemResponseList.add(GetIngredientAnalysisItemResponse.builder()
                    .item(entry.getKey())
                    .data(entry.getValue())
                    .build());
        }

        return getIngredientAnalysisItemResponseList;
    }

    @Override
    public List<GetIngredientAnalysisItemResponse> findIngredientAnalysisAsAverageAndMonthAndPriceByFactory(String email, LocalDate startDate, LocalDate endDate, List<IngredientPriceType> priceTypeList) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("email", email)
                .addValue("startDate", startDate)
                .addValue("endDate", endDate);

        String findIngredientAnalysisQuery = """
                SELECT
                    date.yearmonth,
                    ingredient_price_data.purchase,
                    ingredient_price_data.sell
                FROM
                    (
                        WITH RECURSIVE T_TEMP_DATES AS (
                           SELECT :startDate AS DT
                        UNION
                           SELECT DATE_ADD(T_TEMP_DATES.DT, INTERVAL 1 MONTH)
                           FROM T_TEMP_DATES
                           WHERE DATE_ADD(T_TEMP_DATES.DT, INTERVAL 1 MONTH) <= :endDate
                        )
                        SELECT DT, DATE_FORMAT(DT, '%Y-%m') AS yearmonth FROM T_TEMP_DATES
                    )  AS date LEFT OUTER JOIN (
                        SELECT
                            ranked_data.yearmonth,
                            ROUND(AVG(ranked_data.purchase), 2) as purchase,
                            ROUND(AVG(ranked_data.sell), 2) as sell
                        FROM (
                            SELECT
                                DATE_FORMAT(ingredient_price.created_at, '%Y-%m') AS yearmonth,
                                ingredient_price.purchase,
                                ingredient_price.sell,
                                ROW_NUMBER() over (PARTITION BY ingredient_price.ingredient_id, DATE_FORMAT(ingredient_price.created_at, '%Y-%m') ORDER BY ingredient_price.created_at DESC) AS rn
                            FROM ingredient_price
                            JOIN ingredient ON ingredient.id = ingredient_price.ingredient_id
                            JOIN factory ON factory.id = ingredient.factory_id
                            JOIN user_table ON user_table.id = factory.user_id
                            WHERE
                                ingredient_price.created_at >= :startDate AND ingredient_price.created_at < DATE_ADD(:endDate, INTERVAL 1 MONTH ) AND
                                user_table.email = :email
                        ) AS ranked_data
                        WHERE ranked_data.rn = 1
                        GROUP BY yearmonth) AS ingredient_price_data
                ON (date.yearmonth = ingredient_price_data.yearmonth)
                ORDER BY date.yearmonth
                """;

        ColumnMapRowMapper rowMapper = new ColumnMapRowMapper();
        List<Map<String, Object>> ingredientAnalysisDataList = jdbcTemplate.query(findIngredientAnalysisQuery, namedParameters, rowMapper);

        Map<String, List<Number>> ingredientAnalysisDataMap = new HashMap<>();
        for (IngredientPriceType ingredientPriceType : priceTypeList) {
            ingredientAnalysisDataMap.put(ingredientPriceType.getRequest(), new ArrayList<>());
        }

        for(Map<String, Object> ingredientAnalysisData: ingredientAnalysisDataList){
            for (IngredientPriceType ingredientPriceType : priceTypeList) {
                ingredientAnalysisDataMap.get(ingredientPriceType.getRequest()).add((Number) ingredientAnalysisData.get(ingredientPriceType.getRequest()));
            }
        }

        List<GetIngredientAnalysisItemResponse> getIngredientAnalysisItemResponseList = new ArrayList<>();
        for (Map.Entry<String, List<Number>> entry : ingredientAnalysisDataMap.entrySet()) {
            getIngredientAnalysisItemResponseList.add(GetIngredientAnalysisItemResponse.builder()
                    .item(entry.getKey())
                    .data(entry.getValue())
                    .build());
        }

        return getIngredientAnalysisItemResponseList;
    }

    @Override
    public List<GetIngredientAnalysisItemResponse> findIngredientAnalysisAsAverageAndYearAndStockByFactory(String email, LocalDate startDate, LocalDate endDate, List<IngredientStockType> stockTypeList, String stockUnit) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("email", email)
                .addValue("startDate", startDate)
                .addValue("endDate", endDate);

        String findIngredientAnalysisQuery = """
                SELECT
                    date.year,
                    ingredient_stock_data.incoming,
                    ingredient_stock_data.production,
                    ingredient_stock_data.stock,
                    ingredient_stock_data.optimal
                FROM
                    (
                        WITH RECURSIVE T_TEMP_DATES AS (
                           SELECT :startDate AS DT
                        UNION
                           SELECT DATE_ADD(T_TEMP_DATES.DT, INTERVAL 1 YEAR)
                           FROM T_TEMP_DATES
                           WHERE DATE_ADD(T_TEMP_DATES.DT, INTERVAL 1 YEAR) <= :endDate
                        )
                        SELECT DT, DATE_FORMAT(DT, '%Y') AS year FROM T_TEMP_DATES
                    )  AS date LEFT OUTER JOIN (
                        SELECT
                            ingredient_stock_data_1.year AS year,
                            ROUND(AVG(ingredient_stock_data_1.incoming), 2) AS incoming,
                            ROUND(AVG(ingredient_stock_data_1.production), 2) AS production,
                            ROUND(AVG(ingredient_stock_data_2.stock), 2) AS stock,
                            ROUND(AVG(ingredient_stock_data_2.optimal), 2) AS optimal
                        FROM
                            (
                                SELECT
                                    ingredient_stock.ingredient_id,
                                    DATE_FORMAT(ingredient_stock.created_at, '%Y') AS year,
                """ +
                (stockUnit.equals("count") ? """
                                    SUM(ingredient_stock.incoming) AS incoming,
                                    SUM(ingredient_stock.production) AS production
                """ :
                        """
                                            ROUND(CAST(SUM(ingredient_stock.incoming) AS DECIMAL(10, 2)) * CAST(ingredient.weight AS DECIMAL (10, 2)), 2) AS incoming,
                                            ROUND(CAST(SUM(ingredient_stock.production) AS DECIMAL(10, 2)) * CAST(ingredient.weight AS DECIMAL(10, 2)), 2) AS production
                        """) +
                """
                                FROM ingredient_stock
                                JOIN ingredient ON ingredient.id = ingredient_stock.ingredient_id
                                JOIN factory ON factory.id = ingredient.factory_id
                                JOIN user_table ON user_table.id = factory.user_id
                                WHERE
                                    ingredient_stock.created_at >= :startDate AND ingredient_stock.created_at < DATE_ADD(:endDate, INTERVAL 1 YEAR ) AND
                                    user_table.email = :email
                                GROUP BY ingredient_stock.ingredient_id, year
                            ) AS ingredient_stock_data_1
                            JOIN
                            (
                                SELECT
                                    ranked_data.ingredient_id as ingredient_id,
                                    ranked_data.year,
                                    ranked_data.stock as stock,
                                    ranked_data.optimal as optimal
                                FROM (
                                    SELECT
                                        ingredient_stock.ingredient_id,
                                        DATE_FORMAT(ingredient_stock.created_at, '%Y') AS year,
                """ +
                (stockUnit.equals("count") ? """
                                        ingredient_stock.stock,
                                        ingredient_stock.optimal,
                """ :
                """
                                        ROUND(CAST(ingredient_stock.stock AS DECIMAL(10, 2)) * CAST(ingredient.weight AS DECIMAL(10, 2)), 2) AS stock,
                                        ROUND(CAST(ingredient_stock.optimal AS DECIMAL(10, 2)) * CAST(ingredient.weight AS DECIMAL(10, 2)), 2) AS optimal,
                """) +
                """
                                        ROW_NUMBER() over (PARTITION BY ingredient_stock.ingredient_id, DATE_FORMAT(ingredient_stock.created_at, '%Y') ORDER BY ingredient_stock.created_at DESC) AS rn
                                    FROM ingredient_stock
                                    JOIN ingredient ON ingredient.id = ingredient_stock.ingredient_id
                                    JOIN factory ON factory.id = ingredient.factory_id
                                    JOIN user_table ON user_table.id = factory.user_id
                                    WHERE
                                        ingredient_stock.created_at >= :startDate AND ingredient_stock.created_at < DATE_ADD(:endDate, INTERVAL 1 YEAR ) AND
                                        user_table.email = :email
                                ) AS ranked_data
                                WHERE ranked_data.rn = 1
                            ) AS ingredient_stock_data_2
                            ON (ingredient_stock_data_1.ingredient_id = ingredient_stock_data_2.ingredient_id and ingredient_stock_data_1.year = ingredient_stock_data_2.year)
                        GROUP BY ingredient_stock_data_1.year
                    ) AS ingredient_stock_data
                ON (date.year = ingredient_stock_data.year)
                ORDER BY date.year
                """;

        ColumnMapRowMapper rowMapper = new ColumnMapRowMapper();
        List<Map<String, Object>> ingredientAnalysisDataList = jdbcTemplate.query(findIngredientAnalysisQuery, namedParameters, rowMapper);

        Map<String, List<Number>> ingredientAnalysisDataMap = new HashMap<>();
        for (IngredientStockType ingredientStockType : stockTypeList) {
            ingredientAnalysisDataMap.put(ingredientStockType.getRequest(), new ArrayList<>());
        }

        for(Map<String, Object> ingredientAnalysisData: ingredientAnalysisDataList){
            for (IngredientStockType ingredientStockType : stockTypeList) {
                ingredientAnalysisDataMap.get(ingredientStockType.getRequest()).add((Number) ingredientAnalysisData.get(ingredientStockType.getRequest()));
            }
        }

        List<GetIngredientAnalysisItemResponse> getIngredientAnalysisItemResponseList = new ArrayList<>();
        for (Map.Entry<String, List<Number>> entry : ingredientAnalysisDataMap.entrySet()) {
            getIngredientAnalysisItemResponseList.add(GetIngredientAnalysisItemResponse.builder()
                    .item(entry.getKey())
                    .data(entry.getValue())
                    .build());
        }

        return getIngredientAnalysisItemResponseList;
    }

    @Override
    public List<GetIngredientAnalysisItemResponse> findIngredientAnalysisAsAverageAndYearAndPriceByFactory(String email, LocalDate startDate, LocalDate endDate, List<IngredientPriceType> priceTypeList) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("email", email)
                .addValue("startDate", startDate)
                .addValue("endDate", endDate);

        String findIngredientAnalysisQuery = """
                SELECT
                   date.year,
                   ingredient_price_data.purchase,
                   ingredient_price_data.sell
                FROM
                   (
                       WITH RECURSIVE T_TEMP_DATES AS (
                          SELECT :startDate AS DT
                       UNION
                          SELECT DATE_ADD(T_TEMP_DATES.DT, INTERVAL 1 YEAR)
                          FROM T_TEMP_DATES
                          WHERE DATE_ADD(T_TEMP_DATES.DT, INTERVAL 1 YEAR) <= :endDate
                       )
                       SELECT DT, DATE_FORMAT(DT, '%Y') AS year FROM T_TEMP_DATES
                   )  AS date LEFT OUTER JOIN (
                       SELECT
                           ranked_data.year,
                           ROUND(AVG(ranked_data.purchase), 2) as purchase,
                           ROUND(AVG(ranked_data.sell), 2) as sell
                       FROM (
                           SELECT
                               DATE_FORMAT(ingredient_price.created_at, '%Y') as year,
                               ingredient_price.purchase,
                               ingredient_price.sell,
                               ROW_NUMBER() over (PARTITION BY ingredient_price.ingredient_id, DATE_FORMAT(ingredient_price.created_at, '%Y') ORDER BY ingredient_price.created_at DESC) AS rn
                           FROM ingredient_price
                           JOIN ingredient ON ingredient.id = ingredient_price.ingredient_id
                           JOIN factory ON factory.id = ingredient.factory_id
                           JOIN user_table ON user_table.id = factory.user_id
                           WHERE
                               ingredient_price.created_at >= :startDate AND ingredient_price.created_at < DATE_ADD(:endDate, INTERVAL 1 YEAR ) AND
                               user_table.email = :email
                       ) AS ranked_data
                       WHERE ranked_data.rn = 1
                       GROUP BY year) AS ingredient_price_data
                ON (date.year = ingredient_price_data.year)
                ORDER BY date.year
                """;

        ColumnMapRowMapper rowMapper = new ColumnMapRowMapper();
        List<Map<String, Object>> ingredientAnalysisDataList = jdbcTemplate.query(findIngredientAnalysisQuery, namedParameters, rowMapper);

        Map<String, List<Number>> ingredientAnalysisDataMap = new HashMap<>();
        for (IngredientPriceType ingredientPriceType : priceTypeList) {
            ingredientAnalysisDataMap.put(ingredientPriceType.getRequest(), new ArrayList<>());
        }

        for(Map<String, Object> ingredientAnalysisData: ingredientAnalysisDataList){
            for (IngredientPriceType ingredientPriceType : priceTypeList) {
                ingredientAnalysisDataMap.get(ingredientPriceType.getRequest()).add((Number) ingredientAnalysisData.get(ingredientPriceType.getRequest()));
            }
        }

        List<GetIngredientAnalysisItemResponse> getIngredientAnalysisItemResponseList = new ArrayList<>();
        for (Map.Entry<String, List<Number>> entry : ingredientAnalysisDataMap.entrySet()) {
            getIngredientAnalysisItemResponseList.add(GetIngredientAnalysisItemResponse.builder()
                    .item(entry.getKey())
                    .data(entry.getValue())
                    .build());
        }

        return getIngredientAnalysisItemResponseList;
    }

    @Override
    public List<GetIngredientAnalysisItemResponse> findIngredientAnalysisAsIngredientAndMonthAndStock(Long ingredientId, LocalDate startDate, LocalDate endDate, List<IngredientStockType> stockTypeList, String stockUnit) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("ingredientId", ingredientId)
                .addValue("startDate", startDate)
                .addValue("endDate", endDate);

        String findIngredientAnalysisQuery = """
                SELECT
                    date.yearmonth,
                    ingredient_stock_data.incoming,
                    ingredient_stock_data.production,
                    ingredient_stock_data.stock,
                    ingredient_stock_data.optimal
                FROM
                    (
                        WITH RECURSIVE T_TEMP_DATES AS (
                           SELECT :startDate AS DT
                        UNION
                           SELECT DATE_ADD(T_TEMP_DATES.DT, INTERVAL 1 MONTH)
                           FROM T_TEMP_DATES
                           WHERE DATE_ADD(T_TEMP_DATES.DT, INTERVAL 1 MONTH) <= :endDate
                        )
                        SELECT DATE_FORMAT(DT, '%Y-%m') AS yearmonth FROM T_TEMP_DATES
                    )  AS date LEFT OUTER JOIN (
                        SELECT
                            ingredient_stock_data_1.yearmonth AS yearmonth,
                            ingredient_stock_data_1.incoming AS incoming,
                            ingredient_stock_data_1.production AS production,
                            ingredient_stock_data_2.stock AS stock,
                            ingredient_stock_data_2.optimal AS optimal
                        FROM
                            (
                                SELECT
                                    DATE_FORMAT(ingredient_stock.created_at, '%Y-%m') AS yearmonth,
                """ +
                (stockUnit.equals("count") ? """
                                    SUM(ingredient_stock.incoming) AS incoming,
                                    SUM(ingredient_stock.production) AS production
                """ :
                """
                                    ROUND(CAST(SUM(ingredient_stock.incoming) AS DECIMAL(10, 2)) * CAST(ingredient.weight AS DECIMAL (10, 2)), 2) AS incoming,
                                    ROUND(CAST(SUM(ingredient_stock.production) AS DECIMAL(10, 2)) * CAST(ingredient.weight AS DECIMAL(10, 2)), 2) AS production
                """) +
                """
                                FROM ingredient_stock
                                JOIN ingredient ON ingredient.id = ingredient_stock.ingredient_id
                                WHERE
                                    ingredient_stock.ingredient_id = :ingredientId AND
                                    ingredient_stock.created_at >= :startDate AND ingredient_stock.created_at < DATE_ADD(:endDate, INTERVAL 1 MONTH )
                                GROUP BY yearmonth
                            ) AS ingredient_stock_data_1
                            JOIN
                            (
                                SELECT
                                    ranked_data.yearmonth,
                                    ranked_data.stock as stock,
                                    ranked_data.optimal as optimal
                                FROM (
                                    SELECT
                                        DATE_FORMAT(ingredient_stock.created_at, '%Y-%m') AS yearmonth,            
                """ +
                (stockUnit.equals("count") ? """
                                        ingredient_stock.stock,
                                        ingredient_stock.optimal,
                """ :
                """
                                        ROUND(CAST(ingredient_stock.stock AS DECIMAL(10, 2)) * CAST(ingredient.weight AS DECIMAL(10, 2)), 2) AS stock,
                                        ROUND(CAST(ingredient_stock.optimal AS DECIMAL(10, 2)) * CAST(ingredient.weight AS DECIMAL(10, 2)), 2) AS optimal,
                """) +
                """                
                                        ROW_NUMBER() over (PARTITION BY DATE_FORMAT(ingredient_stock.created_at, '%Y-%m') ORDER BY ingredient_stock.created_at DESC) AS rn
                                    FROM ingredient_stock
                                    JOIN ingredient ON ingredient.id = ingredient_stock.ingredient_id
                                    WHERE
                                    ingredient_stock.ingredient_id = :ingredientId AND
                                    ingredient_stock.created_at >= :startDate AND ingredient_stock.created_at < DATE_ADD(:endDate, INTERVAL 1 MONTH )
                                ) AS ranked_data
                                WHERE ranked_data.rn = 1
                            ) AS ingredient_stock_data_2
                            ON (ingredient_stock_data_1.yearmonth = ingredient_stock_data_2.yearmonth)
                    ) AS ingredient_stock_data
                ON (date.yearmonth = ingredient_stock_data.yearmonth)
                ORDER BY date.yearmonth
                """;

        ColumnMapRowMapper rowMapper = new ColumnMapRowMapper();
        List<Map<String, Object>> ingredientAnalysisDataList = jdbcTemplate.query(findIngredientAnalysisQuery, namedParameters, rowMapper);

        Map<String, List<Number>> ingredientAnalysisDataMap = new HashMap<>();
        for (IngredientStockType ingredientStockType : stockTypeList) {
            ingredientAnalysisDataMap.put(ingredientStockType.getRequest(), new ArrayList<>());
        }

        for(Map<String, Object> ingredientAnalysisData: ingredientAnalysisDataList){
            for (IngredientStockType ingredientStockType : stockTypeList) {
                ingredientAnalysisDataMap.get(ingredientStockType.getRequest()).add((Number) ingredientAnalysisData.get(ingredientStockType.getRequest()));
            }
        }

        List<GetIngredientAnalysisItemResponse> getIngredientAnalysisItemResponseList = new ArrayList<>();
        for (Map.Entry<String, List<Number>> entry : ingredientAnalysisDataMap.entrySet()) {
            getIngredientAnalysisItemResponseList.add(GetIngredientAnalysisItemResponse.builder()
                    .item(entry.getKey())
                    .data(entry.getValue())
                    .build());
        }

        return getIngredientAnalysisItemResponseList;
    }

    @Override
    public List<GetIngredientAnalysisItemResponse> findIngredientAnalysisAsIngredientAndMonthAndPrice(Long ingredientId, LocalDate startDate, LocalDate endDate, List<IngredientPriceType> priceTypeList) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("ingredientId", ingredientId)
                .addValue("startDate", startDate)
                .addValue("endDate", endDate);

        String findIngredientAnalysisQuery = """
                SELECT
                    date.yearmonth,
                    data.purchase,
                    data.sell
                FROM
                    (
                        WITH RECURSIVE T_TEMP_DATES AS (
                           SELECT :startDate AS DT
                        UNION
                           SELECT DATE_ADD(T_TEMP_DATES.DT, INTERVAL 1 MONTH)
                           FROM T_TEMP_DATES
                           WHERE DATE_ADD(T_TEMP_DATES.DT, INTERVAL 1 MONTH) <= :endDate
                        )
                        SELECT DT, DATE_FORMAT(DT, '%Y-%m') AS yearmonth FROM T_TEMP_DATES
                    )  AS date LEFT OUTER JOIN (
                        SELECT
                            ranked_data.yearmonth,
                            ranked_data.purchase as purchase,
                            ranked_data.sell as sell
                        FROM (
                            SELECT
                                DATE_FORMAT(ingredient_price.created_at, '%Y-%m') AS yearmonth,
                                ingredient_price.purchase,
                                ingredient_price.sell,
                                ROW_NUMBER() over (PARTITION BY DATE_FORMAT(ingredient_price.created_at, '%Y-%m') ORDER BY ingredient_price.created_at DESC) AS rn
                            FROM ingredient_price
                            WHERE
                                ingredient_price.ingredient_id = :ingredientId AND
                                ingredient_price.created_at >= :startDate AND ingredient_price.created_at < DATE_ADD(:endDate, INTERVAL 1 MONTH )
                        ) AS ranked_data
                        WHERE ranked_data.rn = 1) AS data
                ON (date.yearmonth = data.yearmonth)
                ORDER BY date.yearmonth
                """;

        ColumnMapRowMapper rowMapper = new ColumnMapRowMapper();
        List<Map<String, Object>> ingredientAnalysisDataList = jdbcTemplate.query(findIngredientAnalysisQuery, namedParameters, rowMapper);

        Map<String, List<Number>> ingredientAnalysisDataMap = new HashMap<>();
        for (IngredientPriceType ingredientPriceType : priceTypeList) {
            ingredientAnalysisDataMap.put(ingredientPriceType.getRequest(), new ArrayList<>());
        }

        for(Map<String, Object> ingredientAnalysisData: ingredientAnalysisDataList){
            for (IngredientPriceType ingredientPriceType : priceTypeList) {
                ingredientAnalysisDataMap.get(ingredientPriceType.getRequest()).add((Number) ingredientAnalysisData.get(ingredientPriceType.getRequest()));
            }
        }

        List<GetIngredientAnalysisItemResponse> getIngredientAnalysisItemResponseList = new ArrayList<>();
        for (Map.Entry<String, List<Number>> entry : ingredientAnalysisDataMap.entrySet()) {
            getIngredientAnalysisItemResponseList.add(GetIngredientAnalysisItemResponse.builder()
                    .item(entry.getKey())
                    .data(entry.getValue())
                    .build());
        }

        return getIngredientAnalysisItemResponseList;
    }

    @Override
    public List<GetIngredientAnalysisItemResponse> findIngredientAnalysisAsIngredientAndYearAndStock(Long ingredientId, LocalDate startDate, LocalDate endDate, List<IngredientStockType> stockTypeList, String stockUnit) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("ingredientId", ingredientId)
                .addValue("startDate", startDate)
                .addValue("endDate", endDate);

        String findIngredientAnalysisQuery = """
                SELECT
                    date.year,
                    data.incoming,
                    data.production,
                    data.stock,
                    data.optimal
                FROM
                    (
                        WITH RECURSIVE T_TEMP_DATES AS (
                           SELECT :startDate AS DT
                        UNION
                           SELECT DATE_ADD(T_TEMP_DATES.DT, INTERVAL 1 YEAR)
                           FROM T_TEMP_DATES
                           WHERE DATE_ADD(T_TEMP_DATES.DT, INTERVAL 1 YEAR) <= :endDate
                        )
                        SELECT DT, DATE_FORMAT(DT, '%Y') AS year FROM T_TEMP_DATES
                    )  AS date LEFT OUTER JOIN (
                        SELECT
                            data1.year AS year,
                            data1.incoming AS incoming,
                            data1.production AS production,
                            data2.stock AS stock,
                            data2.optimal AS optimal
                        FROM
                            (
                                SELECT
                                    DATE_FORMAT(ingredient_stock.created_at, '%Y') AS year,
                """ +
                (stockUnit.equals("count") ? """
                                    SUM(ingredient_stock.incoming) AS incoming,
                                    SUM(ingredient_stock.production) AS production
                """ :
                """
                                    ROUND(CAST(SUM(ingredient_stock.incoming) AS DECIMAL(10, 2)) * CAST(ingredient.weight AS DECIMAL (10, 2)), 2) AS incoming,
                                    ROUND(CAST(SUM(ingredient_stock.production) AS DECIMAL(10, 2)) * CAST(ingredient.weight AS DECIMAL(10, 2)), 2) AS production
                """) +
                """         
                                FROM ingredient_stock
                                JOIN ingredient ON ingredient.id = ingredient_stock.ingredient_id
                                WHERE
                                    ingredient_stock.ingredient_id = :ingredientId AND
                                    ingredient_stock.created_at >= :startDate AND ingredient_stock.created_at < DATE_ADD(:endDate, INTERVAL 1 YEAR )
                                GROUP BY year
                            ) AS data1
                            JOIN
                            (
                                SELECT
                                    ranked_data.year,
                                    ranked_data.stock as stock,
                                    ranked_data.optimal as optimal
                                FROM (
                                    SELECT
                                        DATE_FORMAT(ingredient_stock.created_at, '%Y') AS year,
                """ +
                (stockUnit.equals("count") ? """
                                        ingredient_stock.stock,
                                        ingredient_stock.optimal,
                """ :
                """
                                        ROUND(CAST(ingredient_stock.stock AS DECIMAL(10, 2)) * CAST(ingredient.weight AS DECIMAL(10, 2)), 2) AS stock,
                                        ROUND(CAST(ingredient_stock.optimal AS DECIMAL(10, 2)) * CAST(ingredient.weight AS DECIMAL(10, 2)), 2) AS optimal,
                """) +
                """
                                        ROW_NUMBER() over (PARTITION BY DATE_FORMAT(ingredient_stock.created_at, '%Y') ORDER BY ingredient_stock.created_at DESC) AS rn
                                    FROM ingredient_stock
                                    JOIN ingredient ON ingredient.id = ingredient_stock.ingredient_id
                                    WHERE
                                        ingredient_stock.ingredient_id = :ingredientId AND
                                        ingredient_stock.created_at >= :startDate AND ingredient_stock.created_at < DATE_ADD(:endDate, INTERVAL 1 YEAR )
                                ) AS ranked_data
                                WHERE ranked_data.rn = 1
                            ) AS data2
                            ON (data1.year = data2.year)
                    ) AS data
                ON (date.year = data.year)
                ORDER BY date.year
                """;

        ColumnMapRowMapper rowMapper = new ColumnMapRowMapper();
        List<Map<String, Object>> ingredientAnalysisDataList = jdbcTemplate.query(findIngredientAnalysisQuery, namedParameters, rowMapper);

        Map<String, List<Number>> ingredientAnalysisDataMap = new HashMap<>();
        for (IngredientStockType ingredientStockType : stockTypeList) {
            ingredientAnalysisDataMap.put(ingredientStockType.getRequest(), new ArrayList<>());
        }

        for(Map<String, Object> ingredientAnalysisData: ingredientAnalysisDataList){
            for (IngredientStockType ingredientStockType : stockTypeList) {
                ingredientAnalysisDataMap.get(ingredientStockType.getRequest()).add((Number) ingredientAnalysisData.get(ingredientStockType.getRequest()));
            }
        }

        List<GetIngredientAnalysisItemResponse> getIngredientAnalysisItemResponseList = new ArrayList<>();
        for (Map.Entry<String, List<Number>> entry : ingredientAnalysisDataMap.entrySet()) {
            getIngredientAnalysisItemResponseList.add(GetIngredientAnalysisItemResponse.builder()
                    .item(entry.getKey())
                    .data(entry.getValue())
                    .build());
        }

        return getIngredientAnalysisItemResponseList;
    }

    @Override
    public List<GetIngredientAnalysisItemResponse> findIngredientAnalysisAsIngredientAndYearAndPrice(Long ingredientId, LocalDate startDate, LocalDate endDate, List<IngredientPriceType> priceTypeList) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("ingredientId", ingredientId)
                .addValue("startDate", startDate)
                .addValue("endDate", endDate);

        String findIngredientAnalysisQuery = """
                SELECT
                    date.year,
                    data.purchase,
                    data.sell
                FROM
                    (
                         WITH RECURSIVE T_TEMP_DATES AS (
                           SELECT :startDate AS DT
                        UNION
                           SELECT DATE_ADD(T_TEMP_DATES.DT, INTERVAL 1 YEAR)
                           FROM T_TEMP_DATES
                           WHERE DATE_ADD(T_TEMP_DATES.DT, INTERVAL 1 YEAR) <= :endDate
                        )
                        SELECT DT, DATE_FORMAT(DT, '%Y') AS year FROM T_TEMP_DATES
                    )  AS date LEFT OUTER JOIN (
                        SELECT
                            ranked_data.year,
                            ranked_data.purchase as purchase,
                            ranked_data.sell as sell
                        FROM (
                            SELECT
                                DATE_FORMAT(ingredient_price.created_at, '%Y') AS year,
                                ingredient_price.purchase,
                                ingredient_price.sell,
                                ROW_NUMBER() over (PARTITION BY DATE_FORMAT(ingredient_price.created_at, '%Y') ORDER BY ingredient_price.created_at DESC) AS rn
                            FROM ingredient_price
                            WHERE
                                ingredient_price.ingredient_id = :ingredientId AND
                                ingredient_price.created_at >= :startDate AND ingredient_price.created_at < DATE_ADD(:endDate, INTERVAL 1 YEAR )
                        ) AS ranked_data
                        WHERE ranked_data.rn = 1) AS data
                ON (date.year = data.year)
                ORDER BY date.year
                """;

        ColumnMapRowMapper rowMapper = new ColumnMapRowMapper();
        List<Map<String, Object>> ingredientAnalysisDataList = jdbcTemplate.query(findIngredientAnalysisQuery, namedParameters, rowMapper);

        Map<String, List<Number>> ingredientAnalysisDataMap = new HashMap<>();
        for (IngredientPriceType ingredientPriceType : priceTypeList) {
            ingredientAnalysisDataMap.put(ingredientPriceType.getRequest(), new ArrayList<>());
        }

        for(Map<String, Object> ingredientAnalysisData: ingredientAnalysisDataList){
            for (IngredientPriceType ingredientPriceType : priceTypeList) {
                ingredientAnalysisDataMap.get(ingredientPriceType.getRequest()).add((Number) ingredientAnalysisData.get(ingredientPriceType.getRequest()));
            }
        }

        List<GetIngredientAnalysisItemResponse> getIngredientAnalysisItemResponseList = new ArrayList<>();
        for (Map.Entry<String, List<Number>> entry : ingredientAnalysisDataMap.entrySet()) {
            getIngredientAnalysisItemResponseList.add(GetIngredientAnalysisItemResponse.builder()
                    .item(entry.getKey())
                    .data(entry.getValue())
                    .build());
        }

        return getIngredientAnalysisItemResponseList;
    }
}
