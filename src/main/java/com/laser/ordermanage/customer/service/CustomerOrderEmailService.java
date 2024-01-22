package com.laser.ordermanage.customer.service;

import com.laser.ordermanage.common.email.EmailService;
import com.laser.ordermanage.common.email.dto.EmailRequest;
import com.laser.ordermanage.order.domain.Order;
import com.laser.ordermanage.order.dto.response.GetEmailRecipientResponse;
import com.laser.ordermanage.order.service.OrderEmailService;
import com.laser.ordermanage.order.service.OrderService;
import com.laser.ordermanage.user.domain.type.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CustomerOrderEmailService {

    private final OrderService orderService;
    private final OrderEmailService orderEmailService;
    private final EmailService emailService;

    @Transactional(readOnly = true)
    public void sendEmailForUpdateOrderDeliveryAddress(Long orderId) {
        GetEmailRecipientResponse emailRecipient = orderEmailService.getEmailRecipient(orderId, Role.ROLE_FACTORY);

        if (!emailRecipient.emailNotification()) {
            return;
        }

        Order order = orderService.getOrderById(orderId);
        StringBuilder sbSubject = new StringBuilder();
        sbSubject.append("[거래 배송지 수정] ")
                .append(order.getCustomer().getName())
                .append(" - ")
                .append(order.getName())
                .append(" 거래의 배송지가 수정되었습니다.");
        String subject = sbSubject.toString();

        String title = "거래 배송지 수정";

        StringBuilder sbContent = new StringBuilder();
        sbContent.append(order.getCustomer().getName())
                .append(order.getCustomer().hasCompanyName() ? " - " + order.getCustomer().getCompanyName() : " ")
                .append("고객님의, ")
                .append(order.getName())
                .append(" 거래 배송지가 수정되었습니다.");
        String content = sbContent.toString();

        EmailRequest emailRequest = EmailRequest.builder()
                .recipient(emailRecipient.email())
                .subject(subject)
                .title(title)
                .content(content)
                .buttonText("거래 정보 확인하기")
                .buttonUrl("https://www.kumoh.org/order/" + order.getId())
                .build();
        emailService.sendEmail(emailRequest);
    }

    @Transactional(readOnly = true)
    public void sendEmailForCreateOrderDrawing(Long orderId) {
        GetEmailRecipientResponse emailRecipient = orderEmailService.getEmailRecipient(orderId, Role.ROLE_FACTORY);

        if (!emailRecipient.emailNotification()) {
            return;
        }

        Order order = orderService.getOrderById(orderId);
        StringBuilder sbSubject = new StringBuilder();
        sbSubject.append("[거래 도면 추가] ")
                .append(order.getCustomer().getName())
                .append(" - ")
                .append(order.getName())
                .append(" 거래의 도면이 추가되었습니다.");
        String subject = sbSubject.toString();

        String title = "거래 도면 추가";

        StringBuilder sbContent = new StringBuilder();
        sbContent.append(order.getCustomer().getName())
                .append(order.getCustomer().hasCompanyName() ? " - " + order.getCustomer().getCompanyName() : " ")
                .append("고객님의, ")
                .append(order.getName())
                .append(" 거래 도면이 추가되었습니다.");
        String content = sbContent.toString();

        EmailRequest emailRequest = EmailRequest.builder()
                .recipient(emailRecipient.email())
                .subject(subject)
                .title(title)
                .content(content)
                .buttonText("거래 정보 확인하기")
                .buttonUrl("https://www.kumoh.org/order/" + order.getId())
                .build();
        emailService.sendEmail(emailRequest);
    }

    @Transactional(readOnly = true)
    public void sendEmailForUpdateOrderDrawing(Long orderId) {
        GetEmailRecipientResponse emailRecipient = orderEmailService.getEmailRecipient(orderId, Role.ROLE_FACTORY);

        if (!emailRecipient.emailNotification()) {
            return;
        }

        Order order = orderService.getOrderById(orderId);
        StringBuilder sbSubject = new StringBuilder();
        sbSubject.append("[거래 도면 항목 수정] ")
                .append(order.getCustomer().getName())
                .append(" - ")
                .append(order.getName())
                .append(" 거래의 도면 항목이 수정되었습니다.");
        String subject = sbSubject.toString();

        String title = "거래 도면 항목 수정";

        StringBuilder sbContent = new StringBuilder();
        sbContent.append(order.getCustomer().getName())
                .append(order.getCustomer().hasCompanyName() ? " - " + order.getCustomer().getCompanyName() : " ")
                .append("고객님의, ")
                .append(" 거래 도면 항목이 수정되었습니다.");
        String content = sbContent.toString();

        EmailRequest emailRequest = EmailRequest.builder()
                .recipient(emailRecipient.email())
                .subject(subject)
                .title(title)
                .content(content)
                .buttonText("거래 정보 확인하기")
                .buttonUrl("https://www.kumoh.org/order/" + order.getId())
                .build();
        emailService.sendEmail(emailRequest);
    }

    @Transactional(readOnly = true)
    public void sendEmailForDeleteOrderDrawing(Long orderId) {
        GetEmailRecipientResponse emailRecipient = orderEmailService.getEmailRecipient(orderId, Role.ROLE_FACTORY);

        if (!emailRecipient.emailNotification()) {
            return;
        }

        Order order = orderService.getOrderById(orderId);
        StringBuilder sbSubject = new StringBuilder();
        sbSubject.append("[거래 도면 삭제] ")
                .append(order.getCustomer().getName())
                .append(" - ")
                .append(order.getName())
                .append(" 거래의 도면이 삭제되었습니다.");
        String subject = sbSubject.toString();

        String title = "거래 도면 삭제";

        StringBuilder sbContent = new StringBuilder();
        sbContent.append(order.getCustomer().getName())
                .append(order.getCustomer().hasCompanyName() ? " - " + order.getCustomer().getCompanyName() : " ")
                .append("고객님의, ")
                .append(order.getName())
                .append(" 거래 도면이 삭제되었습니다.");
        String content = sbContent.toString();

        EmailRequest emailRequest = EmailRequest.builder()
                .recipient(emailRecipient.email())
                .subject(subject)
                .title(title)
                .content(content)
                .buttonText("거래 정보 확인하기")
                .buttonUrl("https://www.kumoh.org/order/" + order.getId())
                .build();
        emailService.sendEmail(emailRequest);
    }

    @Transactional(readOnly = true)
    public void sendEmailForApproveQuotation(Long orderId) {
        GetEmailRecipientResponse emailRecipient = orderEmailService.getEmailRecipient(orderId, Role.ROLE_FACTORY);

        if (!emailRecipient.emailNotification()) {
            return;
        }

        Order order = orderService.getOrderById(orderId);
        StringBuilder sbSubject = new StringBuilder();
        sbSubject.append("[거래 견적서 승인] ")
                .append(order.getCustomer().getName())
                .append(" - ")
                .append(order.getName())
                .append(" 거래의 견적서가 승인되었습니다.");
        String subject = sbSubject.toString();

        String title = "거래 견적서 승인";

        StringBuilder sbContent = new StringBuilder();
        sbContent.append(order.getCustomer().getName())
                .append(order.getCustomer().hasCompanyName() ? " - " + order.getCustomer().getCompanyName() : " ")
                .append("고객님의, ")
                .append(order.getName())
                .append(" 거래 견적서가 승인되었습니다.");
        String content = sbContent.toString();

        EmailRequest emailRequest = EmailRequest.builder()
                .recipient(emailRecipient.email())
                .subject(subject)
                .title(title)
                .content(content)
                .buttonText("거래 정보 확인하기")
                .buttonUrl("https://www.kumoh.org/order/" + order.getId())
                .build();
        emailService.sendEmail(emailRequest);
    }

    @Transactional(readOnly = true)
    public void sendEmailForCreateOrderPurchaseOrder(Long orderId) {
        GetEmailRecipientResponse emailRecipient = orderEmailService.getEmailRecipient(orderId, Role.ROLE_FACTORY);

        if (!emailRecipient.emailNotification()) {
            return;
        }

        Order order = orderService.getOrderById(orderId);
        StringBuilder sbSubject = new StringBuilder();
        sbSubject.append("[거래 발주서 작성] ")
                .append(order.getCustomer().getName())
                .append(" - ")
                .append(order.getName())
                .append(" 거래의 발주서가 작성되었습니다.");
        String subject = sbSubject.toString();

        String title = "거래 발주서 작성";

        StringBuilder sbContent = new StringBuilder();
        sbContent.append(order.getCustomer().getName())
                .append(order.getCustomer().hasCompanyName() ? " - " + order.getCustomer().getCompanyName() : " ")
                .append("고객님의, ")
                .append(order.getName())
                .append(" 거래 발주서가 작성되었습니다.");
        String content = sbContent.toString();

        EmailRequest emailRequest = EmailRequest.builder()
                .recipient(emailRecipient.email())
                .subject(subject)
                .title(title)
                .content(content)
                .buttonText("거래 정보 확인하기")
                .buttonUrl("https://www.kumoh.org/order/" + order.getId())
                .build();
        emailService.sendEmail(emailRequest);
    }

    @Transactional(readOnly = true)
    public void sendEmailForUpdateOrderPurchaseOrder(Long orderId) {
        GetEmailRecipientResponse emailRecipient = orderEmailService.getEmailRecipient(orderId, Role.ROLE_FACTORY);

        if (!emailRecipient.emailNotification()) {
            return;
        }

        Order order = orderService.getOrderById(orderId);
        StringBuilder sbSubject = new StringBuilder();
        sbSubject.append("[거래 발주서 수정] ")
                .append(order.getCustomer().getName())
                .append(" - ")
                .append(order.getName())
                .append(" 거래의 발주서가 수정되었습니다.");
        String subject = sbSubject.toString();

        String title = "거래 발주서 수정";

        StringBuilder sbContent = new StringBuilder();
        sbContent.append(order.getCustomer().getName())
                .append(order.getCustomer().hasCompanyName() ? " - " + order.getCustomer().getCompanyName() : " ")
                .append("고객님의, ")
                .append(order.getName())
                .append(" 거래 발주서가 수정되었습니다.");
        String content = sbContent.toString();

        EmailRequest emailRequest = EmailRequest.builder()
                .recipient(emailRecipient.email())
                .subject(subject)
                .title(title)
                .content(content)
                .buttonText("거래 정보 확인하기")
                .buttonUrl("https://www.kumoh.org/order/" + order.getId())
                .build();
        emailService.sendEmail(emailRequest);
    }
}
