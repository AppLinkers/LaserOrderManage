package com.laser.ordermanage.order.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class CreateCommentRequest {

    @NotEmpty(message = "댓글 내용은 필수 입력값입니다.")
    private String content;
}
