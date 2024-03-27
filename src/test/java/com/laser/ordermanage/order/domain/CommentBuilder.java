package com.laser.ordermanage.order.domain;

import com.laser.ordermanage.user.domain.UserEntity;
import com.laser.ordermanage.user.domain.UserEntityBuilder;

public class CommentBuilder {

    public static Comment build() {
        UserEntity user = UserEntityBuilder.build();
        Order order = OrderBuilder.build();

        return Comment.builder()
                .user(user)
                .order(order)
                .content("댓글 내용")
                .build();
    }
}
