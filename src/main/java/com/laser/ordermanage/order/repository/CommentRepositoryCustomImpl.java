package com.laser.ordermanage.order.repository;

import com.laser.ordermanage.customer.domain.QCustomer;
import com.laser.ordermanage.order.dto.response.GetCommentResponse;
import com.laser.ordermanage.order.dto.response.QGetCommentResponse;
import com.laser.ordermanage.user.domain.QUserEntity;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

import static com.laser.ordermanage.factory.domain.QFactory.factory;
import static com.laser.ordermanage.order.domain.QComment.comment;
import static com.laser.ordermanage.order.domain.QOrder.order;

@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<GetCommentResponse> findCommentByUserAndOrder(User user, Long orderId) {

        QUserEntity userEntityOfComment = new QUserEntity("userEntityOfComment");
        QUserEntity userEntityOfOrder = new QUserEntity("userEntityOfOrder");

        QCustomer customerOfComment = new QCustomer("customerOfComment");
        QCustomer customerOfOrder = new QCustomer("customerOfOrder");

        List<GetCommentResponse> getCommentResponseList = queryFactory
                .select(new QGetCommentResponse(
                        comment.id,
                        new CaseBuilder()
                                .when(factory.isNotNull())
                                .then(factory.companyName)
                                .otherwise(customerOfComment.name),
                        comment.content,
                        comment.createdAt
                ))
                .from(comment)
                .join(comment.user, userEntityOfComment)
                .leftJoin(factory).on(factory.user.id.eq(userEntityOfComment.id))
                .leftJoin(customerOfComment).on(customerOfComment.user.id.eq(userEntityOfComment.id))
                .join(comment.order, order)
                .join(order.customer, customerOfOrder)
                .join(customerOfOrder.user, userEntityOfOrder)
                .where(order.id.eq(orderId))
                .where(userEntityOfOrder.email.eq(user.getUsername()).or(isUserRoleFactory(user)))
                .orderBy(comment.createdAt.asc())
                .fetch();

        return getCommentResponseList;
    }

    private BooleanExpression isUserRoleFactory(User user) {
        return Expressions.asBoolean(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_FACTORY"))).isTrue();
    }
}
