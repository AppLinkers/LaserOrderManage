package com.laser.ordermanage.factory.dto.response;

import com.laser.ordermanage.order.domain.OrderManufacturing;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FactoryGetOrderIsNewAndIsNewIssueHistoryResponseBuilder {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static List<FactoryGetOrderIsNewAndIsNewIssueHistoryResponse> build() {
        OrderManufacturing manufacturingOfOrder1 = OrderManufacturing.ofRequest(List.of("bending", "welding"));
        LocalDateTime createdAtOfOrder1 = LocalDateTime.parse("2023-10-21 10:20:30", formatter);
        LocalDate deliveryAtOfOrder1 = LocalDate.of(2023, 10, 28);
        FactoryGetOrderIsNewAndIsNewIssueHistoryResponse orderHistory1 = new FactoryGetOrderIsNewAndIsNewIssueHistoryResponse(12L, "거래 12 이름", "고객 이름 4", "고객 회사 이름 3", Boolean.TRUE, Boolean.TRUE, "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output12.png", Boolean.FALSE, manufacturingOfOrder1, createdAtOfOrder1, deliveryAtOfOrder1, 1200000000L, null);

        OrderManufacturing manufacturingOfOrder2 = OrderManufacturing.ofRequest(List.of("welding"));
        LocalDateTime createdAtOfOrder2 = LocalDateTime.parse("2023-10-22 14:20:30", formatter);
        LocalDate deliveryAtOfOrder2 = LocalDate.of(2023, 10, 29);
        FactoryGetOrderIsNewAndIsNewIssueHistoryResponse orderHistory2 = new FactoryGetOrderIsNewAndIsNewIssueHistoryResponse(13L, "거래 13 이름", "고객 이름 5", null, Boolean.TRUE, Boolean.TRUE, "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output13.png", Boolean.TRUE, manufacturingOfOrder2, createdAtOfOrder2, deliveryAtOfOrder2, 1300000000L, "거래 13 요청사항");

        OrderManufacturing manufacturingOfOrder3 = OrderManufacturing.ofRequest(List.of("laser-cutting"));
        LocalDateTime createdOfOrder3 = LocalDateTime.parse("2023-10-23 15:20:30", formatter);
        FactoryGetOrderIsNewAndIsNewIssueHistoryResponse orderHistory3 = new FactoryGetOrderIsNewAndIsNewIssueHistoryResponse(14L, "거래 14 이름", "고객 이름 6", "고객 회사 이름 5", Boolean.TRUE, Boolean.FALSE, "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output14.png", Boolean.FALSE, manufacturingOfOrder3, createdOfOrder3, null, null, "거래 14 요청사항");

        OrderManufacturing manufacturingOfOrder4 = OrderManufacturing.ofRequest(List.of("bending", "welding"));
        LocalDateTime createdAtOfOrder4 = LocalDateTime.parse("2023-10-27 10:20:30", formatter);
        FactoryGetOrderIsNewAndIsNewIssueHistoryResponse orderHistory4 = new FactoryGetOrderIsNewAndIsNewIssueHistoryResponse(11L, "거래 11 이름", "고객 이름 3", null, Boolean.FALSE, Boolean.FALSE, "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output11.png", Boolean.FALSE, manufacturingOfOrder4, createdAtOfOrder4, null, null, "거래 11 요청사항");

        return List.of(orderHistory4, orderHistory3, orderHistory2, orderHistory1);
    }

    public static List<FactoryGetOrderIsNewAndIsNewIssueHistoryResponse> buildOfHasQuotationIsTrue() {
        OrderManufacturing manufacturingOfOrder1 = OrderManufacturing.ofRequest(List.of("bending", "welding"));
        LocalDateTime createdAtOfOrder1 = LocalDateTime.parse("2023-10-21 10:20:30", formatter);
        LocalDate deliveryAtOfOrder1 = LocalDate.of(2023, 10, 28);
        FactoryGetOrderIsNewAndIsNewIssueHistoryResponse orderHistory1 = new FactoryGetOrderIsNewAndIsNewIssueHistoryResponse(12L, "거래 12 이름", "고객 이름 4", "고객 회사 이름 3", Boolean.TRUE, Boolean.TRUE, "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output12.png", Boolean.FALSE, manufacturingOfOrder1, createdAtOfOrder1, deliveryAtOfOrder1, 1200000000L, null);

        OrderManufacturing manufacturingOfOrder2 = OrderManufacturing.ofRequest(List.of("welding"));
        LocalDateTime createdAtOfOrder2 = LocalDateTime.parse("2023-10-22 14:20:30", formatter);
        LocalDate deliveryAtOfOrder2 = LocalDate.of(2023, 10, 29);
        FactoryGetOrderIsNewAndIsNewIssueHistoryResponse orderHistory2 = new FactoryGetOrderIsNewAndIsNewIssueHistoryResponse(13L, "거래 13 이름", "고객 이름 5", null, Boolean.TRUE, Boolean.TRUE, "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output13.png", Boolean.TRUE, manufacturingOfOrder2, createdAtOfOrder2, deliveryAtOfOrder2, 1300000000L, "거래 13 요청사항");

        return List.of(orderHistory2, orderHistory1);
    }

    public static List<FactoryGetOrderIsNewAndIsNewIssueHistoryResponse> buildOfHasQuotationIsFalse() {
        OrderManufacturing manufacturingOfOrder1 = OrderManufacturing.ofRequest(List.of("laser-cutting"));
        LocalDateTime createdOfOrder1 = LocalDateTime.parse("2023-10-23 15:20:30", formatter);
        FactoryGetOrderIsNewAndIsNewIssueHistoryResponse orderHistory1 = new FactoryGetOrderIsNewAndIsNewIssueHistoryResponse(14L, "거래 14 이름", "고객 이름 6", "고객 회사 이름 5", Boolean.TRUE, Boolean.FALSE, "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output14.png", Boolean.FALSE, manufacturingOfOrder1, createdOfOrder1, null, null, "거래 14 요청사항");

        OrderManufacturing manufacturingOfOrder2 = OrderManufacturing.ofRequest(List.of("bending", "welding"));
        LocalDateTime createdAtOfOrder2 = LocalDateTime.parse("2023-10-27 10:20:30", formatter);
        FactoryGetOrderIsNewAndIsNewIssueHistoryResponse orderHistory2 = new FactoryGetOrderIsNewAndIsNewIssueHistoryResponse(11L, "거래 11 이름", "고객 이름 3", null, Boolean.FALSE, Boolean.FALSE, "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output11.png", Boolean.FALSE, manufacturingOfOrder2, createdAtOfOrder2, null, null, "거래 11 요청사항");

        return List.of(orderHistory2, orderHistory1);
    }

    public static List<FactoryGetOrderIsNewAndIsNewIssueHistoryResponse> buildOfIsNewCustomerIsTrue() {
        OrderManufacturing manufacturingOfOrder1 = OrderManufacturing.ofRequest(List.of("bending", "welding"));
        LocalDateTime createdAtOfOrder1 = LocalDateTime.parse("2023-10-21 10:20:30", formatter);
        LocalDate deliveryAtOfOrder1 = LocalDate.of(2023, 10, 28);
        FactoryGetOrderIsNewAndIsNewIssueHistoryResponse orderHistory1 = new FactoryGetOrderIsNewAndIsNewIssueHistoryResponse(12L, "거래 12 이름", "고객 이름 4", "고객 회사 이름 3", Boolean.TRUE, Boolean.TRUE, "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output12.png", Boolean.FALSE, manufacturingOfOrder1, createdAtOfOrder1, deliveryAtOfOrder1, 1200000000L, null);

        OrderManufacturing manufacturingOfOrder2 = OrderManufacturing.ofRequest(List.of("welding"));
        LocalDateTime createdAtOfOrder2 = LocalDateTime.parse("2023-10-22 14:20:30", formatter);
        LocalDate deliveryAtOfOrder2 = LocalDate.of(2023, 10, 29);
        FactoryGetOrderIsNewAndIsNewIssueHistoryResponse orderHistory2 = new FactoryGetOrderIsNewAndIsNewIssueHistoryResponse(13L, "거래 13 이름", "고객 이름 5", null, Boolean.TRUE, Boolean.TRUE, "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output13.png", Boolean.TRUE, manufacturingOfOrder2, createdAtOfOrder2, deliveryAtOfOrder2, 1300000000L, "거래 13 요청사항");

        OrderManufacturing manufacturingOfOrder3 = OrderManufacturing.ofRequest(List.of("laser-cutting"));
        LocalDateTime createdOfOrder3 = LocalDateTime.parse("2023-10-23 15:20:30", formatter);
        FactoryGetOrderIsNewAndIsNewIssueHistoryResponse orderHistory3 = new FactoryGetOrderIsNewAndIsNewIssueHistoryResponse(14L, "거래 14 이름", "고객 이름 6", "고객 회사 이름 5", Boolean.TRUE, Boolean.FALSE, "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output14.png", Boolean.FALSE, manufacturingOfOrder3, createdOfOrder3, null, null, "거래 14 요청사항");

        return List.of(orderHistory3, orderHistory2, orderHistory1);
    }

    public static List<FactoryGetOrderIsNewAndIsNewIssueHistoryResponse> buildOfIsNewCustomerIsFalse() {
        OrderManufacturing manufacturingOfOrder1 = OrderManufacturing.ofRequest(List.of("bending", "welding"));
        LocalDateTime createdAtOfOrder1 = LocalDateTime.parse("2023-10-27 10:20:30", formatter);
        FactoryGetOrderIsNewAndIsNewIssueHistoryResponse orderHistory1 = new FactoryGetOrderIsNewAndIsNewIssueHistoryResponse(11L, "거래 11 이름", "고객 이름 3", null, Boolean.FALSE, Boolean.FALSE, "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output11.png", Boolean.FALSE, manufacturingOfOrder1, createdAtOfOrder1, null, null, "거래 11 요청사항");

        return List.of(orderHistory1);
    }

    public static List<FactoryGetOrderIsNewAndIsNewIssueHistoryResponse> buildOfIsUrgentIsTrue() {
        OrderManufacturing manufacturingOfOrder1 = OrderManufacturing.ofRequest(List.of("welding"));
        LocalDateTime createdAtOfOrder1 = LocalDateTime.parse("2023-10-22 14:20:30", formatter);
        LocalDate deliveryAtOfOrder1 = LocalDate.of(2023, 10, 29);
        FactoryGetOrderIsNewAndIsNewIssueHistoryResponse orderHistory1 = new FactoryGetOrderIsNewAndIsNewIssueHistoryResponse(13L, "거래 13 이름", "고객 이름 5", null, Boolean.TRUE, Boolean.TRUE, "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output13.png", Boolean.TRUE, manufacturingOfOrder1, createdAtOfOrder1, deliveryAtOfOrder1, 1300000000L, "거래 13 요청사항");

        return List.of(orderHistory1);
    }

    public static List<FactoryGetOrderIsNewAndIsNewIssueHistoryResponse> buildOfIsUrgentIsFalse() {
        OrderManufacturing manufacturingOfOrder1 = OrderManufacturing.ofRequest(List.of("bending", "welding"));
        LocalDateTime createdAtOfOrder1 = LocalDateTime.parse("2023-10-21 10:20:30", formatter);
        LocalDate deliveryAtOfOrder1 = LocalDate.of(2023, 10, 28);
        FactoryGetOrderIsNewAndIsNewIssueHistoryResponse orderHistory1 = new FactoryGetOrderIsNewAndIsNewIssueHistoryResponse(12L, "거래 12 이름", "고객 이름 4", "고객 회사 이름 3", Boolean.TRUE, Boolean.TRUE, "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output12.png", Boolean.FALSE, manufacturingOfOrder1, createdAtOfOrder1, deliveryAtOfOrder1, 1200000000L, null);

        OrderManufacturing manufacturingOfOrder2 = OrderManufacturing.ofRequest(List.of("laser-cutting"));
        LocalDateTime createdOfOrder2 = LocalDateTime.parse("2023-10-23 15:20:30", formatter);
        FactoryGetOrderIsNewAndIsNewIssueHistoryResponse orderHistory2 = new FactoryGetOrderIsNewAndIsNewIssueHistoryResponse(14L, "거래 14 이름", "고객 이름 6", "고객 회사 이름 5", Boolean.TRUE, Boolean.FALSE, "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output14.png", Boolean.FALSE, manufacturingOfOrder2, createdOfOrder2, null, null, "거래 14 요청사항");

        OrderManufacturing manufacturingOfOrder3 = OrderManufacturing.ofRequest(List.of("bending", "welding"));
        LocalDateTime createdAtOfOrder3 = LocalDateTime.parse("2023-10-27 10:20:30", formatter);
        FactoryGetOrderIsNewAndIsNewIssueHistoryResponse orderHistory3 = new FactoryGetOrderIsNewAndIsNewIssueHistoryResponse(11L, "거래 11 이름", "고객 이름 3", null, Boolean.FALSE, Boolean.FALSE, "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output11.png", Boolean.FALSE, manufacturingOfOrder3, createdAtOfOrder3, null, null, "거래 11 요청사항");

        return List.of(orderHistory3, orderHistory2, orderHistory1);
    }
}
