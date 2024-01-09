package com.laser.ordermanage.order.service;

import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.mail.MailService;
import com.laser.ordermanage.common.paging.ListResponse;
import com.laser.ordermanage.order.domain.Comment;
import com.laser.ordermanage.order.domain.Order;
import com.laser.ordermanage.order.domain.PurchaseOrder;
import com.laser.ordermanage.order.dto.request.CreateCommentRequest;
import com.laser.ordermanage.order.dto.response.GetCommentResponse;
import com.laser.ordermanage.order.dto.response.GetOrderDetailResponse;
import com.laser.ordermanage.order.dto.response.GetPurchaseOrderFileResponse;
import com.laser.ordermanage.order.exception.OrderErrorCode;
import com.laser.ordermanage.order.repository.CommentRepository;
import com.laser.ordermanage.order.repository.OrderRepository;
import com.laser.ordermanage.user.domain.UserEntity;
import com.laser.ordermanage.user.domain.type.Role;
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
    private final MailService mailService;

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
        return orderRepository.findDetailByOrder(orderId);
    }

    @Transactional(readOnly = true)
    public ListResponse<GetCommentResponse> getOrderComment(Long orderId) {
        return new ListResponse<>(commentRepository.findCommentByOrder(orderId));
    }

    @Transactional
    public Comment createOrderComment(String userName, Long orderId, CreateCommentRequest request) {

        UserEntity user = userAuthService.getUserByEmail(userName);
        Order order = this.getOrderById(orderId);

        Comment comment = Comment.builder()
                .user(user)
                .order(order)
                .content(request.content())
                .build();

        commentRepository.save(comment);

        return comment;
    }

    @Transactional(readOnly = true)
    public void sendEmailForCreateOrderComment(Comment comment) {
        Order order = comment.getOrder();
        UserEntity user = comment.getUser();

        StringBuilder sbContent = new StringBuilder();
        sbContent.append(order.getName())
                .append(" 거래에 새로운 댓글이 작성되었습니다.");
        String content = sbContent.toString();

        if (user.getRole().equals(Role.ROLE_FACTORY)) {
            String toEmail = order.getCustomer().getUser().getEmail();

            StringBuilder sbTitle = new StringBuilder();
            sbTitle.append("[댓글] 고객님, ")
                    .append(order.getName())
                    .append(" 거래에 댓글이 작성되었습니다.");
            String title = sbTitle.toString();

            mailService.sendEmail(toEmail, title, content);
        } else if (user.getRole().equals(Role.ROLE_CUSTOMER)) {
            StringBuilder sbTitle = new StringBuilder();
            sbTitle.append("[댓글] ")
                    .append(order.getCustomer().getName())
                    .append(" - ")
                    .append(order.getName())
                    .append(" 거래에 댓글이 작성되었습니다.");
            String title = sbTitle.toString();

            mailService.sendEmailToFactory(title, content);
        }
    }

    @Transactional(readOnly = true)
    public void checkAuthorityCustomerOfOrderOrFactory(User user, Long orderId) {
        if (user.getAuthorities().contains(new SimpleGrantedAuthority(Role.ROLE_FACTORY.name()))) {
            return;
        }

        if (this.getUserEmailByOrder(orderId).equals(user.getUsername())) {
            return;
        }

        throw new CustomCommonException(OrderErrorCode.DENIED_ACCESS_TO_ORDER);
    }

    @Transactional(readOnly = true)
    public Object getOrderPurchaseOrderFile(Long orderId) {
        Order order = getOrderById(orderId);

        if (!order.hasPurchaseOrder()) {
            throw new CustomCommonException(OrderErrorCode.NOT_FOUND_PURCHASE_ORDER);
        }

        PurchaseOrder purchaseOrder = order.getPurchaseOrder();

        return GetPurchaseOrderFileResponse.builder()
                .id(purchaseOrder.getId())
                .fileName(purchaseOrder.getFileName())
                .fileUrl(purchaseOrder.getFileUrl())
                .build();
    }
}
