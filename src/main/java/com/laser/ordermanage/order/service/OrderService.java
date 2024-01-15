package com.laser.ordermanage.order.service;

import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.mail.MailService;
import com.laser.ordermanage.common.mail.dto.MailRequest;
import com.laser.ordermanage.common.paging.ListResponse;
import com.laser.ordermanage.order.domain.Comment;
import com.laser.ordermanage.order.domain.Order;
import com.laser.ordermanage.order.dto.request.CreateCommentRequest;
import com.laser.ordermanage.order.dto.response.GetCommentResponse;
import com.laser.ordermanage.order.dto.response.GetOrderDetailResponse;
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

        if (user.getRole().equals(Role.ROLE_FACTORY)) {
            String toEmail = order.getCustomer().getUser().getEmail();

            StringBuilder sbSubject = new StringBuilder();
            sbSubject.append("[댓글] 고객님, ")
                    .append(order.getName())
                    .append(" 거래에 댓글이 작성되었습니다.");
            String subject = sbSubject.toString();

            String title = "거래 신규 댓글";

            StringBuilder sbContent = new StringBuilder();
            sbContent.append("안녕하세요 ")
                    .append(order.getCustomer().getName())
                    .append(order.getCustomer().hasCompanyName() ? " - " + order.getCustomer().getCompanyName() : " ")
                    .append("고객님,\n")
                    .append(order.getName())
                    .append(" 거래에 새로운 댓글이 작성되었습니다.");
            String content = sbContent.toString();

            MailRequest mailRequest = MailRequest.builder()
                    .toEmail(toEmail)
                    .subject(subject)
                    .title(title)
                    .content(content)
                    .buttonText("거래 정보 확인하기")
                    .buttonUrl("https://www.kumoh.org/order/" + order.getId())
                    .build();
            mailService.sendEmail(mailRequest);

        } else if (user.getRole().equals(Role.ROLE_CUSTOMER)) {
            StringBuilder sbSubject = new StringBuilder();
            sbSubject.append("[댓글] ")
                    .append(order.getCustomer().getName())
                    .append(" - ")
                    .append(order.getName())
                    .append(" 거래에 댓글이 작성되었습니다.");
            String subject = sbSubject.toString();

            String title = "거래 신규 댓글";

            StringBuilder sbContent = new StringBuilder();
            sbContent.append(order.getCustomer().getName())
                    .append(order.getCustomer().hasCompanyName() ? " - " + order.getCustomer().getCompanyName() : " ")
                    .append("고객님의, ")
                    .append(order.getName())
                    .append(" 거래에 새로운 댓글이 작성되었습니다.");
            String content = sbContent.toString();

            MailRequest mailRequest = MailRequest.builderToFactory()
                    .subject(subject)
                    .title(title)
                    .content(content)
                    .buttonText("거래 정보 확인하기")
                    .buttonUrl("https://www.kumoh.org/order/" + order.getId())
                    .buildToFactory();
            mailService.sendEmail(mailRequest);
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
}
