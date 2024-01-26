package com.laser.ordermanage.order.repository;

import com.laser.ordermanage.order.dto.response.GetCommentResponse;

import java.util.List;

public interface CommentRepositoryCustom {

    List<GetCommentResponse> findCommentByOrder(Long orderId);

    void deleteAllByOrder(Long orderId);

    void deleteAllByOrderList(List<Long> orderIdList);

    void updateCommentUserAsNullByUserAndOrder(String email, List<Long> orderIdList);
}
