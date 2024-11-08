package com.laser.ordermanage.ingredient.unit.repository;

import com.laser.ordermanage.common.RepositoryUnitTest;
import com.laser.ordermanage.ingredient.domain.Ingredient;
import com.laser.ordermanage.ingredient.domain.IngredientBuilder;
import com.laser.ordermanage.ingredient.dto.response.*;
import com.laser.ordermanage.ingredient.repository.IngredientRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@EnableJpaRepositories(basePackageClasses = IngredientRepository.class)
public class IngredientRepositoryUnitTest extends RepositoryUnitTest {

    @Autowired
    private IngredientRepository ingredientRepository;

    private final static String email = "admin@kumoh.org";

    private final static Long ingredientId = 1L;

    private final static LocalDate startYear = LocalDate.of(2023, 1, 1);
    private final static LocalDate endYear = LocalDate.of(2024, 1, 1);

    private final static LocalDate startYearMonth = LocalDate.of(2023, 1, 1);
    private final static LocalDate endYearMonth = LocalDate.of(2023, 12, 1);

    private final static List<String> stockItemTypeList = List.of("incoming", "production", "stock", "optimal");
    private final static List<String> priceItemTypeList = List.of("purchase", "sell");

    private final static String countStockUnit = "count";
    private final static String weightStockUnit = "weight";

    @Test
    public void findFirstById_존재_O() {
        // given
        final Long expectedIngredientId = 1L;
        final Ingredient expectedIngredient = IngredientBuilder.build();

        // when
        final Optional<Ingredient> optionalIngredient = ingredientRepository.findFirstById(expectedIngredientId);

        // then
        Assertions.assertThat(optionalIngredient.isPresent()).isTrue();
        optionalIngredient.ifPresent(
                actualIngredient -> {
                    Assertions.assertThat(actualIngredient.getId()).isEqualTo(expectedIngredientId);
                    IngredientBuilder.assertIngredient(actualIngredient, expectedIngredient);
                }
        );
    }

    @Test
    public void findFirstById_존재_X() {
        // given
        final Long unknownIngredientId = 0L;

        // when
        final Optional<Ingredient> optionalIngredient = ingredientRepository.findFirstById(unknownIngredientId);

        // then
        Assertions.assertThat(optionalIngredient.isEmpty()).isTrue();
    }

    @Test
    public void findByDeletedAtIsNull() {
        // given

        // when
        final List<Ingredient> actualIngredientList = ingredientRepository.findByDeletedAtIsNull();

        // then
        Assertions.assertThat(actualIngredientList.size()).isEqualTo(9);
    }

    @Test
    public void findIngredientStatusByFactoryAndDate() {
        // given
        final LocalDate date = LocalDate.of(2024, 4, 1);
        final List<GetIngredientResponse> expectedIngredientList = GetIngredientResponseBuilder.buildList();

        // when
        final List<GetIngredientResponse> actualIngredientList = ingredientRepository.findIngredientStatusByFactoryAndDate(email, date);

        // then
        Assertions.assertThat(actualIngredientList).isEqualTo(expectedIngredientList);
    }

    @Test
    public void findIngredientByFactoryManager() {
        // given
        final List<GetIngredientInfoResponse> expectedIngredientInfoList = GetIngredientInfoResponseBuilder.buildList();

        // when
        final List<GetIngredientInfoResponse> actualIngredientInfoList = ingredientRepository.findIngredientByFactoryManager(email);

        // then
        Assertions.assertThat(actualIngredientInfoList).isEqualTo(expectedIngredientInfoList);
    }

    // total, month, stock, count
    @Test
    public void findIngredientAnalysisAsTotalAndMonthAndStockByFactoryManager_count() {
        // given
        final List<GetIngredientAnalysisItemResponse> expectedIngredientAnalysisItemList = GetIngredientAnalysisItemResponseBuilder.buildList4();

        // when
        final List<GetIngredientAnalysisItemResponse> actualIngredientAnalysisItemList = ingredientRepository.findIngredientAnalysisAsTotalAndMonthAndStockByFactoryManager(email, startYearMonth, endYearMonth, stockItemTypeList, countStockUnit);

        // then
        assertIngredientAnalysisItemList(actualIngredientAnalysisItemList, expectedIngredientAnalysisItemList);
    }

