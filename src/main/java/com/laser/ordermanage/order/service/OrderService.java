package com.laser.ordermanage.order.service;

import com.laser.ordermanage.common.paging.ListResponse;
import com.laser.ordermanage.order.dto.response.GetCommentResponse;
import com.laser.ordermanage.order.dto.response.GetOrderDetailResponse;
import com.laser.ordermanage.order.repository.CommentRepositoryCustom;
import com.laser.ordermanage.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final CommentRepositoryCustom commentRepository;
    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public GetOrderDetailResponse getOrderDetail(User user, Long orderId) {
        return orderRepository.findDetailByUserAndOrder(user, orderId);
    }

    @Transactional(readOnly = true)
    public ListResponse<GetCommentResponse> getOrderComment(User user, Long orderId) {
        return new ListResponse<>(commentRepository.findCommentByUserAndOrder(user, orderId));
    }
}
