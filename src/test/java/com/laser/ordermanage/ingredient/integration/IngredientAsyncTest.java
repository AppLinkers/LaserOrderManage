package com.laser.ordermanage.ingredient.integration;

import com.laser.ordermanage.OrderManageApplication;
import com.laser.ordermanage.ingredient.domain.IngredientStock;
import com.laser.ordermanage.ingredient.dto.request.UpdateIngredientStockRequest;
import com.laser.ordermanage.ingredient.dto.request.UpdateIngredientStockRequestBuilder;
import com.laser.ordermanage.ingredient.repository.IngredientStockRepository;
import com.laser.ordermanage.ingredient.service.IngredientService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest(classes = OrderManageApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
@Transactional
public class IngredientAsyncTest {

    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private IngredientStockRepository ingredientStockRepository;

    @Test
    public void 자재_재고_수정_성공_동시성_문제() throws Exception {
        // given
        final Long ingredientId = 1L;
        final UpdateIngredientStockRequest request = UpdateIngredientStockRequestBuilder.build();
        final UpdateIngredientStockRequest request2 = UpdateIngredientStockRequestBuilder.build2();

        final LocalDate nowDate = LocalDate.now();

        final int threadCount = 2;
        final ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        final CountDownLatch latch = new CountDownLatch(threadCount);

        // when - 2개의 스레드에서 동시 요청
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    ingredientService.updateIngredientStock(ingredientId, request);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // then
        // ingredient-id 에 대한 자재 재고 조회 시, 단일 결과가 나와야 한다.
        ingredientService.updateIngredientStock(ingredientId, request2);

        Optional<IngredientStock> optionalIngredientStock = ingredientStockRepository.findByIngredientIdAndCreatedAt(ingredientId, nowDate);
        Assertions.assertThat(optionalIngredientStock.isPresent()).isTrue();
    }

    // @Test
    public void 자재_재고_수정_실패_동시성_문제() throws Exception {
        // given
        final Long ingredientId = 1L;
        final UpdateIngredientStockRequest request = UpdateIngredientStockRequestBuilder.build();
        final UpdateIngredientStockRequest request2 = UpdateIngredientStockRequestBuilder.build2();

        final LocalDate nowDate = LocalDate.now();

        final int threadCount = 2;
        final ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        final CountDownLatch latch = new CountDownLatch(threadCount);

        // when - 2개의 스레드에서 동시 요청
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    ingredientService.updateIngredientStock(ingredientId, request);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // then
        // ingredient-id 에 대한 자재 재고 조회 시, 단일 결과가 나와야 한다. -> but, 2개의 데이터가 존재하기 때문에 에러 발생
        Assertions.assertThatThrownBy(() -> ingredientService.updateIngredientStock(ingredientId, request2))
                .isInstanceOf(IncorrectResultSizeDataAccessException.class)
                .hasMessage("query did not return a unique result: 2");

        Assertions.assertThatThrownBy(() -> ingredientStockRepository.findByIngredientIdAndCreatedAt(ingredientId, nowDate))
                .isInstanceOf(IncorrectResultSizeDataAccessException.class)
                .hasMessage("query did not return a unique result: 2");
    }
}
