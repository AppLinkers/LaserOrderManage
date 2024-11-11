package com.laser.ordermanage.common.slack;

import com.laser.ordermanage.common.util.NetworkUtil;
import com.slack.api.Slack;
import com.slack.api.model.Attachment;
import com.slack.api.model.Field;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.slack.api.webhook.WebhookPayloads.payload;

@Slf4j
@Service
@RequiredArgsConstructor
public class SlackService {

    private final Slack slackClient = Slack.getInstance();

    @Value("${slack.webhook.url}")
    private String webhookUrl;

    // 슬랙 알림 보내는 메서드
    public void sendSlackAlertErrorLog(Exception e, HttpServletRequest request) {
        try {
            slackClient.send(webhookUrl, payload(p -> p
                    .text("서버 에러 발생!")
                    .attachments(
                            List.of(generateSlackAttachment(e, request))
                    )
            ));
        } catch (IOException slackError) {
            // slack 통신 시 발생한 예외에서 Exception을 던져준다면 재귀적인 예외가 발생합니다.
            // 따라서 로깅으로 처리하였고, 서버 에러는 아니므로 `error` 레벨보다 낮은 레벨로 설정했습니다.
            log.debug("Slack 통신과의 예외 발생");
        }
    }

    // 슬랙 알림 보내는 메서드
    public void sendSlackAlertForMailFailure(MimeMessage emailForm) {
        try {
            slackClient.send(webhookUrl, payload(p -> p
                    .text("메일 발송 에러 발생!")
                    .attachments(
                            List.of(generateSlackAttachment(emailForm))
                    )
            ));
        } catch (IOException slackError) {
            // slack 통신 시 발생한 예외에서 Exception을 던져준다면 재귀적인 예외가 발생합니다.
            // 따라서 로깅으로 처리하였고, 서버 에러는 아니므로 `error` 레벨보다 낮은 레벨로 설정했습니다.
            log.debug("Slack 통신과의 예외 발생");
        }
    }

    // attachment 생성 메서드
    private Attachment generateSlackAttachment(Exception e, HttpServletRequest request) {
        String requestTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").format(LocalDateTime.now());
        String requestIP = NetworkUtil.getClientIp(request);

        List<Field> fieldList = new ArrayList<>();
        fieldList.add(new Field("Request IP", requestIP, false));
        fieldList.add(new Field("Request URL", request.getMethod() + " " + request.getRequestURL(), false));

        if (request.getContentType() != null && request.getContentType().contains("application/json")) {
            try {
                String messageBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
                fieldList.add(new Field("Request Body", messageBody, false));
            } catch (IOException ex) {
                log.debug("Extract Request Body 예외 발생");
            }
        }

        fieldList.add(new Field("Error Message", e.getMessage(), false));

        return Attachment.builder()
                .color("ff0000")  // 붉은 색으로 보이도록
                .title(requestTime + " 발생한 에러 로그")
                .fields(fieldList)
                .build();
    }

    // attachment 생성 메서드
    private Attachment generateSlackAttachment(MimeMessage emailForm) {
        String requestTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").format(LocalDateTime.now());

        List<Field> fieldList = new ArrayList<>();
        try {
            fieldList.add(new Field("Receiver", Arrays.toString(emailForm.getRecipients(MimeMessage.RecipientType.TO)), false));
            fieldList.add(new Field("Subject", emailForm.getSubject(), false));
            fieldList.add(new Field("Text", emailForm.getContent().toString(), false));
        } catch (Exception ex) {
            log.debug("메일 정보 추출 예외 발생");
        }

        return Attachment.builder()
                .color("ff0000")
                .title(requestTime + " 발생한 메일 발송 에러 로그")
                .fields(fieldList)
                .build();
    }

}
