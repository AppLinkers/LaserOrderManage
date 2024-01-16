package com.laser.ordermanage.customer.service;

import com.laser.ordermanage.common.mail.MailService;
import com.laser.ordermanage.common.mail.dto.MailRequest;
import com.laser.ordermanage.order.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CustomerOrderMailService {

    private final MailService mailService;

    @Transactional(readOnly = true)
    public void sendEmailForUpdateOrderDeliveryAddress(Order order) {
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

        MailRequest mailRequest = MailRequest.builderToFactory()
                .subject(subject)
                .title(title)
                .content(content)
                .buttonText("거래 정보 확인하기")
                .buttonUrl("https://www.kumoh.org/order/" + order.getId())
                .buildToFactory();
        mailService.sendEmail(mailRequest);
    }

    @Transactional(readOnly = true)
    public void sendEmailForCreateOrderDrawing(Order order) {
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

        MailRequest mailRequest = MailRequest.builderToFactory()
                .subject(subject)
                .title(title)
                .content(content)
                .buttonText("거래 정보 확인하기")
                .buttonUrl("https://www.kumoh.org/order/" + order.getId())
                .buildToFactory();
        mailService.sendEmail(mailRequest);
    }

    @Transactional(readOnly = true)
    public void sendEmailForUpdateOrderDrawing(Order order) {
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

        MailRequest mailRequest = MailRequest.builderToFactory()
                .subject(subject)
                .title(title)
                .content(content)
                .buttonText("거래 정보 확인하기")
                .buttonUrl("https://www.kumoh.org/order/" + order.getId())
                .buildToFactory();
        mailService.sendEmail(mailRequest);
    }

    @Transactional(readOnly = true)
    public void sendEmailForDeleteOrderDrawing(Order order) {
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

        MailRequest mailRequest = MailRequest.builderToFactory()
                .subject(subject)
                .title(title)
                .content(content)
                .buttonText("거래 정보 확인하기")
                .buttonUrl("https://www.kumoh.org/order/" + order.getId())
                .buildToFactory();
        mailService.sendEmail(mailRequest);
    }

    @Transactional(readOnly = true)
    public void sendEmailForApproveQuotation(Order order) {
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

        MailRequest mailRequest = MailRequest.builderToFactory()
                .subject(subject)
                .title(title)
                .content(content)
                .buttonText("거래 정보 확인하기")
                .buttonUrl("https://www.kumoh.org/order/" + order.getId())
                .buildToFactory();
        mailService.sendEmail(mailRequest);
    }

    @Transactional(readOnly = true)
    public void sendEmailForCreateOrderPurchaseOrder(Order order) {
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

        MailRequest mailRequest = MailRequest.builderToFactory()
                .subject(subject)
                .title(title)
                .content(content)
                .buttonText("거래 정보 확인하기")
                .buttonUrl("https://www.kumoh.org/order/" + order.getId())
                .buildToFactory();
        mailService.sendEmail(mailRequest);
    }

    @Transactional(readOnly = true)
    public void sendEmailForUpdateOrderPurchaseOrder(Order order) {
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
