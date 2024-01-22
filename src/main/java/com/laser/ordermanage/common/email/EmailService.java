package com.laser.ordermanage.common.email;

import com.laser.ordermanage.common.email.dto.EmailRequest;
import com.laser.ordermanage.common.exception.CommonErrorCode;
import com.laser.ordermanage.common.exception.CustomCommonException;
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
public class EmailService {

    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;

    @Async("mailExecutor")
    public void sendEmail(EmailRequest emailRequest) {
        try {
            MimeMessage emailForm = createEmailForm(emailRequest);
            emailSender.send(emailForm);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomCommonException(CommonErrorCode.UNABLE_TO_SEND_EMAIL);
        }
    }

    // 발신할 이메일 데이터 세팅
    private MimeMessage createEmailForm(EmailRequest emailRequest) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, emailRequest.recipient());
        message.setSubject(emailRequest.subject());

        Context context = new Context();
        context.setVariable("title", emailRequest.title());
        context.setVariable("content", emailRequest.content());
        context.setVariable("buttonText", emailRequest.buttonText());
        context.setVariable("buttonUrl", emailRequest.buttonUrl());
        message.setText(templateEngine.process("mail", context), "utf-8", "html");

        return message;
    }

}
