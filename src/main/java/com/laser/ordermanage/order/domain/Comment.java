package com.laser.ordermanage.order.domain;

import com.laser.ordermanage.common.entity.CreatedAtEntity;
import com.laser.ordermanage.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Comment extends CreatedAtEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false, updatable = false)
    private Order order;

    @Column(name = "content", nullable = false, length = 200)
    private String content;

    @Builder
    public Comment(UserEntity user, Order order, String content) {
        this.user = user;
        this.order = order;
        this.content = content;
    }
}
