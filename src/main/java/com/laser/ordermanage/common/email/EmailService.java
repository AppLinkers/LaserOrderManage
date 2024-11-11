package com.laser.ordermanage.common.email;

import com.laser.ordermanage.common.email.dto.EmailRequest;
import com.laser.ordermanage.common.email.dto.EmailWithButtonRequest;
import com.laser.ordermanage.common.email.dto.EmailWithCodeRequest;
import com.laser.ordermanage.common.exception.CommonErrorCode;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.slack.SlackService;
import com.laser.ordermanage.common.timer.Timer;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static jakarta.mail.Message.RecipientType.TO;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;
    private final TemplateEngine templateEngine;
    private final Executor asyncExecutor;

    private final SlackService slackService;

    private final static int MAX_RETRIES = 3;

    public void sendEmail(EmailRequest emailRequest) {
        MimeMessage emailForm = createEmailForm(emailRequest);

        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> emailSender.send(emailForm), asyncExecutor);

        AtomicInteger attempt = new AtomicInteger(0);
        for (int i = 1; i <= MAX_RETRIES; i ++) {
            completableFuture = completableFuture.thenApply(CompletableFuture::completedFuture)
                    .exceptionally(__ -> {
                        if (attempt.incrementAndGet() < MAX_RETRIES) {
                            return CompletableFuture.runAsync(() -> emailSender.send(emailForm), asyncExecutor);
                        } else {
                            slackService.sendSlackAlertForMailFailure(emailForm);
                            return null;
                        }
                    })
                    .thenCompose(Function.identity());
        }
    }

    // 발신할 이메일 데이터 세팅
    private MimeMessage createEmailForm(EmailRequest emailRequest) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            message.addRecipients(TO, emailRequest.recipient());
            message.setSubject(emailRequest.subject());
            message.setText(emailRequest.text(), "utf-8", "html");

            return message;
        } catch (Exception e) {
            throw new CustomCommonException(CommonErrorCode.UNABLE_TO_SEND_EMAIL);
        }
    }

    public void sendEmailWithButton(EmailWithButtonRequest request) {
        Context context = new Context();
        context.setVariable("title", request.title());
        context.setVariable("content", request.content());
        context.setVariable("buttonText", request.buttonText());
        context.setVariable("buttonUrl", request.buttonUrl());
        String text = templateEngine.process("email-with-button", context);

        EmailRequest emailRequest = EmailRequest.builder()
                .recipient(request.recipient())
                .subject(request.subject())
                .text(text)
                .build();

        sendEmail(emailRequest);
    }

    public void sendEmailWithCode(EmailWithCodeRequest request) {
        Context context = new Context();
        context.setVariable("title", request.title());
        context.setVariable("content", request.content());
        context.setVariable("code", request.code());
        String text = templateEngine.process("email-with-code", context);

        EmailRequest emailRequest = EmailRequest.builder()
                .recipient(request.recipient())
                .subject(request.subject())
                .text(text)
                .build();

        sendEmail(emailRequest);
    }

}