    // total, month, stock, weight
    @Test
    public void findIngredientAnalysisAsTotalAndMonthAndStockByFactoryManager_weight() {
        // given
        final List<GetIngredientAnalysisItemResponse> expectedIngredientAnalysisItemList = GetIngredientAnalysisItemResponseBuilder.buildList5();

        // when
        final List<GetIngredientAnalysisItemResponse> actualIngredientAnalysisItemList = ingredientRepository.findIngredientAnalysisAsTotalAndMonthAndStockByFactoryManager(email, startYearMonth, endYearMonth, stockItemTypeList, weightStockUnit);

        // then
        assertIngredientAnalysisItemList(actualIngredientAnalysisItemList, expectedIngredientAnalysisItemList);
    }

    // total, month, price
    @Test
    public void findIngredientAnalysisAsTotalAndMonthAndPriceByFactoryManager() {
        // given
        final List<GetIngredientAnalysisItemResponse> expectedIngredientAnalysisItemList = GetIngredientAnalysisItemResponseBuilder.buildList6();

        // when
        final List<GetIngredientAnalysisItemResponse> actualIngredientAnalysisItemList = ingredientRepository.findIngredientAnalysisAsTotalAndMonthAndPriceByFactoryManager(email, startYearMonth, endYearMonth, priceItemTypeList);

        // then
        assertIngredientAnalysisItemList(actualIngredientAnalysisItemList, expectedIngredientAnalysisItemList);
    }

    // total, year, stock, count
    @Test
    public void findIngredientAnalysisAsTotalAndYearAndStockByFactoryManager_count() {
        // given
        final List<GetIngredientAnalysisItemResponse> expectedIngredientAnalysisItemList = GetIngredientAnalysisItemResponseBuilder.buildList1();

        // when
        final List<GetIngredientAnalysisItemResponse> actualIngredientAnalysisItemList = ingredientRepository.findIngredientAnalysisAsTotalAndYearAndStockByFactoryManager(email, startYear, endYear, stockItemTypeList, countStockUnit);

        // then
        assertIngredientAnalysisItemList(actualIngredientAnalysisItemList, expectedIngredientAnalysisItemList);
    }

    // total, year, stock, weight
    @Test
    public void findIngredientAnalysisAsTotalAndYearAndStockByFactoryManager_weight() {
        // given
        final List<GetIngredientAnalysisItemResponse> expectedIngredientAnalysisItemList = GetIngredientAnalysisItemResponseBuilder.buildList2();

        // when
        final List<GetIngredientAnalysisItemResponse> actualIngredientAnalysisItemList = ingredientRepository.findIngredientAnalysisAsTotalAndYearAndStockByFactoryManager(email, startYear, endYear, stockItemTypeList, weightStockUnit);

        // then
        assertIngredientAnalysisItemList(actualIngredientAnalysisItemList, expectedIngredientAnalysisItemList);
    }

    // total, year, price
    @Test
    public void findIngredientAnalysisAsTotalAndYearAndPriceByFactoryManager() {
        // given
        final List<GetIngredientAnalysisItemResponse> expectedIngredientAnalysisItemList = GetIngredientAnalysisItemResponseBuilder.buildList3();

        // when
        final List<GetIngredientAnalysisItemResponse> actualIngredientAnalysisItemList = ingredientRepository.findIngredientAnalysisAsTotalAndYearAndPriceByFactoryManager(email, startYear, endYear, priceItemTypeList);

        // then
        assertIngredientAnalysisItemList(actualIngredientAnalysisItemList, expectedIngredientAnalysisItemList);
    }

    // average, month, stock, count
    @Test
    public void findIngredientAnalysisAsAverageAndMonthAndStockByFactoryManager_count() {
        // given
        final List<GetIngredientAnalysisItemResponse> expectedIngredientAnalysisItemList = GetIngredientAnalysisItemResponseBuilder.buildList10();

        // when
        final List<GetIngredientAnalysisItemResponse> actualIngredientAnalysisItemList = ingredientRepository.findIngredientAnalysisAsAverageAndMonthAndStockByFactoryManager(email, startYearMonth, endYearMonth, stockItemTypeList, countStockUnit);

        // then
        assertIngredientAnalysisItemList(actualIngredientAnalysisItemList, expectedIngredientAnalysisItemList);
    }

