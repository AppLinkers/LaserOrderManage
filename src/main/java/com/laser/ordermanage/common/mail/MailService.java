package com.laser.ordermanage.common.mail;

import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;


@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender emailSender;

    @Async("mailExecutor")
    public void sendEmail(String toEmail, String title, String text) {
        SimpleMailMessage emailForm = createEmailForm(toEmail, title, text);
        try {
            emailSender.send(emailForm);
        } catch (RuntimeException e) {
            throw new CustomCommonException(ErrorCode.UNABLE_TO_SEND_EMAIL);
        }
    }

    // 공장에게 메일 전송
    @Async("mailExecutor")
    public void sendEmailToFactory(String title, String text) {
        SimpleMailMessage emailForm = createEmailForm("admin@kumoh.org", title, text);
        try {
            emailSender.send(emailForm);
        } catch (RuntimeException e) {
            throw new CustomCommonException(ErrorCode.UNABLE_TO_SEND_EMAIL);
        }
    }

    // 발신할 이메일 데이터 세팅
    private SimpleMailMessage createEmailForm(String toEmail, String title, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(title);
        message.setText(text);

        return message;
    }

    public String createVerifyCode() {
        int length = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < length; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new CustomCommonException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
