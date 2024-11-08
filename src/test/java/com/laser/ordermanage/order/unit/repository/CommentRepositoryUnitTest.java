package com.laser.ordermanage.order.unit.repository;

import com.laser.ordermanage.common.RepositoryUnitTest;
import com.laser.ordermanage.order.dto.response.GetCommentResponse;
import com.laser.ordermanage.order.dto.response.GetCommentResponseBuilder;
import com.laser.ordermanage.order.repository.CommentRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@EnableJpaRepositories(basePackageClasses = CommentRepository.class)
public class CommentRepositoryUnitTest extends RepositoryUnitTest {

    @Autowired
    private CommentRepository commentRepository;

    @Test
    public void findCommentByOrder() {
        // given
        final Long orderId = 1L;
        final List<GetCommentResponse> expectedResponse = GetCommentResponseBuilder.buildListForOrder1();

        // when
        final List<GetCommentResponse> actualResponse = commentRepository.findCommentByOrder(orderId);

        // then
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    public void deleteAllByOrder() {
        // given
        final Long orderId = 1L;

        // when
        commentRepository.deleteAllByOrder(orderId);

        // then
        final List<GetCommentResponse> actualResponse = commentRepository.findCommentByOrder(orderId);
        Assertions.assertThat(actualResponse.size()).isEqualTo(0);
    }

    @Test
    public void deleteAllByOrderList() {
        // given
        final List<Long> orderIdList = List.of(1L, 15L);

        // when
        commentRepository.deleteAllByOrderList(orderIdList);

        // then
        orderIdList.forEach(
                orderId -> {
                    final List<GetCommentResponse> actualResponse = commentRepository.findCommentByOrder(orderId);
                    Assertions.assertThat(actualResponse.size()).isEqualTo(0);
                }
        );
    }

    @Test
    public void updateCommentUserAsNullByUserAndOrder() {
        // given
        final String email = "user1@gmail.com";
        final Long orderId = 1L;
        final List<Long> orderIdList = List.of(orderId);

        // when
        commentRepository.updateCommentUserAsNullByUserAndOrder(email, orderIdList);

        // then
        final List<GetCommentResponse> commentResponseList = commentRepository.findCommentByOrder(orderId);
        commentResponseList.forEach(
                commentResponse -> Assertions.assertThat(commentResponse.authorName()).isNotEqualTo(email)
        );
    }
}
