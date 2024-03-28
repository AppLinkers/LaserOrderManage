package com.laser.ordermanage.order.unit.repository;

import com.laser.ordermanage.common.RepositoryUnitTest;
import com.laser.ordermanage.order.domain.Drawing;
import com.laser.ordermanage.order.domain.DrawingBuilder;
import com.laser.ordermanage.order.repository.DrawingRepository;
import com.laser.ordermanage.order.unit.domain.DrawingUnitTest;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;
import java.util.Optional;

@EnableJpaRepositories(basePackageClasses = DrawingRepository.class)
public class DrawingRepositoryUnitTest extends RepositoryUnitTest {

    @Autowired
    protected DrawingRepository drawingRepository;

    @Autowired
    protected JPAQueryFactory queryFactory;

    @Test
    public void findFirstById_존재_O() {
        // given
        final Long expectedDrawingId = 1L;
        final Drawing expectedDrawing = DrawingBuilder.build();

        // when
        final Optional<Drawing> optionalDrawing = drawingRepository.findFirstById(expectedDrawingId);

        // then
        Assertions.assertThat(optionalDrawing.isPresent()).isTrue();
        optionalDrawing.ifPresent(
                actualDrawing -> {
                    Assertions.assertThat(actualDrawing.getId()).isEqualTo(expectedDrawingId);
                    DrawingUnitTest.assertDrawing(actualDrawing, expectedDrawing);
                }
        );
    }

    @Test
    public void findFirstById_존재_X() {
        // given
        final Long unknownDrawingId = 0L;

        // when
        final Optional<Drawing> optionalDrawing = drawingRepository.findFirstById(unknownDrawingId);

        // then
        Assertions.assertThat(optionalDrawing.isEmpty()).isTrue();
    }

    @Test
    public void countByOrderId() {
        // given
        final Long orderId = 1L;
        final Integer expectedCountDrawing = 1;

        // when
        final Integer actualCountDrawing = drawingRepository.countByOrderId(orderId);

        // then
        Assertions.assertThat(actualCountDrawing).isEqualTo(expectedCountDrawing);
    }

    @Test
    public void deleteAllByOrder() {
        // given
        final Long orderId = 5L;
        final Integer countDrawingBeforeDelete = drawingRepository.countByOrderId(orderId);
        Assertions.assertThat(countDrawingBeforeDelete).isGreaterThan(0);
        final Integer expectedCountDrawing = 0;

        // when
        drawingRepository.deleteAllByOrder(orderId);

        // then
        final Integer actualCountDrawing = drawingRepository.countByOrderId(orderId);
        Assertions.assertThat(actualCountDrawing).isEqualTo(expectedCountDrawing);
    }

    @Test
    public void deleteAllByOrderList() {
        // given
        final List<Long> orderIdList = List.of(1L, 5L);
        orderIdList.forEach(
                orderId -> {
                    final Integer countDrawingBeforeDelete = drawingRepository.countByOrderId(orderId);
                    Assertions.assertThat(countDrawingBeforeDelete).isGreaterThan(0);
                }
        );
        final Integer expectedCountDrawing = 0;

        // when
        drawingRepository.deleteAllByOrderList(orderIdList);

        // then
        orderIdList.forEach(
                orderId -> {
                    final Integer countDrawingBeforeDelete = drawingRepository.countByOrderId(orderId);
                    Assertions.assertThat(countDrawingBeforeDelete).isEqualTo(expectedCountDrawing);
                }
        );
    }
}
