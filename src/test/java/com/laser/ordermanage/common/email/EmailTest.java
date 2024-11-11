package com.laser.ordermanage.common.email;

import com.laser.ordermanage.OrderManageApplication;
import com.laser.ordermanage.common.IntegrationTest;
import com.laser.ordermanage.common.email.dto.EmailRequest;
import jakarta.mail.internet.MimeMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = OrderManageApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class EmailTest {

    @Autowired
    private EmailService emailService;

    @SpyBean
    private JavaMailSender mailSender;

    @Autowired
    private ThreadPoolTaskExecutor executor;

    @Test
    public void 메일_발송_성공() throws Exception {
        // given
        final EmailRequest emailRequest = EmailRequest.builder()
                .recipient("yuseogi0218@gmail.com")
                .subject("테스트 메일")
                .text("메일 내용")
                .build();

        // when
        emailService.sendEmail(emailRequest);
        executor.getThreadPoolExecutor().awaitTermination(3, TimeUnit.SECONDS);

        // then
        verify(mailSender, times(1)).send((MimeMessage) any());
    }
}
