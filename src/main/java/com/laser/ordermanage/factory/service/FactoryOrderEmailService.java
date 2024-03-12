package com.laser.ordermanage.factory.service;

import com.laser.ordermanage.common.email.EmailService;
import com.laser.ordermanage.common.email.dto.EmailWithButtonRequest;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.order.domain.Order;
import com.laser.ordermanage.order.dto.response.GetEmailRecipientResponse;
import com.laser.ordermanage.order.exception.OrderErrorCode;
import com.laser.ordermanage.order.service.OrderEmailService;
import com.laser.ordermanage.order.service.OrderService;
import com.laser.ordermanage.user.domain.type.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@Service
public class FactoryOrderEmailService {

    private final OrderService orderService;
    private final OrderEmailService orderEmailService;
    private final EmailService emailService;

    @Transactional(readOnly = true)
    public void sendEmailForUpdateOrderIsUrgent(Long orderId) {
        GetEmailRecipientResponse emailRecipient = orderEmailService.getEmailRecipient(orderId, Role.ROLE_CUSTOMER);

        if (!emailRecipient.emailNotification()) {
            return;
        }

        Order order = orderService.getOrderById(orderId);

        StringBuilder sbSubject = new StringBuilder();
        StringBuilder sbTitle = new StringBuilder();
        StringBuilder sbContent = new StringBuilder();

        if (order.getIsUrgent()) {
            sbSubject.append("[거래 긴급 설정] ")
                    .append(order.getName())
                    .append(" 거래의 긴급이 설정 되었습니다.");

            sbTitle.append("거래 긴급 설정");

            sbContent.append("안녕하세요 ")
                    .append(order.getCustomer().getUser().getName())
                    .append(order.getCustomer().hasCompanyName() ? " - " + order.getCustomer().getCompanyName() : " ")
                    .append("고객님,<br/>")
                    .append(order.getName())
                    .append(" 거래의 긴급이 설정 되었습니다.");
        } else {
            sbSubject.append("[거래 긴급 설정 해제] ")
                    .append(order.getName())
                    .append(" 거래의 긴급 설정이 해제 되었습니다.");

            sbTitle.append("거래 긴급 설정 해제");

            sbContent.append("안녕하세요 ")
                    .append(order.getCustomer().getUser().getName())
                    .append(order.getCustomer().hasCompanyName() ? " - " + order.getCustomer().getCompanyName() : " ")
                    .append("고객님,<br/>")
                    .append(order.getName())
                    .append(" 거래의 긴급 설정이 해제 되었습니다.");
        }

        String subject = sbSubject.toString();
        String title = sbTitle.toString();
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

    @Transactional(readOnly = true)
    public void sendEmailForCreateOrderQuotation(Long orderId) {
        GetEmailRecipientResponse emailRecipient = orderEmailService.getEmailRecipient(orderId, Role.ROLE_CUSTOMER);

        if (!emailRecipient.emailNotification()) {
            return;
        }

        Order order = orderService.getOrderById(orderId);

        StringBuilder sbSubject = new StringBuilder();
        sbSubject.append("[거래 견적서 작성] 고객님, ")
                .append(order.getName())
                .append(" 거래의 견적서가 작성되었습니다.");
        String subject = sbSubject.toString();

        String title = "거래 견적서 작성";

        StringBuilder sbContent = new StringBuilder();
        sbContent.append("안녕하세요 ")
                .append(order.getCustomer().getUser().getName())
                .append(order.getCustomer().hasCompanyName() ? " - " + order.getCustomer().getCompanyName() : " ")
                .append("고객님,<br/>")
                .append(order.getName())
                .append(" 거래의 견적서가 작성되었습니다.");
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

    @Transactional(readOnly = true)
    public void sendEmailForUpdateOrderQuotation(Long orderId) {
        GetEmailRecipientResponse emailRecipient = orderEmailService.getEmailRecipient(orderId, Role.ROLE_CUSTOMER);

        if (!emailRecipient.emailNotification()) {
            return;
        }

        Order order = orderService.getOrderById(orderId);

        StringBuilder sbSubject = new StringBuilder();
        sbSubject.append("[거래 견적서 수정] 고객님, ")
                .append(order.getName())
                .append(" 거래의 견적서가 수정되었습니다.");
        String subject = sbSubject.toString();

        String title = "거래 견적서 수정";

        StringBuilder sbContent = new StringBuilder();
        sbContent.append("안녕하세요 ")
                .append(order.getCustomer().getUser().getName())
                .append(order.getCustomer().hasCompanyName() ? " - " + order.getCustomer().getCompanyName() : " ")
                .append("고객님,<br/>")
                .append(order.getName())
                .append(" 거래의 견적서가 수정되었습니다.");
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

    @Transactional(readOnly = true)
    public void sendEmailForApprovePurchaseOrder(Long orderId) {
        GetEmailRecipientResponse emailRecipient = orderEmailService.getEmailRecipient(orderId, Role.ROLE_CUSTOMER);

        if (!emailRecipient.emailNotification()) {
            return;
        }

        Order order = orderService.getOrderById(orderId);

        StringBuilder sbSubject = new StringBuilder();
        sbSubject.append("[거래 발주서 승인] 고객님, ")
                .append(order.getName())
                .append(" 거래의 발주서가 승인되었습니다.");
        String subject = sbSubject.toString();

        String title = "거래 발주서 승인";

        StringBuilder sbContent = new StringBuilder();
        sbContent.append("안녕하세요 ")
                .append(order.getCustomer().getUser().getName())
                .append(order.getCustomer().hasCompanyName() ? " - " + order.getCustomer().getCompanyName() : " ")
                .append("고객님,<br/>")
                .append(order.getName())
                .append(" 거래의 발주서가 승인되었습니다.");
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

    @Transactional(readOnly = true)
    public void sendEmailForChangeStageToProductionCompleted(Long orderId) {
        GetEmailRecipientResponse emailRecipient = orderEmailService.getEmailRecipient(orderId, Role.ROLE_CUSTOMER);

        if (!emailRecipient.emailNotification()) {
            return;
        }

        Order order = orderService.getOrderById(orderId);

        StringBuilder sbSubject = new StringBuilder();
        sbSubject.append("[거래 제작 완료] 고객님, ")
                .append(order.getName())
                .append(" 거래의 제작이 완료되었습니다.");
        String subject = sbSubject.toString();

        String title = "거래 제작 완료";

        StringBuilder sbContent = new StringBuilder();
        sbContent.append("안녕하세요 ")
                .append(order.getCustomer().getUser().getName())
                .append(order.getCustomer().hasCompanyName() ? " - " + order.getCustomer().getCompanyName() : " ")
                .append("고객님,<br/>")
                .append(order.getName())
                .append(" 거래의 제작이 완료되었습니다.");
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

    @Transactional(readOnly = true)
    public void sendEmailForAcquirer(Long orderId, String baseUrl) {
        GetEmailRecipientResponse emailRecipient = orderEmailService.getEmailRecipient(orderId, Role.ROLE_FACTORY);

        if (!emailRecipient.emailNotification()) {
            return;
        }

        Order order = orderService.getOrderById(orderId);

        if (!order.enableChangeStageToCompleted()) {
            throw new CustomCommonException(OrderErrorCode.INVALID_ORDER_STAGE, order.getStage().getValue());
        }

        String acquireSignatureUrl = UriComponentsBuilder
                .fromHttpUrl(baseUrl)
                .path("/{order-id}")
                .buildAndExpand(order.getId())
                .toUriString();

        StringBuilder sbSubject = new StringBuilder();
        sbSubject.append("[거래 품목 확인 및 인수 서명 요청] ")
                .append(order.getCustomer().getUser().getName())
                .append(" - ")
                .append(order.getName())
                .append(" 거래의 품목을 확인 후 인수자의 서명을 받아주세요.");
        String subject = sbSubject.toString();

        String title = "거래 품목 확인 및 인수 서명 요청";

        StringBuilder sbContent = new StringBuilder();
        sbContent.append(order.getCustomer().getUser().getName())
                .append(order.getCustomer().hasCompanyName() ? " - " + order.getCustomer().getCompanyName() : " ")
                .append("고객님의, ")
                .append(order.getName())
                .append(" 거래에 대한 품목 확인 및 인수자 서명을 받아주세요.");
        String content = sbContent.toString();

        EmailWithButtonRequest emailWithButtonRequest = EmailWithButtonRequest.builder()
                .recipient(emailRecipient.email())
                .subject(subject)
                .title(title)
                .content(content)
                .buttonText("서명 링크 이동")
                .buttonUrl(acquireSignatureUrl)
                .build();
        emailService.sendEmailWithButton(emailWithButtonRequest);
    }

    @Transactional(readOnly = true)
    public void sendEmailForChangeStageToCompleted(Long orderId) {
        GetEmailRecipientResponse emailRecipient = orderEmailService.getEmailRecipient(orderId, Role.ROLE_CUSTOMER);

        if (!emailRecipient.emailNotification()) {
            return;
        }

        Order order = orderService.getOrderById(orderId);

        StringBuilder sbSubject = new StringBuilder();
        sbSubject.append("[거래 완료] 고객님, ")
                .append(order.getName())
                .append(" 거래가 완료되었습니다.");
        String subject = sbSubject.toString();

        String title = "거래 완료";

        StringBuilder sbContent = new StringBuilder();
        sbContent.append("안녕하세요 ")
                .append(order.getCustomer().getUser().getName())
                .append(order.getCustomer().hasCompanyName() ? " - " + order.getCustomer().getCompanyName() : " ")
                .append("고객님,<br/>")
                .append(order.getName())
                .append(" 거래가 완료되었습니다.");

        if (order.hasAcquirer()) {
            sbContent.append("<br/><br/>인수자 정보<br/>이름 : ")
                    .append(order.getAcquirer().getName())
                    .append("<br/>핸드폰 번호 : ")
                    .append(order.getAcquirer().getPhone());
        }

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
