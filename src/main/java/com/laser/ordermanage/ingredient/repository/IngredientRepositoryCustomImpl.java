package com.laser.ordermanage.ingredient.repository;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.laser.ordermanage.factory.domain.QFactory.factory;
import static com.laser.ordermanage.factory.domain.QFactoryManager.factoryManager;
import static com.laser.ordermanage.ingredient.domain.QIngredient.ingredient;
import static com.laser.ordermanage.user.domain.QUserEntity.userEntity;

//TODO: 2024-11-21 쿼리 가독성 높이기 with 분리 ?
@RequiredArgsConstructor
public class IngredientRepositoryCustomImpl implements IngredientRepositoryCustom{

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final JPAQueryFactory queryFactory;

    @Override
    public List<GetIngredientResponse> findIngredientStatusByFactoryAndDate(String email, LocalDate date) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("email", email)
                .addValue("previousDate", date.minusDays(1))
                .addValue("date", date)
                .addValue("nextDate", date.plusDays(1));

        String findIngredientQuery = """
            SELECT
                ingredient.id AS id,
                ingredient.texture AS texture,
                ingredient.thickness AS thickness,
                ingredient.width AS width,
                ingredient.height AS height,
                ingredient.weight AS weight,
                ingredient.deleted_at IS NOT NULL AS isDeleted,
                ingredient_price_data.purchase AS purchase,
                ingredient_price_data.sell AS sell,
                COALESCE(ingredient_previous_stock_data.stock, 0) AS previousDay,
                COALESCE(ingredient_stock_data.incoming, 0) AS incoming,
                COALESCE(ingredient_stock_data.production, 0) AS production,
                COALESCE(ingredient_stock_data.stock, COALESCE(ingredient_previous_stock_data.stock, 0)) AS currentDay,
                COALESCE(ingredient_stock_data.optimal, ingredient_previous_stock_data.optimal) AS optimal
            FROM ingredient
            JOIN factory ON ingredient.factory_id = factory.id
            JOIN factory_manager ON factory.id = factory_manager.factory_id
            JOIN user_table on factory_manager.user_id = user_table.id
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
                    WHERE ingredient_stock.created_at <= :previousDate
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
                ingredient.created_at < :nextDate AND
                (ingredient.deleted_at IS NULL or ingredient.deleted_at >= :date) AND
                user_table.email = :email
            """;
        return jdbcTemplate.query(findIngredientQuery, namedParameters, new IngredientRowMapper());
    }

    @Override
    public List<GetIngredientInfoResponse> findIngredientByFactoryManager(String email) {
        List<GetIngredientInfoResponse> ingredientInfoResponseList = queryFactory
                .select(new QGetIngredientInfoResponse(
                        ingredient.id,
                        ingredient.texture,
                        ingredient.thickness
                ))
                .from(ingredient)
                .join(ingredient.factory, factory)
                .join(factoryManager).on(factoryManager.factory.eq(factory))
                .join(factoryManager.user, userEntity)
                .where(userEntity.email.eq(email))
                .fetch();

        return ingredientInfoResponseList;
    }

    @Override
    public List<GetIngredientAnalysisItemResponse> findIngredientAnalysisAsTotalAndMonthAndStockByFactoryManager(String email, LocalDate startDate, LocalDate endDate, List<String> itemTypeList, String stockUnit) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("email", email)
                .addValue("startDate", startDate)
                .addValue("endDate", endDate)
                .addValue("nextEndDate", endDate.plusMonths(1));

        String findIngredientAnalysisQuery = """
                SELECT
                    date.yearmonth,
                    ingredient_stock_data.incoming,
                    ingredient_stock_data.production,
                    ingredient_stock_data.stock,
                    ingredient_stock_data.optimal
                FROM
                    (
                        WITH RECURSIVE T_TEMP_DATES(DT) AS (
                           SELECT :startDate
                        UNION
                           SELECT TIMESTAMPADD(MONTH, 1, DT)
                           FROM T_TEMP_DATES
                           WHERE TIMESTAMPADD(MONTH, 1, DT) <= :endDate
                        )
                        SELECT (CONCAT(YEAR(DT), '-', LPAD(MONTH(DT), 2, '0'))) AS yearmonth FROM T_TEMP_DATES
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
                                    (CONCAT(YEAR(ingredient_stock.created_at), '-', LPAD(MONTH(ingredient_stock.created_at), 2, '0'))) AS yearmonth,
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
                                JOIN factory_manager ON factory.id = factory_manager.factory_id
                                JOIN user_table ON user_table.id = factory_manager.user_id
                                WHERE
                                    ingredient_stock.created_at >= :startDate AND ingredient_stock.created_at < :nextEndDate AND
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
                                        (CONCAT(YEAR(ingredient_stock.created_at), '-', LPAD(MONTH(ingredient_stock.created_at), 2, '0'))) AS yearmonth,
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
                                        ROW_NUMBER() over (PARTITION BY ingredient_stock.ingredient_id, (CONCAT(YEAR(ingredient_stock.created_at), '-', LPAD(MONTH(ingredient_stock.created_at), 2, '0'))) ORDER BY ingredient_stock.created_at DESC) AS rn
                                    FROM ingredient_stock
                                    JOIN ingredient ON ingredient.id = ingredient_stock.ingredient_id
                                    JOIN factory ON factory.id = ingredient.factory_id
                                    JOIN factory_manager ON factory.id = factory_manager.factory_id
                                    JOIN user_table ON user_table.id = factory_manager.user_id
                                    WHERE
                                        ingredient_stock.created_at >= :startDate AND ingredient_stock.created_at < :nextEndDate AND
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

        return extractAnalysisResponse(itemTypeList, jdbcTemplate.query(findIngredientAnalysisQuery, namedParameters, new ColumnMapRowMapper()));
    }

    @Override
    public List<GetIngredientAnalysisItemResponse> findIngredientAnalysisAsTotalAndMonthAndPriceByFactoryManager(String email, LocalDate startDate, LocalDate endDate, List<String> itemTypeList) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("email", email)
                .addValue("startDate", startDate)
                .addValue("endDate", endDate)
                .addValue("nextEndDate", endDate.plusMonths(1));

        String findIngredientAnalysisQuery = """
                SELECT
                    date.yearmonth,
                    ingredient_price_data.purchase,
                    ingredient_price_data.sell
                FROM
                    (
                        WITH RECURSIVE T_TEMP_DATES(DT) AS (
                           SELECT :startDate
                        UNION
                           SELECT TIMESTAMPADD(MONTH, 1, DT)
                           FROM T_TEMP_DATES
                           WHERE TIMESTAMPADD(MONTH, 1, DT) <= :endDate
                        )
                        SELECT (CONCAT(YEAR(DT), '-', LPAD(MONTH(DT), 2, '0'))) AS yearmonth FROM T_TEMP_DATES
                    )  AS date LEFT OUTER JOIN (
                        SELECT
                            ranked_data.yearmonth,
                            SUM(ranked_data.purchase) as purchase,
                            SUM(ranked_data.sell) as sell
                        FROM (
                            SELECT
                                (CONCAT(YEAR(ingredient_price.created_at), '-', LPAD(MONTH(ingredient_price.created_at), 2, '0'))) AS yearmonth,
                                ingredient_price.purchase,
                                ingredient_price.sell,
                                ROW_NUMBER() over (PARTITION BY ingredient_price.ingredient_id, (CONCAT(YEAR(ingredient_price.created_at), '-', LPAD(MONTH(ingredient_price.created_at), 2, '0'))) ORDER BY ingredient_price.created_at DESC) AS rn
                            FROM ingredient_price
                            JOIN ingredient ON ingredient.id = ingredient_price.ingredient_id
                            JOIN factory ON factory.id = ingredient.factory_id
                            JOIN factory_manager ON factory.id = factory_manager.factory_id
                            JOIN user_table ON user_table.id = factory_manager.user_id
                            WHERE
                                ingredient_price.created_at >= :startDate AND ingredient_price.created_at < :nextEndDate AND
                                user_table.email = :email
                        ) AS ranked_data
                        WHERE ranked_data.rn = 1
                        GROUP BY yearmonth) AS ingredient_price_data
                ON (date.yearmonth = ingredient_price_data.yearmonth)
                ORDER BY date.yearmonth
                """;

        return extractAnalysisResponse(itemTypeList, jdbcTemplate.query(findIngredientAnalysisQuery, namedParameters, new ColumnMapRowMapper()));
    }

    @Override
    public List<GetIngredientAnalysisItemResponse> findIngredientAnalysisAsTotalAndYearAndStockByFactoryManager(String email, LocalDate startDate, LocalDate endDate, List<String> itemTypeList, String stockUnit) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("email", email)
                .addValue("startDate", startDate)
                .addValue("endDate", endDate)
                .addValue("nextEndDate", endDate.plusYears(1));

        String findIngredientAnalysisQuery = """
                SELECT
                    date.yr,
                    ingredient_stock_data.incoming,
                    ingredient_stock_data.production,
                    ingredient_stock_data.stock,
                    ingredient_stock_data.optimal
                FROM
                    (
                        WITH RECURSIVE T_TEMP_DATES(DT) AS (
                           SELECT :startDate
                        UNION
                           SELECT TIMESTAMPADD(YEAR, 1, DT)
                           FROM T_TEMP_DATES
                           WHERE TIMESTAMPADD(YEAR, 1, DT) <= :endDate
                        )
                        SELECT YEAR(DT) AS yr FROM T_TEMP_DATES
                    )  AS date LEFT OUTER JOIN (
                        SELECT
                            ingredient_stock_data_1.yr AS yr,
                            SUM(ingredient_stock_data_1.incoming) AS incoming,
                            SUM(ingredient_stock_data_1.production) AS production,
                            SUM(ingredient_stock_data_2.stock) AS stock,
                            SUM(ingredient_stock_data_2.optimal) AS optimal
                        FROM
                            (
                                SELECT
                                    ingredient_stock.ingredient_id,
                                    YEAR(ingredient_stock.created_at) AS yr,
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
                                JOIN factory_manager ON factory.id = factory_manager.factory_id
                                JOIN user_table ON user_table.id = factory_manager.user_id
                                WHERE
                                    ingredient_stock.created_at >= :startDate AND ingredient_stock.created_at < :nextEndDate AND
                                    user_table.email = :email
                                GROUP BY ingredient_stock.ingredient_id, yr
                            ) AS ingredient_stock_data_1
                            JOIN
                            (
                                SELECT
                                    ranked_data.ingredient_id as ingredient_id,
                                    ranked_data.yr,
                                    ranked_data.stock as stock,
                                    ranked_data.optimal as optimal
                                FROM (
                                    SELECT
                                        ingredient_stock.ingredient_id,
                                        YEAR(ingredient_stock.created_at) AS yr,
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
                                        ROW_NUMBER() over (PARTITION BY ingredient_stock.ingredient_id, YEAR(ingredient_stock.created_at) ORDER BY ingredient_stock.created_at DESC) AS rn
                                    FROM ingredient_stock
                                    JOIN ingredient ON ingredient.id = ingredient_stock.ingredient_id
                                    JOIN factory ON factory.id = ingredient.factory_id
                                    JOIN factory_manager ON factory.id = factory_manager.factory_id
                                    JOIN user_table ON user_table.id = factory_manager.user_id
                                    WHERE
                                        ingredient_stock.created_at >= :startDate AND ingredient_stock.created_at < :nextEndDate AND
                                        user_table.email = :email
                                ) AS ranked_data
                                WHERE ranked_data.rn = 1
                            ) AS ingredient_stock_data_2
                            ON (ingredient_stock_data_1.ingredient_id = ingredient_stock_data_2.ingredient_id and ingredient_stock_data_1.yr = ingredient_stock_data_2.yr)
                        GROUP BY ingredient_stock_data_1.yr
                    ) AS ingredient_stock_data
                ON (date.yr = ingredient_stock_data.yr)
                ORDER BY date.yr
                """;

        return extractAnalysisResponse(itemTypeList, jdbcTemplate.query(findIngredientAnalysisQuery, namedParameters, new ColumnMapRowMapper()));
    }

    @Override
    public List<GetIngredientAnalysisItemResponse> findIngredientAnalysisAsTotalAndYearAndPriceByFactoryManager(String email, LocalDate startDate, LocalDate endDate, List<String> itemTypeList) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("email", email)
                .addValue("startDate", startDate)
                .addValue("endDate", endDate)
                .addValue("nextEndDate", endDate.plusYears(1));

        String findIngredientAnalysisQuery = """
                SELECT
                   date.yr,
                   ingredient_price_data.purchase,
                   ingredient_price_data.sell
                FROM
                   (
                       WITH RECURSIVE T_TEMP_DATES(DT) AS (
                          SELECT :startDate
                       UNION
                          SELECT TIMESTAMPADD(YEAR, 1, DT)
                          FROM T_TEMP_DATES
                          WHERE TIMESTAMPADD(YEAR, 1, DT) <= :endDate
                       )
                       SELECT YEAR(DT) AS yr FROM T_TEMP_DATES
                   )  AS date LEFT OUTER JOIN (
                       SELECT
                           ranked_data.yr,
                           SUM(ranked_data.purchase) as purchase,
                           SUM(ranked_data.sell) as sell
                       FROM (
                           SELECT
                               YEAR(ingredient_price.created_at) as yr,
                               ingredient_price.purchase,
                               ingredient_price.sell,
                               ROW_NUMBER() over (PARTITION BY ingredient_price.ingredient_id, YEAR(ingredient_price.created_at) ORDER BY ingredient_price.created_at DESC) AS rn
                           FROM ingredient_price
                           JOIN ingredient ON ingredient.id = ingredient_price.ingredient_id
                           JOIN factory ON factory.id = ingredient.factory_id
                           JOIN factory_manager ON factory.id = factory_manager.factory_id
                           JOIN user_table ON user_table.id = factory_manager.user_id
                           WHERE
                               ingredient_price.created_at >= :startDate AND ingredient_price.created_at < :nextEndDate AND
                               user_table.email = :email
                       ) AS ranked_data
                       WHERE ranked_data.rn = 1
                       GROUP BY yr) AS ingredient_price_data
                ON (date.yr = ingredient_price_data.yr)
                ORDER BY date.yr
                """;

        return extractAnalysisResponse(itemTypeList, jdbcTemplate.query(findIngredientAnalysisQuery, namedParameters, new ColumnMapRowMapper()));
    }

    @Override
    public List<GetIngredientAnalysisItemResponse> findIngredientAnalysisAsAverageAndMonthAndStockByFactoryManager(String email, LocalDate startDate, LocalDate endDate, List<String> itemTypeList, String stockUnit) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("email", email)
                .addValue("startDate", startDate)
                .addValue("endDate", endDate)
                .addValue("nextEndDate", endDate.plusMonths(1));

        String findIngredientAnalysisQuery = """
                SELECT
                    date.yearmonth,
                    ingredient_stock_data.incoming,
                    ingredient_stock_data.production,
                    ingredient_stock_data.stock,
                    ingredient_stock_data.optimal
                FROM
                    (
                        WITH RECURSIVE T_TEMP_DATES(DT) AS (
                           SELECT :startDate
                        UNION
                           SELECT TIMESTAMPADD(MONTH, 1, DT)
                           FROM T_TEMP_DATES
                           WHERE TIMESTAMPADD(MONTH, 1, DT) <= :endDate
                        )
                        SELECT (CONCAT(YEAR(DT), '-', LPAD(MONTH(DT), 2, '0'))) AS yearmonth FROM T_TEMP_DATES
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
                                    (CONCAT(YEAR(ingredient_stock.created_at), '-', LPAD(MONTH(ingredient_stock.created_at), 2, '0'))) AS yearmonth,
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
                                JOIN factory_manager ON factory.id = factory_manager.factory_id
                                JOIN user_table ON user_table.id = factory_manager.user_id
                                WHERE
                                    ingredient_stock.created_at >= :startDate AND ingredient_stock.created_at < :nextEndDate AND
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
                                        (CONCAT(YEAR(ingredient_stock.created_at), '-', LPAD(MONTH(ingredient_stock.created_at), 2, '0'))) AS yearmonth,
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
                                        ROW_NUMBER() over (PARTITION BY ingredient_stock.ingredient_id, (CONCAT(YEAR(ingredient_stock.created_at), '-', LPAD(MONTH(ingredient_stock.created_at), 2, '0'))) ORDER BY ingredient_stock.created_at DESC) AS rn
                                    FROM ingredient_stock
                                    JOIN ingredient ON ingredient.id = ingredient_stock.ingredient_id
                                    JOIN factory ON factory.id = ingredient.factory_id
                                    JOIN factory_manager ON factory.id = factory_manager.factory_id
                                    JOIN user_table ON user_table.id = factory_manager.user_id
                                    WHERE
                                        ingredient_stock.created_at >= :startDate AND ingredient_stock.created_at < :nextEndDate AND
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

        return extractAnalysisResponse(itemTypeList, jdbcTemplate.query(findIngredientAnalysisQuery, namedParameters, new ColumnMapRowMapper()));
    }

    @Override
    public List<GetIngredientAnalysisItemResponse> findIngredientAnalysisAsAverageAndMonthAndPriceByFactoryManager(String email, LocalDate startDate, LocalDate endDate, List<String> itemTypeList) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("email", email)
                .addValue("startDate", startDate)
                .addValue("endDate", endDate)
                .addValue("nextEndDate", endDate.plusMonths(1));

        String findIngredientAnalysisQuery = """
                SELECT
                    date.yearmonth,
                    ingredient_price_data.purchase,
                    ingredient_price_data.sell
                FROM
                    (
                        WITH RECURSIVE T_TEMP_DATES(DT) AS (
                           SELECT :startDate
                        UNION
                           SELECT TIMESTAMPADD(MONTH, 1, DT)
                           FROM T_TEMP_DATES
                           WHERE TIMESTAMPADD(MONTH, 1, DT) <= :endDate
                        )
                        SELECT (CONCAT(YEAR(DT), '-', LPAD(MONTH(DT), 2, '0'))) AS yearmonth FROM T_TEMP_DATES
                    )  AS date LEFT OUTER JOIN (
                        SELECT
                            ranked_data.yearmonth,
                            ROUND(AVG(ranked_data.purchase), 2) as purchase,
                            ROUND(AVG(ranked_data.sell), 2) as sell
                        FROM (
                            SELECT
                                (CONCAT(YEAR(ingredient_price.created_at), '-', LPAD(MONTH(ingredient_price.created_at), 2, '0'))) AS yearmonth,
                                ingredient_price.purchase,
                                ingredient_price.sell,
                                ROW_NUMBER() over (PARTITION BY ingredient_price.ingredient_id, (CONCAT(YEAR(ingredient_price.created_at), '-', LPAD(MONTH(ingredient_price.created_at), 2, '0'))) ORDER BY ingredient_price.created_at DESC) AS rn
                            FROM ingredient_price
                            JOIN ingredient ON ingredient.id = ingredient_price.ingredient_id
                            JOIN factory ON factory.id = ingredient.factory_id
                            JOIN factory_manager ON factory.id = factory_manager.factory_id
                            JOIN user_table ON user_table.id = factory_manager.user_id
                            WHERE
                                ingredient_price.created_at >= :startDate AND ingredient_price.created_at < :nextEndDate AND
                                user_table.email = :email
                        ) AS ranked_data
                        WHERE ranked_data.rn = 1
                        GROUP BY yearmonth) AS ingredient_price_data
                ON (date.yearmonth = ingredient_price_data.yearmonth)
                ORDER BY date.yearmonth
                """;

        return extractAnalysisResponse(itemTypeList, jdbcTemplate.query(findIngredientAnalysisQuery, namedParameters, new ColumnMapRowMapper()));
    }

    @Override
    public List<GetIngredientAnalysisItemResponse> findIngredientAnalysisAsAverageAndYearAndStockByFactoryManager(String email, LocalDate startDate, LocalDate endDate, List<String> itemTypeList, String stockUnit) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("email", email)
                .addValue("startDate", startDate)
                .addValue("endDate", endDate)
                .addValue("nextEndDate", endDate.plusYears(1));

        String findIngredientAnalysisQuery = """
                SELECT
                    date.yr,
                    ingredient_stock_data.incoming,
                    ingredient_stock_data.production,
                    ingredient_stock_data.stock,
                    ingredient_stock_data.optimal
                FROM
                    (
                        WITH RECURSIVE T_TEMP_DATES(DT) AS (
                           SELECT :startDate
                        UNION
                           SELECT TIMESTAMPADD(YEAR, 1, DT)
                           FROM T_TEMP_DATES
                           WHERE TIMESTAMPADD(YEAR, 1, DT) <= :endDate
                        )
                        SELECT YEAR(DT) AS yr FROM T_TEMP_DATES
                    )  AS date LEFT OUTER JOIN (
                        SELECT
                            ingredient_stock_data_1.yr AS yr,
                            ROUND(AVG(ingredient_stock_data_1.incoming), 2) AS incoming,
                            ROUND(AVG(ingredient_stock_data_1.production), 2) AS production,
                            ROUND(AVG(ingredient_stock_data_2.stock), 2) AS stock,
                            ROUND(AVG(ingredient_stock_data_2.optimal), 2) AS optimal
                        FROM
                            (
                                SELECT
                                    ingredient_stock.ingredient_id,
                                    YEAR(ingredient_stock.created_at) AS yr,
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
                                JOIN factory_manager ON factory.id = factory_manager.factory_id
                                JOIN user_table ON user_table.id = factory_manager.user_id
                                WHERE
                                    ingredient_stock.created_at >= :startDate AND ingredient_stock.created_at < :nextEndDate AND
                                    user_table.email = :email
                                GROUP BY ingredient_stock.ingredient_id, yr
                            ) AS ingredient_stock_data_1
                            JOIN
                            (
                                SELECT
                                    ranked_data.ingredient_id as ingredient_id,
                                    ranked_data.yr,
                                    ranked_data.stock as stock,
                                    ranked_data.optimal as optimal
                                FROM (
                                    SELECT
                                        ingredient_stock.ingredient_id,
                                        YEAR(ingredient_stock.created_at) AS yr,
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
                                        ROW_NUMBER() over (PARTITION BY ingredient_stock.ingredient_id, YEAR(ingredient_stock.created_at) ORDER BY ingredient_stock.created_at DESC) AS rn
                                    FROM ingredient_stock
                                    JOIN ingredient ON ingredient.id = ingredient_stock.ingredient_id
                                    JOIN factory ON factory.id = ingredient.factory_id
                                    JOIN factory_manager ON factory.id = factory_manager.factory_id
                                    JOIN user_table ON user_table.id = factory_manager.user_id
                                    WHERE
                                        ingredient_stock.created_at >= :startDate AND ingredient_stock.created_at < :nextEndDate AND
                                        user_table.email = :email
                                ) AS ranked_data
                                WHERE ranked_data.rn = 1
                            ) AS ingredient_stock_data_2
                            ON (ingredient_stock_data_1.ingredient_id = ingredient_stock_data_2.ingredient_id and ingredient_stock_data_1.yr = ingredient_stock_data_2.yr)
                        GROUP BY ingredient_stock_data_1.yr
                    ) AS ingredient_stock_data
                ON (date.yr = ingredient_stock_data.yr)
                ORDER BY date.yr
                """;

        return extractAnalysisResponse(itemTypeList, jdbcTemplate.query(findIngredientAnalysisQuery, namedParameters, new ColumnMapRowMapper()));
    }

    @Override
    public List<GetIngredientAnalysisItemResponse> findIngredientAnalysisAsAverageAndYearAndPriceByFactoryManager(String email, LocalDate startDate, LocalDate endDate, List<String> itemTypeList) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("email", email)
                .addValue("startDate", startDate)
                .addValue("endDate", endDate)
                .addValue("nextEndDate", endDate.plusYears(1));

        String findIngredientAnalysisQuery = """
                SELECT
                   date.yr,
                   ingredient_price_data.purchase,
                   ingredient_price_data.sell
                FROM
                   (
                       WITH RECURSIVE T_TEMP_DATES(DT) AS (
                          SELECT :startDate
                       UNION
                          SELECT TIMESTAMPADD(YEAR, 1, DT)
                          FROM T_TEMP_DATES
                          WHERE TIMESTAMPADD(YEAR, 1, DT) <= :endDate
                       )
                       SELECT YEAR(DT) AS yr FROM T_TEMP_DATES
                   )  AS date LEFT OUTER JOIN (
                       SELECT
                           ranked_data.yr,
                           ROUND(AVG(ranked_data.purchase), 2) as purchase,
                           ROUND(AVG(ranked_data.sell), 2) as sell
                       FROM (
                           SELECT
                               YEAR(ingredient_price.created_at) as yr,
                               ingredient_price.purchase,
                               ingredient_price.sell,
                               ROW_NUMBER() over (PARTITION BY ingredient_price.ingredient_id, YEAR(ingredient_price.created_at) ORDER BY ingredient_price.created_at DESC) AS rn
                           FROM ingredient_price
                           JOIN ingredient ON ingredient.id = ingredient_price.ingredient_id
                           JOIN factory ON factory.id = ingredient.factory_id
                           JOIN factory_manager ON factory.id = factory_manager.factory_id
                           JOIN user_table ON user_table.id = factory_manager.user_id
                           WHERE
                               ingredient_price.created_at >= :startDate AND ingredient_price.created_at < :nextEndDate AND
                               user_table.email = :email
                       ) AS ranked_data
                       WHERE ranked_data.rn = 1
                       GROUP BY yr) AS ingredient_price_data
                ON (date.yr = ingredient_price_data.yr)
                ORDER BY date.yr
                """;

        return extractAnalysisResponse(itemTypeList, jdbcTemplate.query(findIngredientAnalysisQuery, namedParameters, new ColumnMapRowMapper()));
    }

    @Override
    public List<GetIngredientAnalysisItemResponse> findIngredientAnalysisAsIngredientAndMonthAndStock(Long ingredientId, LocalDate startDate, LocalDate endDate, List<String> itemTypeList, String stockUnit) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("ingredientId", ingredientId)
                .addValue("startDate", startDate)
                .addValue("endDate", endDate)
                .addValue("nextEndDate", endDate.plusMonths(1));

        String findIngredientAnalysisQuery = """
                SELECT
                    date.yearmonth,
                    ingredient_stock_data.incoming,
                    ingredient_stock_data.production,
                    ingredient_stock_data.stock,
                    ingredient_stock_data.optimal
                FROM
                    (
                        WITH RECURSIVE T_TEMP_DATES(DT) AS (
                           SELECT :startDate
                        UNION
                           SELECT TIMESTAMPADD(MONTH, 1, DT)
                           FROM T_TEMP_DATES
                           WHERE TIMESTAMPADD(MONTH, 1, DT) <= :endDate
                        )
                        SELECT (CONCAT(YEAR(DT), '-', LPAD(MONTH(DT), 2, '0'))) AS yearmonth FROM T_TEMP_DATES
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
                                    (CONCAT(YEAR(ingredient_stock.created_at), '-', LPAD(MONTH(ingredient_stock.created_at), 2, '0'))) AS yearmonth,
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
                                    ingredient_stock.created_at >= :startDate AND ingredient_stock.created_at < :nextEndDate
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
                                        (CONCAT(YEAR(ingredient_stock.created_at), '-', LPAD(MONTH(ingredient_stock.created_at), 2, '0'))) AS yearmonth,     
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
                                        ROW_NUMBER() over (PARTITION BY (CONCAT(YEAR(ingredient_stock.created_at), '-', LPAD(MONTH(ingredient_stock.created_at), 2, '0'))) ORDER BY ingredient_stock.created_at DESC) AS rn
                                    FROM ingredient_stock
                                    JOIN ingredient ON ingredient.id = ingredient_stock.ingredient_id
                                    WHERE
                                    ingredient_stock.ingredient_id = :ingredientId AND
                                    ingredient_stock.created_at >= :startDate AND ingredient_stock.created_at < :nextEndDate
                                ) AS ranked_data
                                WHERE ranked_data.rn = 1
                            ) AS ingredient_stock_data_2
                            ON (ingredient_stock_data_1.yearmonth = ingredient_stock_data_2.yearmonth)
                    ) AS ingredient_stock_data
                ON (date.yearmonth = ingredient_stock_data.yearmonth)
                ORDER BY date.yearmonth
                """;

        return extractAnalysisResponse(itemTypeList, jdbcTemplate.query(findIngredientAnalysisQuery, namedParameters, new ColumnMapRowMapper()));
    }

    @Override
    public List<GetIngredientAnalysisItemResponse> findIngredientAnalysisAsIngredientAndMonthAndPrice(Long ingredientId, LocalDate startDate, LocalDate endDate, List<String> itemTypeList) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("ingredientId", ingredientId)
                .addValue("startDate", startDate)
                .addValue("endDate", endDate)
                .addValue("nextEndDate", endDate.plusMonths(1));

        String findIngredientAnalysisQuery = """
                SELECT
                    date.yearmonth,
                    data.purchase,
                    data.sell
                FROM
                    (
                        WITH RECURSIVE T_TEMP_DATES(DT) AS (
                           SELECT :startDate
                        UNION
                           SELECT TIMESTAMPADD(MONTH, 1, DT)
                           FROM T_TEMP_DATES
                           WHERE TIMESTAMPADD(MONTH, 1, DT) <= :endDate
                        )
                        SELECT (CONCAT(YEAR(DT), '-', LPAD(MONTH(DT), 2, '0'))) AS yearmonth FROM T_TEMP_DATES
                    )  AS date LEFT OUTER JOIN (
                        SELECT
                            ranked_data.yearmonth,
                            ranked_data.purchase as purchase,
                            ranked_data.sell as sell
                        FROM (
                            SELECT
                                (CONCAT(YEAR(ingredient_price.created_at), '-', LPAD(MONTH(ingredient_price.created_at), 2, '0'))) AS yearmonth,
                                ingredient_price.purchase,
                                ingredient_price.sell,
                                ROW_NUMBER() over (PARTITION BY (CONCAT(YEAR(ingredient_price.created_at), '-', LPAD(MONTH(ingredient_price.created_at), 2, '0'))) ORDER BY ingredient_price.created_at DESC) AS rn
                            FROM ingredient_price
                            WHERE
                                ingredient_price.ingredient_id = :ingredientId AND
                                ingredient_price.created_at >= :startDate AND ingredient_price.created_at < :nextEndDate
                        ) AS ranked_data
                        WHERE ranked_data.rn = 1) AS data
                ON (date.yearmonth = data.yearmonth)
                ORDER BY date.yearmonth
                """;

        return extractAnalysisResponse(itemTypeList, jdbcTemplate.query(findIngredientAnalysisQuery, namedParameters, new ColumnMapRowMapper()));
    }

    @Override
    public List<GetIngredientAnalysisItemResponse> findIngredientAnalysisAsIngredientAndYearAndStock(Long ingredientId, LocalDate startDate, LocalDate endDate, List<String> itemTypeList, String stockUnit) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("ingredientId", ingredientId)
                .addValue("startDate", startDate)
                .addValue("endDate", endDate)
                .addValue("nextEndDate", endDate.plusYears(1));

        String findIngredientAnalysisQuery = """
                SELECT
                    date.yr,
                    data.incoming,
                    data.production,
                    data.stock,
                    data.optimal
                FROM
                    (
                        WITH RECURSIVE T_TEMP_DATES(DT) AS (
                           SELECT :startDate
                        UNION
                           SELECT TIMESTAMPADD(YEAR, 1, DT)
                           FROM T_TEMP_DATES
                           WHERE TIMESTAMPADD(YEAR, 1, DT) <= :endDate
                        )
                        SELECT YEAR(DT) AS yr FROM T_TEMP_DATES
                    )  AS date LEFT OUTER JOIN (
                        SELECT
                            data1.yr AS yr,
                            data1.incoming AS incoming,
                            data1.production AS production,
                            data2.stock AS stock,
                            data2.optimal AS optimal
                        FROM
                            (
                                SELECT
                                    YEAR(ingredient_stock.created_at) AS yr,
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
                                    ingredient_stock.created_at >= :startDate AND ingredient_stock.created_at < :nextEndDate
                                GROUP BY yr
                            ) AS data1
                            JOIN
                            (
                                SELECT
                                    ranked_data.yr,
                                    ranked_data.stock as stock,
                                    ranked_data.optimal as optimal
                                FROM (
                                    SELECT
                                        YEAR(ingredient_stock.created_at) AS yr,
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
                                        ROW_NUMBER() over (PARTITION BY YEAR(ingredient_stock.created_at) ORDER BY ingredient_stock.created_at DESC) AS rn
                                    FROM ingredient_stock
                                    JOIN ingredient ON ingredient.id = ingredient_stock.ingredient_id
                                    WHERE
                                        ingredient_stock.ingredient_id = :ingredientId AND
                                        ingredient_stock.created_at >= :startDate AND ingredient_stock.created_at < :nextEndDate
                                ) AS ranked_data
                                WHERE ranked_data.rn = 1
                            ) AS data2
                            ON (data1.yr = data2.yr)
                    ) AS data
                ON (date.yr = data.yr)
                ORDER BY date.yr
                """;

        return extractAnalysisResponse(itemTypeList, jdbcTemplate.query(findIngredientAnalysisQuery, namedParameters, new ColumnMapRowMapper()));
    }

    @Override
    public List<GetIngredientAnalysisItemResponse> findIngredientAnalysisAsIngredientAndYearAndPrice(Long ingredientId, LocalDate startDate, LocalDate endDate, List<String> itemTypeList) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("ingredientId", ingredientId)
                .addValue("startDate", startDate)
                .addValue("endDate", endDate)
                .addValue("nextEndDate", endDate.plusYears(1));

        String findIngredientAnalysisQuery = """
                SELECT
                    date.yr,
                    data.purchase,
                    data.sell
                FROM
                    (
                         WITH RECURSIVE T_TEMP_DATES(DT) AS (
                           SELECT :startDate
                        UNION
                           SELECT TIMESTAMPADD(YEAR, 1, DT)
                           FROM T_TEMP_DATES
                           WHERE TIMESTAMPADD(YEAR, 1, DT) <= :endDate
                        )
                        SELECT YEAR(DT) AS yr FROM T_TEMP_DATES
                    )  AS date LEFT OUTER JOIN (
                        SELECT
                            ranked_data.yr,
                            ranked_data.purchase as purchase,
                            ranked_data.sell as sell
                        FROM (
                            SELECT
                                YEAR(ingredient_price.created_at) AS yr,
                                ingredient_price.purchase,
                                ingredient_price.sell,
                                ROW_NUMBER() over (PARTITION BY YEAR(ingredient_price.created_at) ORDER BY ingredient_price.created_at DESC) AS rn
                            FROM ingredient_price
                            WHERE
                                ingredient_price.ingredient_id = :ingredientId AND
                                ingredient_price.created_at >= :startDate AND ingredient_price.created_at < :nextEndDate
                        ) AS ranked_data
                        WHERE ranked_data.rn = 1) AS data
                ON (date.yr = data.yr)
                ORDER BY date.yr
                """;

        return extractAnalysisResponse(itemTypeList, jdbcTemplate.query(findIngredientAnalysisQuery, namedParameters, new ColumnMapRowMapper()));
    }

    public List<GetIngredientAnalysisItemResponse> extractAnalysisResponse(List<String> itemTypeList, List<Map<String, Object>> ingredientAnalysisDataList) {
        Map<String, List<Number>> ingredientAnalysisDataMap = new HashMap<>();
        for (String itemType : itemTypeList) {
            ingredientAnalysisDataMap.put(itemType, new ArrayList<>());
        }

        for (Map<String, Object> ingredientAnalysisData : ingredientAnalysisDataList) {
            for (String itemType : itemTypeList) {
                ingredientAnalysisDataMap.get(itemType).add((Number) ingredientAnalysisData.get(itemType));
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
