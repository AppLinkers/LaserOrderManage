package com.laser.ordermanage.order.dto.request;

public class CreateCommentRequestBuilder {

    public static CreateCommentRequest build() {
        return new CreateCommentRequest("신규 댓글 내용");
    }
}
