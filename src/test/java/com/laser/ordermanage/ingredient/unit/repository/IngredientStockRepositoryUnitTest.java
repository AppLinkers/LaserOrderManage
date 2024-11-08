package com.laser.ordermanage.ingredient.unit.repository;

import com.laser.ordermanage.common.RepositoryUnitTest;
import com.laser.ordermanage.ingredient.domain.IngredientStock;
import com.laser.ordermanage.ingredient.domain.IngredientStockBuilder;
import com.laser.ordermanage.ingredient.repository.IngredientStockRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.LocalDate;
import java.util.Optional;

@EnableJpaRepositories(basePackageClasses = IngredientStockRepository.class)
public class IngredientStockRepositoryUnitTest extends RepositoryUnitTest {

    @Autowired
    private IngredientStockRepository ingredientStockRepository;

    @Test
    public void findByIngredientIdAndCreatedAt_존재_O() {
        // given
        final Long ingredientId = 1L;
        final LocalDate date = LocalDate.of(2024, 6, 1);
        final Long expectedIngredientStockId = 230L;
        final IngredientStock expectedIngredientStock = IngredientStockBuilder.build();

        // when
        Optional<IngredientStock> optionalIngredientStock = ingredientStockRepository.findByIngredientIdAndCreatedAt(ingredientId, date);

        // then
        Assertions.assertThat(optionalIngredientStock.isPresent()).isTrue();
        optionalIngredientStock.ifPresent(
                actualIngredientStock -> {
                    Assertions.assertThat(actualIngredientStock.getId()).isEqualTo(expectedIngredientStockId);
                    IngredientStockBuilder.assertIngredientStock(actualIngredientStock, expectedIngredientStock);
                }
        );
    }

    @Test
    public void findByIngredientIdAndCreatedAt_존재_X() {
        // given
        final Long unknownIngredientId = 0L;
        final LocalDate date = LocalDate.of(2024, 6, 1);

        // when
        Optional<IngredientStock> optionalIngredientStock = ingredientStockRepository.findByIngredientIdAndCreatedAt(unknownIngredientId, date);

        // then
        Assertions.assertThat(optionalIngredientStock.isEmpty()).isTrue();
    }

    @Test
    public void findFirstByIngredientIdOrderByCreatedAtDesc() {
        // given
        final Long ingredientId = 1L;
        final Long expectedIngredientStockId = 230L;
        final IngredientStock expectedIngredientStock = IngredientStockBuilder.build();

        // when
        IngredientStock actualIngredientStock = ingredientStockRepository.findFirstByIngredientIdOrderByCreatedAtDesc(ingredientId);

        // then
        Assertions.assertThat(actualIngredientStock.getId()).isEqualTo(expectedIngredientStockId);
        IngredientStockBuilder.assertIngredientStock(actualIngredientStock, expectedIngredientStock);
    }

    @Test
    public void findPreviousByIngredientIdAndDate() {
        // given
        final Long ingredientId = 1L;
        final LocalDate date = LocalDate.of(2023, 10, 1);
        final Long expectedIngredientStockId = 21L;
        final IngredientStock expectedIngredientStock = ingredientStockRepository.findPreviousByIngredientIdAndDate(ingredientId, date);

        // when
        IngredientStock actualIngredientStock = ingredientStockRepository.findPreviousByIngredientIdAndDate(ingredientId, date);

        // then
        Assertions.assertThat(actualIngredientStock.getId()).isEqualTo(expectedIngredientStockId);
        IngredientStockBuilder.assertIngredientStock(actualIngredientStock, expectedIngredientStock);
    }
}
