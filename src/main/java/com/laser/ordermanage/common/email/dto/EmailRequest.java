package com.laser.ordermanage.common.email.dto;

import lombok.Builder;

public record EmailRequest(
        String recipient,
        String subject,
        String title,
        String content,
        String buttonText,
        String buttonUrl
) {

    @Builder
    public EmailRequest(String recipient, String subject, String title, String content, String buttonText, String buttonUrl) {
        this.recipient = recipient;
        this.subject = subject;
        this.title = title;
        this.content = content;
        this.buttonText = buttonText;
        this.buttonUrl = buttonUrl;
    }
}
