package com.laser.ordermanage.ingredient.repository;

import com.laser.ordermanage.ingredient.domain.Ingredient;
import com.laser.ordermanage.ingredient.domain.IngredientStock;
import com.laser.ordermanage.ingredient.dto.response.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.laser.ordermanage.factory.domain.QFactory.factory;
import static com.laser.ordermanage.ingredient.domain.QIngredient.ingredient;
import static com.laser.ordermanage.ingredient.domain.QIngredientPrice.ingredientPrice;
import static com.laser.ordermanage.ingredient.domain.QIngredientStock.ingredientStock;
import static com.laser.ordermanage.user.domain.QUserEntity.userEntity;

@Repository
@RequiredArgsConstructor
public class IngredientRepositoryCustomImpl implements IngredientRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public GetIngredientStockResponse findIngredientStockByFactoryAndDate(String email, LocalDate date, String unit) {
        // 자재 목록 조회
        List<Ingredient> ingredientList = queryFactory
                .selectFrom(ingredient)
                .join(ingredient.factory, factory)
                .join(factory.user, userEntity)
                .where(
                        ingredient.createdAt.loe(date.atTime(LocalTime.MAX)),
                        ingredient.deletedAt.isNull().or(ingredient.deletedAt.goe(date)),
                        userEntity.email.eq(email)
                )
                .fetch();

        List<GetIngredientResponse> getIngredientResponseList = new ArrayList<>();
        List<Integer> purchasePriceList = new ArrayList<>();
        List<Integer> sellPriceList = new ArrayList<>();
        List<Integer> stockCountList = new ArrayList<>();
        List<Double> stockWeightList = new ArrayList<>();

        ingredientList.forEach(
                ingredientEntity -> {
                    GetIngredientPriceResponse ingredientPriceResponse = queryFactory
                            .select(
                                    new QGetIngredientPriceResponse(
                                            ingredientPrice.purchase,
                                            ingredientPrice.sell
                                    )
                            )
                            .from(ingredientPrice)
                            .join(ingredientPrice.ingredient, ingredient)
                            .where(
                                    ingredient.id.eq(ingredientEntity.getId()),
                                    ingredientPrice.createdAt.loe(date.atTime(LocalTime.MAX))
                            )
                            .orderBy(ingredientPrice.createdAt.desc())
                            .fetchFirst();

                    IngredientStock ingredientStockEntity = queryFactory
                            .selectFrom(ingredientStock)
                            .join(ingredientStock.ingredient, ingredient)
                            .where(
                                    ingredient.id.eq(ingredientEntity.getId()),
                                    ingredientStock.createdAt.loe(date.atTime(LocalTime.MAX))
                            )
                            .orderBy(ingredientStock.createdAt.desc())
                            .fetchFirst();

                    IngredientStock ingredientPreviousStockEntity = queryFactory
                            .selectFrom(ingredientStock)
                            .join(ingredientStock.ingredient, ingredient)
                            .where(
                                    ingredient.id.eq(ingredientEntity.getId()),
                                    ingredientStock.createdAt.loe(date.minusDays(1).atTime(LocalTime.MAX))
                            )
                            .orderBy(ingredientStock.createdAt.desc())
                            .fetchFirst();

                    Assert.notNull(ingredientPriceResponse);
                    Assert.notNull(ingredientStockEntity);

                    GetIngredientStockDetailResponse getIngredientStockDetailResponse;
                    if (unit.equals("count")) {
                        getIngredientStockDetailResponse = new GetIngredientStockDetailResponse(
                                ingredientPreviousStockEntity == null ? 0 : ingredientPreviousStockEntity.getStock(),
                                ingredientStockEntity.getIncoming(),
                                ingredientStockEntity.getProduction(),
                                ingredientStockEntity.getStock(),
                                ingredientStockEntity.getOptimal()
                        );
                    } else {
                        getIngredientStockDetailResponse = new GetIngredientStockDetailResponse(
                                ingredientPreviousStockEntity == null ? 0 : ingredientPreviousStockEntity.getStock().doubleValue() * ingredientEntity.getWeight(),
                                ingredientStockEntity.getIncoming().doubleValue() * ingredientEntity.getWeight(),
                                ingredientStockEntity.getProduction().doubleValue() * ingredientEntity.getWeight(),
                                ingredientStockEntity.getStock().doubleValue() * ingredientEntity.getWeight(),
                                ingredientStockEntity.getOptimal().doubleValue() * ingredientEntity.getWeight()
                        );
                    }

                    getIngredientResponseList.add(
                            new GetIngredientResponse(
                                    ingredientEntity.getId(),
                                    ingredientEntity.getTexture(),
                                    ingredientEntity.getThickness(),
                                    ingredientEntity.getWidth(),
                                    ingredientEntity.getHeight(),
                                    getIngredientStockDetailResponse,
                                    ingredientPriceResponse
                            )
                    );

                    purchasePriceList.add(ingredientPriceResponse.purchase());
                    sellPriceList.add(ingredientPriceResponse.sell());
                    stockCountList.add(ingredientStockEntity.getStock());
                    stockWeightList.add(ingredientStockEntity.getStock().doubleValue() * ingredientEntity.getWeight());
                }
        );

        return new GetIngredientStockResponse(
                new GetIngredientPriceResponse(
                        (int)purchasePriceList.stream().mapToInt(purchasePrice -> purchasePrice).average().orElse(0),
                        (int)sellPriceList.stream().mapToInt(sellPrice -> sellPrice).average().orElse(0)
                ),
                new GetIngredientTotalStockResponse(
                        stockCountList.stream().mapToInt(stockCount -> stockCount).sum(),
                        stockWeightList.stream().mapToDouble(stockWeight -> stockWeight).sum()
                ),
                getIngredientResponseList,
                date
        );
    }
}
