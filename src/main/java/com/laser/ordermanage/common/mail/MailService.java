package com.laser.ordermanage.common.mail;

import com.laser.ordermanage.common.exception.CommonErrorCode;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.mail.dto.MailRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;


@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;

    @Async("mailExecutor")
    public void sendEmail(MailRequest mailRequest) {
        try {
            MimeMessage emailForm = createEmailForm(mailRequest);
            emailSender.send(emailForm);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomCommonException(CommonErrorCode.UNABLE_TO_SEND_EMAIL);
        }
    }

    // 발신할 이메일 데이터 세팅
    private MimeMessage createEmailForm(MailRequest mailRequest) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, mailRequest.toEmail());
        message.setSubject(mailRequest.subject());

        Context context = new Context();
        context.setVariable("title", mailRequest.title());
        context.setVariable("content", mailRequest.content());
        context.setVariable("buttonText", mailRequest.buttonText());
        context.setVariable("buttonUrl", mailRequest.buttonUrl());
        message.setText(templateEngine.process("mail", context), "utf-8", "html");

        return message;
    }

}
