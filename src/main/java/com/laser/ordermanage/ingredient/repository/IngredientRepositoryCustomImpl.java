package com.laser.ordermanage.ingredient.repository;

import com.laser.ordermanage.ingredient.domain.Ingredient;
import com.laser.ordermanage.ingredient.domain.IngredientStock;
import com.laser.ordermanage.ingredient.dto.response.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.laser.ordermanage.factory.domain.QFactory.factory;
import static com.laser.ordermanage.ingredient.domain.QIngredient.ingredient;
import static com.laser.ordermanage.ingredient.domain.QIngredientPrice.ingredientPrice;
import static com.laser.ordermanage.ingredient.domain.QIngredientStock.ingredientStock;
import static com.laser.ordermanage.user.domain.QUserEntity.userEntity;

@RequiredArgsConstructor
public class IngredientRepositoryCustomImpl implements IngredientRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public GetIngredientStockResponse findIngredientStockByFactoryAndDate(String email, LocalDate date) {
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
        List<Number> stockWeightList = new ArrayList<>();

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
                                    ingredientPrice.createdAt.loe(date)
                            )
                            .orderBy(ingredientPrice.createdAt.desc())
                            .fetchFirst();

                    IngredientStock ingredientPreviousStockEntity = queryFactory
                            .selectFrom(ingredientStock)
                            .join(ingredientStock.ingredient, ingredient)
                            .where(
                                    ingredient.id.eq(ingredientEntity.getId()),
                                    ingredientStock.createdAt.loe(date.minusDays(1))
                            )
                            .orderBy(ingredientStock.createdAt.desc())
                            .fetchFirst();

                    IngredientStock ingredientStockEntity = queryFactory
                            .selectFrom(ingredientStock)
                            .join(ingredientStock.ingredient, ingredient)
                            .where(
                                    ingredient.id.eq(ingredientEntity.getId()),
                                    ingredientStock.createdAt.eq(date)
                            )
                            .orderBy(ingredientStock.createdAt.desc())
                            .fetchFirst();

                    if (ingredientStockEntity == null) {
                        ingredientStockEntity = IngredientStock.builder()
                                .ingredient(null)
                                .incoming(0)
                                .production(0)
                                .stock(ingredientPreviousStockEntity.getStock())
                                .optimal(ingredientPreviousStockEntity.getOptimal())
                                .build();
                    }
                    BigDecimal ingredientWeight = BigDecimal.valueOf(ingredientEntity.getWeight());

                    GetIngredientStockDetailResponse getIngredientStockCountDetailResponse = new GetIngredientStockDetailResponse(
                            ingredientPreviousStockEntity == null ? 0 : ingredientPreviousStockEntity.getStock(),
                            ingredientStockEntity.getIncoming(),
                            ingredientStockEntity.getProduction(),
                            ingredientStockEntity.getStock(),
                            ingredientStockEntity.getOptimal()
                    );

                    GetIngredientStockDetailResponse getIngredientStockWeightDetailResponse = new GetIngredientStockDetailResponse(
                            ingredientPreviousStockEntity == null ? 0 : ingredientWeight.multiply(BigDecimal.valueOf(ingredientPreviousStockEntity.getStock())),
                            ingredientWeight.multiply(BigDecimal.valueOf(ingredientStockEntity.getIncoming())),
                            ingredientWeight.multiply(BigDecimal.valueOf(ingredientStockEntity.getProduction())),
                            ingredientWeight.multiply(BigDecimal.valueOf(ingredientStockEntity.getStock())),
                            ingredientWeight.multiply(BigDecimal.valueOf(ingredientStockEntity.getOptimal()))
                    );

                    getIngredientResponseList.add(
                            new GetIngredientResponse(
                                    ingredientEntity.getId(),
                                    ingredientEntity.getTexture(),
                                    ingredientEntity.getThickness(),
                                    ingredientEntity.getWidth(),
                                    ingredientEntity.getHeight(),
                                    ingredientEntity.getWeight(),
                                    getIngredientStockCountDetailResponse,
                                    getIngredientStockWeightDetailResponse,
                                    ingredientPriceResponse
                            )
                    );

                    purchasePriceList.add(ingredientPriceResponse.purchase());
                    sellPriceList.add(ingredientPriceResponse.sell());
                    stockCountList.add(ingredientStockEntity.getStock());
                    stockWeightList.add(ingredientWeight.multiply(BigDecimal.valueOf(ingredientStockEntity.getStock())));
                }
        );

        return new GetIngredientStockResponse(
                new GetIngredientPriceResponse(
                        (int)purchasePriceList.stream().mapToInt(purchasePrice -> purchasePrice).average().orElse(0),
                        (int)sellPriceList.stream().mapToInt(sellPrice -> sellPrice).average().orElse(0)
                ),
                new GetIngredientTotalStockResponse(
                        stockCountList.stream().mapToInt(stockCount -> stockCount).sum(),
                        stockWeightList.stream().mapToDouble(stockWeight -> stockWeight.doubleValue()).sum()
                ),
                getIngredientResponseList,
                date
        );
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
