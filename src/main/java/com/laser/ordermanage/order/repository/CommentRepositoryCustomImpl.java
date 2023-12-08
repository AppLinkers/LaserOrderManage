package com.laser.ordermanage.order.repository;

import com.laser.ordermanage.order.dto.response.GetCommentResponse;
import com.laser.ordermanage.order.dto.response.QGetCommentResponse;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.laser.ordermanage.customer.domain.QCustomer.customer;
import static com.laser.ordermanage.factory.domain.QFactory.factory;
import static com.laser.ordermanage.order.domain.QComment.comment;
import static com.laser.ordermanage.order.domain.QOrder.order;
import static com.laser.ordermanage.user.domain.QUserEntity.userEntity;

@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<GetCommentResponse> findCommentByOrder(Long orderId) {

        List<GetCommentResponse> getCommentResponseList = queryFactory
                .select(new QGetCommentResponse(
                        comment.id,
                        new CaseBuilder()
                                .when(factory.isNotNull())
                                .then(factory.companyName)
                                .otherwise(customer.name),
                        comment.content,
                        comment.createdAt
                ))
                .from(comment)
                .join(comment.user, userEntity)
                .leftJoin(factory).on(factory.user.id.eq(userEntity.id))
                .leftJoin(customer).on(customer.user.id.eq(userEntity.id))
                .join(comment.order, order)
                .where(order.id.eq(orderId))
                .orderBy(comment.createdAt.asc())
                .fetch();

        return getCommentResponseList;
    }
}
