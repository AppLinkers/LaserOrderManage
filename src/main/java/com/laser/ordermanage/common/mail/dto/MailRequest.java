package com.laser.ordermanage.common.mail.dto;

import lombok.Builder;

public record MailRequest(
        String toEmail,
        String subject,
        String title,
        String content,
        String buttonText,
        String buttonUrl
) {

    @Builder
    public MailRequest(String toEmail, String subject, String title, String content, String buttonText, String buttonUrl) {
        this.toEmail = toEmail;
        this.subject = subject;
        this.title = title;
        this.content = content;
        this.buttonText = buttonText;
        this.buttonUrl = buttonUrl;
    }

    @Builder(builderMethodName = "builderToFactory", buildMethodName = "buildToFactory")
    public MailRequest(String subject, String title, String content, String buttonText, String buttonUrl) {
        this(
                "admin@kumoh.org",
                subject,
                title,
                content,
                buttonText,
                buttonUrl
        );
    }
}
