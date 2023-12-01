package com.laser.ordermanage.order.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GetCommentResponse {

    private final Long id;

    private final String authorName;

    private final String content;

    private final LocalDateTime createdAt;

    @QueryProjection
    public GetCommentResponse(Long id, String authorName, String content, LocalDateTime createdAt) {
        this.id = id;
        this.authorName = authorName;
        this.content = content;
        this.createdAt = createdAt;
    }
}
