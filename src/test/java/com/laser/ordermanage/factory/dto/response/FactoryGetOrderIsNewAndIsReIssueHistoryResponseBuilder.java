package com.laser.ordermanage.factory.dto.response;

import com.laser.ordermanage.order.domain.OrderManufacturing;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FactoryGetOrderIsNewAndIsReIssueHistoryResponseBuilder {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static List<FactoryGetOrderIsNewAndIsReIssueHistoryResponse> build() {
        OrderManufacturing manufacturingOfOrder1 = OrderManufacturing.ofRequest(List.of("bending"));
        LocalDateTime createdAtOfOrder1 = LocalDateTime.parse("2023-10-23 14:20:30", formatter);
        FactoryGetOrderIsNewAndIsReIssueHistoryResponse orderHistory1 = new FactoryGetOrderIsNewAndIsReIssueHistoryResponse(5L, "거래 5 이름", "고객 이름 1", "고객 회사 이름 1", Boolean.FALSE, "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output5.png", Boolean.FALSE, manufacturingOfOrder1, createdAtOfOrder1, null, null, "거래 5 요청사항");

        OrderManufacturing manufacturingOfOrder2 = OrderManufacturing.ofRequest(List.of("laser-cutting", "bending", "welding"));
        LocalDateTime createdAtOfOrder2 = LocalDateTime.parse("2023-10-25 10:20:30", formatter);
        LocalDate deliveryAtOfOrder2 = LocalDate.of(2023, 11, 1);
        FactoryGetOrderIsNewAndIsReIssueHistoryResponse orderHistory2 = new FactoryGetOrderIsNewAndIsReIssueHistoryResponse(10L, "거래 10 이름", "고객 이름 3", null, Boolean.TRUE, "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output10.png", Boolean.TRUE, manufacturingOfOrder2, createdAtOfOrder2, deliveryAtOfOrder2, 1000000000L, "거래 10 요청사항");

        return List.of(orderHistory2, orderHistory1);
    }

    public static List<FactoryGetOrderIsNewAndIsReIssueHistoryResponse> buildOfHasQuotationIsTrueAndIsUrgentIsTrue() {
        OrderManufacturing manufacturingOfOrder1 = OrderManufacturing.ofRequest(List.of("laser-cutting", "bending", "welding"));
        LocalDateTime createdAtOfOrder1 = LocalDateTime.parse("2023-10-25 10:20:30", formatter);
        LocalDate deliveryAtOfOrder1 = LocalDate.of(2023, 11, 1);
        FactoryGetOrderIsNewAndIsReIssueHistoryResponse orderHistory1 = new FactoryGetOrderIsNewAndIsReIssueHistoryResponse(10L, "거래 10 이름", "고객 이름 3", null, Boolean.TRUE, "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output10.png", Boolean.TRUE, manufacturingOfOrder1, createdAtOfOrder1, deliveryAtOfOrder1, 1000000000L, "거래 10 요청사항");

        return List.of(orderHistory1);
    }

    public static List<FactoryGetOrderIsNewAndIsReIssueHistoryResponse> buildOfHasQuotationIsFalseAndIsUrgentIsFalse() {
        OrderManufacturing manufacturingOfOrder1 = OrderManufacturing.ofRequest(List.of("bending"));
        LocalDateTime createdAtOfOrder1 = LocalDateTime.parse("2023-10-23 14:20:30", formatter);
        FactoryGetOrderIsNewAndIsReIssueHistoryResponse orderHistory1 = new FactoryGetOrderIsNewAndIsReIssueHistoryResponse(5L, "거래 5 이름", "고객 이름 1", "고객 회사 이름 1", Boolean.FALSE, "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output5.png", Boolean.FALSE, manufacturingOfOrder1, createdAtOfOrder1, null, null, "거래 5 요청사항");

        return List.of(orderHistory1);
    }

}
