package com.laser.ordermanage.order.dto.response;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GetCommentResponseBuilder {

    public static List<GetCommentResponse> buildCommentListForOrder1() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime createdAtOfComment1 = LocalDateTime.parse("2023-10-13 10:20:30", formatter);
        GetCommentResponse comment1 = new GetCommentResponse(1L, "관리자", "안녕하세요 고객님\\n어떻게 도와드릴까요?", createdAtOfComment1);

        LocalDateTime createdAtComment2 = LocalDateTime.parse("2023-10-14 10:20:30", formatter);
        GetCommentResponse comment2 = new GetCommentResponse(2L, "고객 이름 1", "견적서에 대해 궁금해서\\n언제 연락 괜찮으신가요?", createdAtComment2);

        return List.of(comment1, comment2);
    }
}
