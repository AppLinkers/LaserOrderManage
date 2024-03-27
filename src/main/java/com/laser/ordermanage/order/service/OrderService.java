package com.laser.ordermanage.order.service;

import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.paging.ListResponse;
import com.laser.ordermanage.order.domain.Comment;
import com.laser.ordermanage.order.domain.Order;
import com.laser.ordermanage.order.dto.request.CreateCommentRequest;
import com.laser.ordermanage.order.dto.response.DeleteOrderResponse;
import com.laser.ordermanage.order.dto.response.GetCommentResponse;
import com.laser.ordermanage.order.dto.response.GetOrderDetailResponse;
import com.laser.ordermanage.order.exception.OrderErrorCode;
import com.laser.ordermanage.order.repository.CommentRepository;
import com.laser.ordermanage.order.repository.DrawingRepository;
import com.laser.ordermanage.order.repository.OrderRepository;
import com.laser.ordermanage.user.domain.UserEntity;
import com.laser.ordermanage.user.domain.type.Role;
import com.laser.ordermanage.user.service.UserAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final CommentRepository commentRepository;
    private final DrawingRepository drawingRepository;
    private final OrderRepository orderRepository;

    private final UserAuthService userAuthService;

    @Transactional(readOnly = true)
    public Order getOrderById(Long orderId) {
        return orderRepository.findFirstById(orderId).orElseThrow(() -> new CustomCommonException(OrderErrorCode.NOT_FOUND_ORDER));
    }

    @Transactional(readOnly = true)
    public String getUserEmailByOrder(Long orderId) {
        return orderRepository.findUserEmailById(orderId).orElseThrow(() -> new CustomCommonException(OrderErrorCode.NOT_FOUND_ORDER));
    }

    @Transactional(readOnly = true)
    public GetOrderDetailResponse getOrderDetail(Long orderId) {
        return orderRepository.findDetailByOrder(orderId).orElseThrow(() -> new CustomCommonException(OrderErrorCode.NOT_FOUND_ORDER));
    }

    @Transactional(readOnly = true)
    public Comment getCommentByCommentId(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new CustomCommonException(OrderErrorCode.NOT_FOUND_COMMENT));
    }

    @Transactional(readOnly = true)
    public ListResponse<GetCommentResponse> getCommentByOrder(Long orderId) {
        return new ListResponse<>(commentRepository.findCommentByOrder(orderId));
    }

    @Transactional
    public Long createOrderComment(String email, Long orderId, CreateCommentRequest request) {
        UserEntity user = userAuthService.getUserByEmail(email);
        Order order = this.getOrderById(orderId);

        Comment comment = Comment.builder()
                .user(user)
                .order(order)
                .content(request.content())
                .build();

        Comment createdComment = commentRepository.save(comment);

        return createdComment.getId();
    }

    @Transactional
    public DeleteOrderResponse deleteOrder(Long orderId) {
        Order order = this.getOrderById(orderId);

        if (!order.enableDelete()) {
            throw new CustomCommonException(OrderErrorCode.INVALID_ORDER_STAGE, order.getStage().getValue());
        }

        DeleteOrderResponse response = DeleteOrderResponse.builder()
                .name(order.getName())
                .customerUserEmail(order.getCustomer().getUser().getEmail())
                .build();

        // 거래 도면 데이터 삭제
        drawingRepository.deleteAllByOrder(orderId);

        // 거래 댓글 데이터 삭제
        commentRepository.deleteAllByOrder(orderId);

        // 거래 데이터 삭제 및 연관 데이터 삭제 (거래 제조 서비스, 거래 후처리 서비스, 거래 배송지, 견적서, 발주서)
        orderRepository.delete(order);

        return response;
    }

    @Transactional(readOnly = true)
    public void checkAuthorityCustomerOfOrderOrFactory(User user, Long orderId) {
        UserEntity userEntity = userAuthService.getUserByEmail(user.getUsername());
        if (userEntity.getRole().equals(Role.ROLE_FACTORY)) {
            return;
        }

        if (!getUserEmailByOrder(orderId).equals(user.getUsername())) {
            throw new CustomCommonException(OrderErrorCode.DENIED_ACCESS_TO_ORDER);
        }
    }
}
