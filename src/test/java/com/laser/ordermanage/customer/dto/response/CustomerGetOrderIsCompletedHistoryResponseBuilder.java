package com.laser.ordermanage.customer.dto.response;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CustomerGetOrderIsCompletedHistoryResponseBuilder {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static List<CustomerGetOrderIsCompletedHistoryResponse> build() {
        LocalDateTime createdAtOfOrder1 = LocalDateTime.parse("2023-10-12 10:20:30", formatter);
        CustomerGetOrderIsCompletedHistoryResponse orderHistory1 = new CustomerGetOrderIsCompletedHistoryResponse(1L, "거래 1 이름", "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output.png", createdAtOfOrder1);

        return List.of(orderHistory1);
    }
}
