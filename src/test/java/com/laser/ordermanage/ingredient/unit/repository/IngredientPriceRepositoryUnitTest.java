package com.laser.ordermanage.ingredient.unit.repository;

import com.laser.ordermanage.common.RepositoryUnitTest;
import com.laser.ordermanage.ingredient.domain.IngredientPrice;
import com.laser.ordermanage.ingredient.domain.IngredientPriceBuilder;
import com.laser.ordermanage.ingredient.repository.IngredientPriceRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.LocalDate;
import java.util.Optional;

@EnableJpaRepositories(basePackageClasses = IngredientPriceRepository.class)
public class IngredientPriceRepositoryUnitTest extends RepositoryUnitTest {

    @Autowired
    private IngredientPriceRepository ingredientPriceRepository;

    @Test
    public void findByIngredientIdAndCreatedAt_존재_O() {
        // given
        final Long ingredientId = 1L;
        final LocalDate date = LocalDate.of(2024, 6, 1);
        final Long expectedIngredientPriceId = 230L;
        final IngredientPrice expectedIngredientPrice = IngredientPriceBuilder.build();

        // when
        Optional<IngredientPrice> optionalIngredientPrice = ingredientPriceRepository.findByIngredientIdAndCreatedAt(ingredientId, date);

        // then
        Assertions.assertThat(optionalIngredientPrice.isPresent()).isTrue();
        optionalIngredientPrice.ifPresent(
                actualIngredientPrice -> {
                    Assertions.assertThat(actualIngredientPrice.getId()).isEqualTo(expectedIngredientPriceId);
                    IngredientPriceBuilder.assertIngredientPrice(actualIngredientPrice, expectedIngredientPrice);
                }
        );
    }

    @Test
    public void findByIngredientIdAndCreatedAt_존재_X() {
        // given
        final Long unknownIngredientId = 0L;
        final LocalDate date = LocalDate.of(2023, 1, 1);

        // when
        Optional<IngredientPrice> optionalIngredientPrice = ingredientPriceRepository.findByIngredientIdAndCreatedAt(unknownIngredientId, date);

        // then
        Assertions.assertThat(optionalIngredientPrice.isEmpty()).isTrue();
    }

    @Test
    public void findFirstByIngredientIdOrderByCreatedAtDesc() {
        // given
        final Long ingredientId = 1L;
        final Long expectedIngredientPriceId = 230L;
        final IngredientPrice expectedIngredientPrice = IngredientPriceBuilder.build();

        // when
        IngredientPrice actualIngredientPrice = ingredientPriceRepository.findFirstByIngredientIdOrderByCreatedAtDesc(ingredientId);

        // then
        Assertions.assertThat(actualIngredientPrice.getId()).isEqualTo(expectedIngredientPriceId);
        IngredientPriceBuilder.assertIngredientPrice(actualIngredientPrice, expectedIngredientPrice);
    }
}
