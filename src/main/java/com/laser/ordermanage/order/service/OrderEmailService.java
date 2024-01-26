package com.laser.ordermanage.order.service;

import com.laser.ordermanage.common.email.EmailService;
import com.laser.ordermanage.common.email.dto.EmailWithButtonRequest;
import com.laser.ordermanage.customer.domain.Customer;
import com.laser.ordermanage.customer.repository.CustomerRepository;
import com.laser.ordermanage.order.domain.Comment;
import com.laser.ordermanage.order.domain.Order;
import com.laser.ordermanage.order.dto.response.DeleteOrderResponse;
import com.laser.ordermanage.order.dto.response.GetEmailRecipientResponse;
import com.laser.ordermanage.user.domain.UserEntity;
import com.laser.ordermanage.user.domain.type.Role;
import com.laser.ordermanage.user.service.UserAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrderEmailService {

    private final CustomerRepository customerRepository;

    private final UserAuthService userAuthService;
    private final OrderService orderService;
    private final EmailService emailService;

    @Transactional(readOnly = true)
    public void sendEmailForCreateOrderComment(Long commentId) {
        Comment comment = orderService.getCommentByCommentId(commentId);
        Order order = comment.getOrder();
        UserEntity user = comment.getUser();

        if (user.getRole().equals(Role.ROLE_FACTORY)) {
            GetEmailRecipientResponse emailRecipient = getEmailRecipient(order.getId(), Role.ROLE_CUSTOMER);

            if (!emailRecipient.emailNotification()) {
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

            EmailWithButtonRequest emailWithButtonRequest = EmailWithButtonRequest.builder()
                    .recipient(emailRecipient.email())
                    .subject(subject)
                    .title(title)
                    .content(content)
                    .buttonText("거래 정보 확인하기")
                    .buttonUrl("https://www.kumoh.org/order/" + order.getId())
                    .build();
            emailService.sendEmailWithButton(emailWithButtonRequest);

        } else if (user.getRole().equals(Role.ROLE_CUSTOMER)) {
            if (!order.hasCustomer()) {
                return;
            }

            GetEmailRecipientResponse emailRecipient = getEmailRecipient(order.getId(), Role.ROLE_FACTORY);

            if (!emailRecipient.emailNotification()) {
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

            EmailWithButtonRequest emailWithButtonRequest = EmailWithButtonRequest.builder()
                    .recipient(emailRecipient.email())
                    .subject(subject)
                    .title(title)
                    .content(content)
                    .buttonText("거래 정보 확인하기")
                    .buttonUrl("https://www.kumoh.org/order/" + order.getId())
                    .build();
            emailService.sendEmailWithButton(emailWithButtonRequest);
        }
    }

    @Transactional(readOnly = true)
    public void sendEmailForDeleteOrder(User user, DeleteOrderResponse deletedOrder) {
        UserEntity userEntity = userAuthService.getUserByEmail(user.getUsername());
        Customer customer = customerRepository.findFirstByUserEmail(deletedOrder.customerUserEmail());

        if (userEntity.getRole().equals(Role.ROLE_FACTORY)) {
            UserEntity emailRecipient = customer.getUser();

            if (!emailRecipient.getEmailNotification()) {
                return;
            }

            StringBuilder sbSubject = new StringBuilder();
            sbSubject.append("[거래 삭제] 고객님, ")
                    .append(deletedOrder.name())
                    .append(" 거래가 삭제되었습니다.");
            String subject = sbSubject.toString();

            String title = "거래 삭제";

            StringBuilder sbContent = new StringBuilder();
            sbContent.append(customer.getName())
                    .append(customer.hasCompanyName() ? " - " + customer.getCompanyName() : " ")
                    .append("고객님의, ")
                    .append(deletedOrder.name())
                    .append(" 거래가 삭제되었습니다.");
            String content = sbContent.toString();

            EmailWithButtonRequest emailWithButtonRequest = EmailWithButtonRequest.builder()
                    .recipient(emailRecipient.getEmail())
                    .subject(subject)
                    .title(title)
                    .content(content)
                    .buttonText("거래 내역 확인하기")
                    .buttonUrl("https://www.kumoh.org/customer/order")
                    .build();
        } else if (userEntity.getRole().equals(Role.ROLE_CUSTOMER)) {
            UserEntity emailRecipient = userAuthService.getUserByEmail("admin@kumoh.org");

            if (!emailRecipient.getEmailNotification()) {
                return;
            }

            StringBuilder sbSubject = new StringBuilder();
            sbSubject.append("[거래 삭제] ")
                    .append(customer.getName())
                    .append(" - ")
                    .append(deletedOrder.name())
                    .append(" 거래가 삭제되었습니다.");
            String subject = sbSubject.toString();

            String title = "거래 신규 댓글";

            StringBuilder sbContent = new StringBuilder();
            sbContent.append(customer.getName())
                    .append(customer.hasCompanyName() ? " - " + customer.getCompanyName() : " ")
                    .append("고객님의, ")
                    .append(deletedOrder.name())
                    .append(" 거래가 삭제되었습니다.");
            String content = sbContent.toString();

            EmailWithButtonRequest emailWithButtonRequest = EmailWithButtonRequest.builder()
                    .recipient(emailRecipient.getEmail())
                    .subject(subject)
                    .title(title)
                    .content(content)
                    .buttonText("거래 내역 확인하기")
                    .buttonUrl("https://www.kumoh.org/factory/order")
                    .build();
            emailService.sendEmailWithButton(emailWithButtonRequest);
        }
    }

    @Transactional(readOnly = true)
    public GetEmailRecipientResponse getEmailRecipient(Long orderId, Role userRole) {

        if (userRole.equals(Role.ROLE_FACTORY)) {
            UserEntity factoryUser = userAuthService.getUserByEmail("admin@kumoh.org");

            return GetEmailRecipientResponse.builder()
                    .emailNotification(factoryUser.getEmailNotification())
                    .email(factoryUser.getEmail())
                    .build();
        } else {
            UserEntity customerUser = orderService.getOrderById(orderId).getCustomer().getUser();

            return GetEmailRecipientResponse.builder()
                    .emailNotification(customerUser.getEmailNotification())
                    .email(customerUser.getEmail())
                    .build();
        }

    }
}
