package com.laser.ordermanage.order.service;

import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.exception.ErrorCode;
import com.laser.ordermanage.common.paging.ListResponse;
import com.laser.ordermanage.order.domain.Comment;
import com.laser.ordermanage.order.domain.Order;
import com.laser.ordermanage.order.dto.request.CreateCommentRequest;
import com.laser.ordermanage.order.dto.response.GetCommentResponse;
import com.laser.ordermanage.order.dto.response.GetOrderDetailResponse;
import com.laser.ordermanage.order.repository.CommentRepository;
import com.laser.ordermanage.order.repository.OrderRepository;
import com.laser.ordermanage.user.domain.UserEntity;
import com.laser.ordermanage.user.service.UserAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final CommentRepository commentRepository;
    private final OrderRepository orderRepository;

    private final UserAuthService userAuthService;

    @Transactional(readOnly = true)
    public Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new CustomCommonException(ErrorCode.NOT_FOUND_ENTITY, "order"));
    }

    @Transactional(readOnly = true)
    public GetOrderDetailResponse getOrderDetail(User user, Long orderId) {
        return orderRepository.findDetailByUserAndOrder(user, orderId);
    }

    @Transactional(readOnly = true)
    public ListResponse<GetCommentResponse> getOrderComment(User user, Long orderId) {
        return new ListResponse<>(commentRepository.findCommentByUserAndOrder(user, orderId));
    }

    @Transactional
    public void createOrderComment(String userName, Long orderId, CreateCommentRequest request) {

        UserEntity user = userAuthService.findUserByEmail(userName);

        Order order = this.findOrderById(orderId);

        Comment comment = Comment.builder()
                .user(user)
                .order(order)
                .content(request.getContent())
                .build();

        commentRepository.save(comment);
    }
}
