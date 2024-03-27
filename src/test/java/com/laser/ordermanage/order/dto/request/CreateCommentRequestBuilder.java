package com.laser.ordermanage.order.dto.request;

public class CreateCommentRequestBuilder {

    public static CreateCommentRequest build() {
        return new CreateCommentRequest("신규 댓글 내용");
    }

    public static CreateCommentRequest nullContentBuild() {
        return new CreateCommentRequest(null);
    }

    public static CreateCommentRequest emptyContentBuild() {
        return new CreateCommentRequest("");
    }

    public static CreateCommentRequest invalidContentBuild() {
        return new CreateCommentRequest("댓글 내용".repeat(41));
    }
}
