package com.laser.ordermanage.order.service;

import com.laser.ordermanage.common.mail.MailService;
import com.laser.ordermanage.common.mail.dto.MailRequest;
import com.laser.ordermanage.order.domain.Comment;
import com.laser.ordermanage.order.domain.Order;
import com.laser.ordermanage.order.dto.response.GetEmailReceiverResponse;
import com.laser.ordermanage.user.domain.UserEntity;
import com.laser.ordermanage.user.domain.type.Role;
import com.laser.ordermanage.user.service.UserAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrderMailService {

    private final UserAuthService userAuthService;
    private final OrderService orderService;
    private final MailService mailService;

    @Transactional(readOnly = true)
    public void sendEmailForCreateOrderComment(Long commentId) {
        Comment comment = orderService.getCommentByCommentId(commentId);
        Order order = comment.getOrder();
        UserEntity user = comment.getUser();

        if (user.getRole().equals(Role.ROLE_FACTORY)) {
            GetEmailReceiverResponse emailReceiver = getMailReceiver(order.getId(), Role.ROLE_CUSTOMER);

            if (!emailReceiver.emailNotification()) {
                return;
            }

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
                    .toEmail(emailReceiver.email())
                    .subject(subject)
                    .title(title)
                    .content(content)
                    .buttonText("거래 정보 확인하기")
                    .buttonUrl("https://www.kumoh.org/order/" + order.getId())
                    .build();
            mailService.sendEmail(mailRequest);

        } else if (user.getRole().equals(Role.ROLE_CUSTOMER)) {
            GetEmailReceiverResponse emailReceiver = getMailReceiver(order.getId(), Role.ROLE_FACTORY);

            if (!emailReceiver.emailNotification()) {
                return;
            }

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

            MailRequest mailRequest = MailRequest.builder()
                    .toEmail(emailReceiver.email())
                    .subject(subject)
                    .title(title)
                    .content(content)
                    .buttonText("거래 정보 확인하기")
                    .buttonUrl("https://www.kumoh.org/order/" + order.getId())
                    .build();
            mailService.sendEmail(mailRequest);
        }
    }

    @Transactional(readOnly = true)
    public GetEmailReceiverResponse getMailReceiver(Long orderId, Role userRole) {

        if (userRole.equals(Role.ROLE_FACTORY)) {
            UserEntity factoryUser = userAuthService.getUserByEmail("admin@kumoh.org");

            return GetEmailReceiverResponse.builder()
                    .emailNotification(factoryUser.getEmailNotification())
                    .email(factoryUser.getEmail())
                    .build();
        } else {
            UserEntity customerUser = orderService.getOrderById(orderId).getCustomer().getUser();

            return GetEmailReceiverResponse.builder()
                    .emailNotification(customerUser.getEmailNotification())
                    .email(customerUser.getEmail())
                    .build();
        }

    }
}
