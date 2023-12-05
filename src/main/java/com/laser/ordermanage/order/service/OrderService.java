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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new CustomCommonException(ErrorCode.NOT_FOUND_ENTITY, "order"));
    }

    @Transactional(readOnly = true)
    public String getUserEmailByOrder(Long orderId) {
        return orderRepository.findUserEmailById(orderId).orElseThrow(() -> new CustomCommonException(ErrorCode.NOT_FOUND_ENTITY, "order"));
    }

    @Transactional(readOnly = true)
    public GetOrderDetailResponse getOrderDetail(Long orderId) {
        return orderRepository.findDetailByOrder(orderId);
    }

    @Transactional(readOnly = true)
    public ListResponse<GetCommentResponse> getOrderComment(Long orderId) {
        return new ListResponse<>(commentRepository.findCommentByOrder(orderId));
    }

    @Transactional
    public void createOrderComment(String userName, Long orderId, CreateCommentRequest request) {

        UserEntity user = userAuthService.getUserByEmail(userName);
        Order order = this.getOrderById(orderId);

        Comment comment = Comment.builder()
                .user(user)
                .order(order)
                .content(request.getContent())
                .build();

        commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public void checkAuthorityCustomerOfOrderOrFactory(User user, Long orderId) {
        if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_FACTORY"))) {
            return;
        }

        if (this.getUserEmailByOrder(orderId).equals(user.getUsername())) {
            return;
        }

        throw new CustomCommonException(ErrorCode.DENIED_ACCESS_TO_ENTITY, "order");
    }
}
