package com.laser.ordermanage.order.repository;

import com.laser.ordermanage.order.dto.response.GetCommentResponse;
import com.laser.ordermanage.order.dto.response.QGetCommentResponse;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.laser.ordermanage.customer.domain.QCustomer.customer;
import static com.laser.ordermanage.factory.domain.QFactory.factory;
import static com.laser.ordermanage.order.domain.QComment.comment;
import static com.laser.ordermanage.user.domain.QUserEntity.userEntity;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<GetCommentResponse> findCommentByUserAndOrder(User user, Long orderId) {
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
                .fetch();

        return getCommentResponseList;
    }
}
