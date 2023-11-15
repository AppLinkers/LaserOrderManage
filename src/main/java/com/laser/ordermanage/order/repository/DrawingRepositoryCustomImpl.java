package com.laser.ordermanage.order.repository;

import com.laser.ordermanage.customer.dto.response.CustomerGetDrawingResponse;
import com.laser.ordermanage.customer.dto.response.QCustomerGetDrawingResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.laser.ordermanage.customer.domain.QCustomer.customer;
import static com.laser.ordermanage.order.domain.QDrawing.drawing;
import static com.laser.ordermanage.order.domain.QOrder.order;
import static com.laser.ordermanage.user.domain.QUserEntity.userEntity;

@RequiredArgsConstructor
@Repository
public class DrawingRepositoryCustomImpl implements DrawingRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CustomerGetDrawingResponse> findByCustomerAndOrder(String userName, Long orderId) {
        List<CustomerGetDrawingResponse> customerGetDrawingResponseList = queryFactory
                .select(new QCustomerGetDrawingResponse(
                        drawing.id,
                        drawing.fileName,
                        drawing.fileSize,
                        drawing.fileType,
                        drawing.fileUrl,
                        drawing.thumbnailUrl,
                        drawing.count,
                        drawing.ingredient
                ))
                .from(drawing)
                .leftJoin(drawing.order, order)
                .leftJoin(order.customer, customer)
                .leftJoin(customer.user, userEntity)
                .where(
                        userEntity.email.eq("user1@gmail.com"),
                        drawing.order.id.eq(orderId)
                )
                .fetch();

        return customerGetDrawingResponseList;
    }
}