    // average, month, stock, weight
    @Test
    public void findIngredientAnalysisAsAverageAndMonthAndStockByFactoryManager_weight() {
        // given
        final List<GetIngredientAnalysisItemResponse> expectedIngredientAnalysisItemList = GetIngredientAnalysisItemResponseBuilder.buildList11();

        // when
        final List<GetIngredientAnalysisItemResponse> actualIngredientAnalysisItemList = ingredientRepository.findIngredientAnalysisAsAverageAndMonthAndStockByFactoryManager(email, startYearMonth, endYearMonth, stockItemTypeList, weightStockUnit);

        // then
        assertIngredientAnalysisItemList(actualIngredientAnalysisItemList, expectedIngredientAnalysisItemList);
    }

    // average, month, price
    @Test
    public void findIngredientAnalysisAsAverageAndMonthAndPriceByFactoryManager() {
        // given
        final List<GetIngredientAnalysisItemResponse> expectedIngredientAnalysisItemList = GetIngredientAnalysisItemResponseBuilder.buildList12();

        // when
        final List<GetIngredientAnalysisItemResponse> actualIngredientAnalysisItemList = ingredientRepository.findIngredientAnalysisAsAverageAndMonthAndPriceByFactoryManager(email, startYearMonth, endYearMonth, priceItemTypeList);

        // then
        assertIngredientAnalysisItemList(actualIngredientAnalysisItemList, expectedIngredientAnalysisItemList);
    }

    // average, year, stock, count
    @Test
    public void findIngredientAnalysisAsAverageAndYearAndStockByFactoryManager_count() {
        // given
        final List<GetIngredientAnalysisItemResponse> expectedIngredientAnalysisItemList = GetIngredientAnalysisItemResponseBuilder.buildList7();

        // when
        final List<GetIngredientAnalysisItemResponse> actualIngredientAnalysisItemList = ingredientRepository.findIngredientAnalysisAsAverageAndYearAndStockByFactoryManager(email, startYear, endYear, stockItemTypeList, countStockUnit);

        // then
        assertIngredientAnalysisItemList(actualIngredientAnalysisItemList, expectedIngredientAnalysisItemList);
    }

    // average, year, stock, weight
    @Test
    public void findIngredientAnalysisAsAverageAndYearAndStockByFactoryManager_weight() {
        // given
        final List<GetIngredientAnalysisItemResponse> expectedIngredientAnalysisItemList = GetIngredientAnalysisItemResponseBuilder.buildList8();

        // when
        final List<GetIngredientAnalysisItemResponse> actualIngredientAnalysisItemList = ingredientRepository.findIngredientAnalysisAsAverageAndYearAndStockByFactoryManager(email, startYear, endYear, stockItemTypeList, weightStockUnit);

        // then
        assertIngredientAnalysisItemList(actualIngredientAnalysisItemList, expectedIngredientAnalysisItemList);
    }

    // average, year, price
    @Test
    public void findIngredientAnalysisAsAverageAndYearAndPriceByFactoryManager() {
        // given
        final List<GetIngredientAnalysisItemResponse> expectedIngredientAnalysisItemList = GetIngredientAnalysisItemResponseBuilder.buildList9();

        // when
        final List<GetIngredientAnalysisItemResponse> actualIngredientAnalysisItemList = ingredientRepository.findIngredientAnalysisAsAverageAndYearAndPriceByFactoryManager(email, startYear, endYear, priceItemTypeList);

        // then
        assertIngredientAnalysisItemList(actualIngredientAnalysisItemList, expectedIngredientAnalysisItemList);
    }

    // ingredient, month, stock, count
    @Test
    public void findIngredientAnalysisAsIngredientAndMonthAndStock_count() {
        // given
        final List<GetIngredientAnalysisItemResponse> expectedIngredientAnalysisItemList = GetIngredientAnalysisItemResponseBuilder.buildList16();

        // when
        final List<GetIngredientAnalysisItemResponse> actualIngredientAnalysisItemList = ingredientRepository.findIngredientAnalysisAsIngredientAndMonthAndStock(ingredientId, startYearMonth, endYearMonth, stockItemTypeList, countStockUnit);

        // then
        assertIngredientAnalysisItemList(actualIngredientAnalysisItemList, expectedIngredientAnalysisItemList);
    }

