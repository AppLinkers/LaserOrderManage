package com.laser.ordermanage.order.repository;

import com.laser.ordermanage.order.dto.response.GetCommentResponse;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CommentRepositoryCustom {

    List<GetCommentResponse> findCommentByUserAndOrder(User user, Long orderId);

}
