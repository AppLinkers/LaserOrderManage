package com.laser.ordermanage.order.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.laser.ordermanage.order.domain.QDrawing.drawing;

@RequiredArgsConstructor
public class DrawingRepositoryCustomImpl implements DrawingRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public void deleteAllByOrder(Long orderId) {
        queryFactory
                .delete(drawing)
                .where(drawing.order.id.eq(orderId))
                .execute();
    }
}