    // ingredient, month, stock, weight
    @Test
    public void findIngredientAnalysisAsIngredientAndMonthAndStock_weight() {
        // given
        final List<GetIngredientAnalysisItemResponse> expectedIngredientAnalysisItemList = GetIngredientAnalysisItemResponseBuilder.buildList17();

        // when
        final List<GetIngredientAnalysisItemResponse> actualIngredientAnalysisItemList = ingredientRepository.findIngredientAnalysisAsIngredientAndMonthAndStock(ingredientId, startYearMonth, endYearMonth, stockItemTypeList, weightStockUnit);

        // then
        assertIngredientAnalysisItemList(actualIngredientAnalysisItemList, expectedIngredientAnalysisItemList);
    }

    // ingredient, month, price
    @Test
    public void findIngredientAnalysisAsIngredientAndMonthAndPrice() {
        // given
        final List<GetIngredientAnalysisItemResponse> expectedIngredientAnalysisItemList = GetIngredientAnalysisItemResponseBuilder.buildList18();

        // when
        final List<GetIngredientAnalysisItemResponse> actualIngredientAnalysisItemList = ingredientRepository.findIngredientAnalysisAsIngredientAndMonthAndPrice(ingredientId, startYearMonth, endYearMonth, priceItemTypeList);

        // then
        assertIngredientAnalysisItemList(actualIngredientAnalysisItemList, expectedIngredientAnalysisItemList);
    }

    // ingredient, year, stock, count
    @Test
    public void findIngredientAnalysisAsIngredientAndYearAndStock_count() {
        // given
        final List<GetIngredientAnalysisItemResponse> expectedIngredientAnalysisItemList = GetIngredientAnalysisItemResponseBuilder.buildList13();

        // when
        final List<GetIngredientAnalysisItemResponse> actualIngredientAnalysisItemList = ingredientRepository.findIngredientAnalysisAsIngredientAndYearAndStock(ingredientId, startYear, endYear, stockItemTypeList, countStockUnit);

        // then
        assertIngredientAnalysisItemList(actualIngredientAnalysisItemList, expectedIngredientAnalysisItemList);
    }

    // ingredient, year, stock, weight
    @Test
    public void findIngredientAnalysisAsIngredientAndYearAndStock_weight() {
        // given
        final List<GetIngredientAnalysisItemResponse> expectedIngredientAnalysisItemList = GetIngredientAnalysisItemResponseBuilder.buildList14();

        // when
        final List<GetIngredientAnalysisItemResponse> actualIngredientAnalysisItemList = ingredientRepository.findIngredientAnalysisAsIngredientAndYearAndStock(ingredientId, startYear, endYear, stockItemTypeList, weightStockUnit);

        // then
        assertIngredientAnalysisItemList(actualIngredientAnalysisItemList, expectedIngredientAnalysisItemList);
    }

    // ingredient, year, price
    @Test
    public void findIngredientAnalysisAsIngredientAndYearAndPrice() {
        // given
        final List<GetIngredientAnalysisItemResponse> expectedIngredientAnalysisItemList = GetIngredientAnalysisItemResponseBuilder.buildList15();

        // when
        final List<GetIngredientAnalysisItemResponse> actualIngredientAnalysisItemList = ingredientRepository.findIngredientAnalysisAsIngredientAndYearAndPrice(ingredientId, startYear, endYear, priceItemTypeList);

        // then
        assertIngredientAnalysisItemList(actualIngredientAnalysisItemList, expectedIngredientAnalysisItemList);
    }

    private static void assertIngredientAnalysisItemList(List<GetIngredientAnalysisItemResponse> actualIngredientAnalysisItemList, List<GetIngredientAnalysisItemResponse> expectedIngredientAnalysisItemList) {
        // data 비교
        Assertions.assertThat(actualIngredientAnalysisItemList.stream()
                        .map(GetIngredientAnalysisItemResponse::item)
                        .collect(Collectors.toList()))
                .isEqualTo(expectedIngredientAnalysisItemList.stream()
                        .map(GetIngredientAnalysisItemResponse::item)
                        .collect(Collectors.toList()));

        // item 비교
        Assertions.assertThat(actualIngredientAnalysisItemList.stream()
                        .map(value -> value.data().stream().map(Number::doubleValue).collect(Collectors.toList()))
                        .collect(Collectors.toList()))
                .isEqualTo(expectedIngredientAnalysisItemList.stream()
                        .map(value -> value.data().stream().map(Number::doubleValue).collect(Collectors.toList()))
                        .collect(Collectors.toList()));
    }
}
