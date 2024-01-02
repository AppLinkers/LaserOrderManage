package com.laser.ordermanage.order.dto.response;

import com.querydsl.core.annotations.QueryProjection;

import java.time.LocalDateTime;

public record GetCommentResponse(
        Long id,
        String authorName,
        String content,
        LocalDateTime createdAt
) {
    @QueryProjection
    public GetCommentResponse(Long id, String authorName, String content, LocalDateTime createdAt) {
        this.id = id;
        this.authorName = authorName;
        this.content = content;
        this.createdAt = createdAt;
    }
}
