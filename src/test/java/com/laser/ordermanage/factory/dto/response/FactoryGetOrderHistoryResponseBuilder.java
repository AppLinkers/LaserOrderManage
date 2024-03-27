package com.laser.ordermanage.factory.dto.response;

import com.laser.ordermanage.order.domain.OrderManufacturing;
import com.laser.ordermanage.order.domain.type.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FactoryGetOrderHistoryResponseBuilder {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static List<FactoryGetOrderHistoryResponse> buildOfIsCompletedFalseAndPage1() {
        OrderManufacturing manufacturingOfOrder1 = OrderManufacturing.ofRequest(List.of("laser-cutting", "bending", "welding"));
        LocalDateTime createdAtOfOrder1 = LocalDateTime.parse("2023-10-15 10:20:30", formatter);
        LocalDate deliveryAtOfOrder1 = LocalDate.of(2023, 10, 22);
        FactoryGetOrderHistoryResponse orderHistory1 = new FactoryGetOrderHistoryResponse(4L, "거래 4 이름", "고객 이름 1", "고객 회사 이름 1", "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output4.png", Stage.QUOTE_APPROVAL, Boolean.FALSE, manufacturingOfOrder1, createdAtOfOrder1, deliveryAtOfOrder1, 4000000000L, "거래 4 요청사항");

        OrderManufacturing manufacturingOfOrder2 = OrderManufacturing.ofRequest(List.of("bending"));
        LocalDateTime createdAtOfOrder2 = LocalDateTime.parse("2023-10-16 10:20:30", formatter);
        LocalDate deliveryAtOfOrder2 = LocalDate.of(2023, 10, 23);
        FactoryGetOrderHistoryResponse orderHistory2 = new FactoryGetOrderHistoryResponse(7L, "거래 7 이름", "고객 이름 3", null, "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output7.png", Stage.PRODUCTION_COMPLETED, Boolean.TRUE, manufacturingOfOrder2, createdAtOfOrder2, deliveryAtOfOrder2, 700000000L, "거래 7 요청사항");

        OrderManufacturing manufacturingOfOrder3 = OrderManufacturing.ofRequest(List.of("bending", "welding"));
        LocalDateTime createdAtOfOrder3 = LocalDateTime.parse("2023-10-21 10:20:30", formatter);
        LocalDate deliveryAtOfOrder3 = LocalDate.of(2023, 10, 28);
        FactoryGetOrderHistoryResponse orderHistory3 = new FactoryGetOrderHistoryResponse(12L, "거래 12 이름", "고객 이름 4", "고객 회사 이름 3", "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output12.png", Stage.NEW, Boolean.FALSE, manufacturingOfOrder3, createdAtOfOrder3, deliveryAtOfOrder3, 1200000000L, null);

        OrderManufacturing manufacturingOfOrder4 = OrderManufacturing.ofRequest(List.of("bending"));
        LocalDateTime createdAtOfOrder4 = LocalDateTime.parse("2023-10-22 10:20:30", formatter);
        LocalDate deliveryAtOfOrder4 = LocalDate.of(2023, 10, 29);
        FactoryGetOrderHistoryResponse orderHistory4 = new FactoryGetOrderHistoryResponse(8L, "거래 8 이름", "고객 이름 3", null, "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output8.png", Stage.IN_PRODUCTION, Boolean.FALSE, manufacturingOfOrder4, createdAtOfOrder4, deliveryAtOfOrder4, 800000000L, null);

        OrderManufacturing manufacturingOfOrder5 = OrderManufacturing.ofRequest(List.of("welding"));
        LocalDateTime createdAtOfOrder5 = LocalDateTime.parse("2023-10-22 14:20:30", formatter);
        LocalDate deliveryAtOfOrder5 = LocalDate.of(2023, 10, 29);
        FactoryGetOrderHistoryResponse orderHistory5 = new FactoryGetOrderHistoryResponse(13L, "거래 13 이름", "고객 이름 5", null, "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output13.png", Stage.NEW, Boolean.TRUE, manufacturingOfOrder5, createdAtOfOrder5, deliveryAtOfOrder5, 1300000000L, "거래 13 요청사항");

        OrderManufacturing manufacturingOfOrder6 = OrderManufacturing.ofRequest(List.of("bending"));
        LocalDateTime createdAtOfOrder6 = LocalDateTime.parse("2023-10-23 14:20:30", formatter);
        FactoryGetOrderHistoryResponse orderHistory6 = new FactoryGetOrderHistoryResponse(5L, "거래 5 이름", "고객 이름 1", "고객 회사 이름 1", "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output5.png", Stage.NEW, Boolean.FALSE, manufacturingOfOrder6, createdAtOfOrder6, null, null, "거래 5 요청사항");

        OrderManufacturing manufacturingOfOrder7 = OrderManufacturing.ofRequest(List.of("laser-cutting"));
        LocalDateTime createdAtOfOrder7 = LocalDateTime.parse("2023-10-23 10:20:30", formatter);
        FactoryGetOrderHistoryResponse orderHistory7 = new FactoryGetOrderHistoryResponse(14L, "거래 14 이름", "고객 이름 6", "고객 회사 이름 5", "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output14.png", Stage.NEW, Boolean.FALSE, manufacturingOfOrder7, createdAtOfOrder7, null, null, "거래 14 요청사항");

        OrderManufacturing manufacturingOfOrder8 = OrderManufacturing.ofRequest(List.of("bending", "welding"));
        LocalDateTime createdAtOfOrder8 = LocalDateTime.parse("2023-10-24 10:20:30", formatter);
        LocalDate deliveryAtOfOrder8 = LocalDate.of(2023, 10, 31);
        FactoryGetOrderHistoryResponse orderHistory8 = new FactoryGetOrderHistoryResponse(9L, "거래 9 이름", "고객 이름 3", null, "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output9.png", Stage.QUOTE_APPROVAL, Boolean.FALSE, manufacturingOfOrder8, createdAtOfOrder8, deliveryAtOfOrder8, 900000000L, null);

        OrderManufacturing manufacturingOfOrder9 = OrderManufacturing.ofRequest(List.of("laser-cutting", "bending", "welding"));
        LocalDateTime createdAtOfOrder9 = LocalDateTime.parse("2023-10-25 10:20:30", formatter);
        LocalDate deliveryAtOfOrder9 = LocalDate.of(2023, 11, 1);
        FactoryGetOrderHistoryResponse orderHistory9 = new FactoryGetOrderHistoryResponse(10L, "거래 10 이름", "고객 이름 3", null, "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output10.png", Stage.NEW, Boolean.TRUE, manufacturingOfOrder9, createdAtOfOrder9, deliveryAtOfOrder9, 1000000000L, "거래 10 요청사항");

        OrderManufacturing manufacturingOfOrder10 = OrderManufacturing.ofRequest(List.of("bending", "welding"));
        LocalDateTime createdAtOfOrder10 = LocalDateTime.parse("2023-10-27 10:20:30", formatter);
        FactoryGetOrderHistoryResponse orderHistory10 = new FactoryGetOrderHistoryResponse(11L, "거래 11 이름", "고객 이름 3", null, "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output11.png", Stage.NEW, Boolean.FALSE, manufacturingOfOrder10, createdAtOfOrder10, null, null, "거래 11 요청사항");

        return List.of(orderHistory10, orderHistory9, orderHistory8, orderHistory7, orderHistory6, orderHistory5, orderHistory4, orderHistory3, orderHistory2, orderHistory1);
    }

    public static List<FactoryGetOrderHistoryResponse> buildOfIsCompletedFalseAndIsUrgentTrue() {
        OrderManufacturing manufacturingOfOrder1 = OrderManufacturing.ofRequest(List.of("bending", "welding"));
        LocalDateTime createdAtOfOrder1 = LocalDateTime.parse("2023-10-14 10:20:30", formatter);
        LocalDate deliveryAtOfOrder1 = LocalDate.of(2023, 10, 21);
        FactoryGetOrderHistoryResponse orderHistory1 = new FactoryGetOrderHistoryResponse(3L, "거래 3 이름", "고객 이름 1", "고객 회사 이름 1", "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output3.png", Stage.IN_PRODUCTION, Boolean.TRUE, manufacturingOfOrder1, createdAtOfOrder1, deliveryAtOfOrder1, 300000L, null);

        OrderManufacturing manufacturingOfOrder2 = OrderManufacturing.ofRequest(List.of("bending"));
        LocalDateTime createdAtOfOrder2 = LocalDateTime.parse("2023-10-16 10:20:30", formatter);
        LocalDate deliveryAtOfOrder2 = LocalDate.of(2023, 10, 23);
        FactoryGetOrderHistoryResponse orderHistory2 = new FactoryGetOrderHistoryResponse(7L, "거래 7 이름", "고객 이름 3", null, "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output7.png", Stage.PRODUCTION_COMPLETED, Boolean.TRUE, manufacturingOfOrder2, createdAtOfOrder2, deliveryAtOfOrder2, 700000000L, "거래 7 요청사항");

        OrderManufacturing manufacturingOfOrder3 = OrderManufacturing.ofRequest(List.of("welding"));
        LocalDateTime createdAtOfOrder3 = LocalDateTime.parse("2023-10-22 14:20:30", formatter);
        LocalDate deliveryAtOfOrder3 = LocalDate.of(2023, 10, 29);
        FactoryGetOrderHistoryResponse orderHistory3 = new FactoryGetOrderHistoryResponse(13L, "거래 13 이름", "고객 이름 5", null, "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output13.png", Stage.NEW, Boolean.TRUE, manufacturingOfOrder3, createdAtOfOrder3, deliveryAtOfOrder3, 1300000000L, "거래 13 요청사항");

        OrderManufacturing manufacturingOfOrder4 = OrderManufacturing.ofRequest(List.of("laser-cutting", "bending", "welding"));
        LocalDateTime createdAtOfOrder4 = LocalDateTime.parse("2023-10-25 10:20:30", formatter);
        LocalDate deliveryAtOfOrder4 = LocalDate.of(2023, 11, 1);
        FactoryGetOrderHistoryResponse orderHistory4 = new FactoryGetOrderHistoryResponse(10L, "거래 10 이름", "고객 이름 3", null, "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output10.png", Stage.NEW, Boolean.TRUE, manufacturingOfOrder4, createdAtOfOrder4, deliveryAtOfOrder4, 1000000000L, "거래 10 요청사항");

        return List.of(orderHistory4, orderHistory3, orderHistory2, orderHistory1);
    }

    public static List<FactoryGetOrderHistoryResponse> buildOfIsCompletedFalseAndIsUrgentFalse() {
        OrderManufacturing manufacturingOfOrder1 = OrderManufacturing.ofRequest(List.of("laser-cutting", "bending"));
        LocalDateTime createdAtOfOrder1 = LocalDateTime.parse("2023-10-13 10:20:30", formatter);
        LocalDate deliveryAtOfOrder1 = LocalDate.of(2023, 10, 20);
        FactoryGetOrderHistoryResponse orderHistory1 = new FactoryGetOrderHistoryResponse(2L, "거래 2 이름", "고객 이름 1", "고객 회사 이름 1", "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output2.png", Stage.PRODUCTION_COMPLETED, Boolean.FALSE, manufacturingOfOrder1, createdAtOfOrder1, deliveryAtOfOrder1, 200000000L, "거래 2 요청사항");

        OrderManufacturing manufacturingOfOrder2 = OrderManufacturing.ofRequest(List.of("laser-cutting", "bending", "welding"));
        LocalDateTime createdAtOfOrder2 = LocalDateTime.parse("2023-10-15 10:20:30", formatter);
        LocalDate deliveryAtOfOrder2 = LocalDate.of(2023, 10, 22);
        FactoryGetOrderHistoryResponse orderHistory2 = new FactoryGetOrderHistoryResponse(4L, "거래 4 이름", "고객 이름 1", "고객 회사 이름 1", "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output4.png", Stage.QUOTE_APPROVAL, Boolean.FALSE, manufacturingOfOrder2, createdAtOfOrder2, deliveryAtOfOrder2, 4000000000L, "거래 4 요청사항");

        OrderManufacturing manufacturingOfOrder3 = OrderManufacturing.ofRequest(List.of("bending", "welding"));
        LocalDateTime createdAtOfOrder3 = LocalDateTime.parse("2023-10-21 10:20:30", formatter);
        LocalDate deliveryAtOfOrder3 = LocalDate.of(2023, 10, 28);
        FactoryGetOrderHistoryResponse orderHistory3 = new FactoryGetOrderHistoryResponse(12L, "거래 12 이름", "고객 이름 4", "고객 회사 이름 3", "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output12.png", Stage.NEW, Boolean.FALSE, manufacturingOfOrder3, createdAtOfOrder3, deliveryAtOfOrder3, 1200000000L, null);

        OrderManufacturing manufacturingOfOrder4 = OrderManufacturing.ofRequest(List.of("bending"));
        LocalDateTime createdAtOfOrder4 = LocalDateTime.parse("2023-10-22 10:20:30", formatter);
        LocalDate deliveryAtOfOrder4 = LocalDate.of(2023, 10, 29);
        FactoryGetOrderHistoryResponse orderHistory4 = new FactoryGetOrderHistoryResponse(8L, "거래 8 이름", "고객 이름 3", null, "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output8.png", Stage.IN_PRODUCTION, Boolean.FALSE, manufacturingOfOrder4, createdAtOfOrder4, deliveryAtOfOrder4, 800000000L, null);

        OrderManufacturing manufacturingOfOrder5 = OrderManufacturing.ofRequest(List.of("bending"));
        LocalDateTime createdAtOfOrder5 = LocalDateTime.parse("2023-10-23 14:20:30", formatter);
        FactoryGetOrderHistoryResponse orderHistory5 = new FactoryGetOrderHistoryResponse(5L, "거래 5 이름", "고객 이름 1", "고객 회사 이름 1", "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output5.png", Stage.NEW, Boolean.FALSE, manufacturingOfOrder5, createdAtOfOrder5, null, null, "거래 5 요청사항");

        OrderManufacturing manufacturingOfOrder6 = OrderManufacturing.ofRequest(List.of("laser-cutting"));
        LocalDateTime createdAtOfOrder6 = LocalDateTime.parse("2023-10-23 10:20:30", formatter);
        FactoryGetOrderHistoryResponse orderHistory6 = new FactoryGetOrderHistoryResponse(14L, "거래 14 이름", "고객 이름 6", "고객 회사 이름 5", "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output14.png", Stage.NEW, Boolean.FALSE, manufacturingOfOrder6, createdAtOfOrder6, null, null, "거래 14 요청사항");

        OrderManufacturing manufacturingOfOrder7 = OrderManufacturing.ofRequest(List.of("bending", "welding"));
        LocalDateTime createdAtOfOrder7 = LocalDateTime.parse("2023-10-24 10:20:30", formatter);
        LocalDate deliveryAtOfOrder7 = LocalDate.of(2023, 10, 31);
        FactoryGetOrderHistoryResponse orderHistory7 = new FactoryGetOrderHistoryResponse(9L, "거래 9 이름", "고객 이름 3", null, "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output9.png", Stage.QUOTE_APPROVAL, Boolean.FALSE, manufacturingOfOrder7, createdAtOfOrder7, deliveryAtOfOrder7, 900000000L, null);

        OrderManufacturing manufacturingOfOrder8 = OrderManufacturing.ofRequest(List.of("bending", "welding"));
        LocalDateTime createdAtOfOrder8 = LocalDateTime.parse("2023-10-27 10:20:30", formatter);
        FactoryGetOrderHistoryResponse orderHistory8 = new FactoryGetOrderHistoryResponse(11L, "거래 11 이름", "고객 이름 3", null, "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output11.png", Stage.NEW, Boolean.FALSE, manufacturingOfOrder8, createdAtOfOrder8, null, null, "거래 11 요청사항");

        return List.of(orderHistory8, orderHistory7, orderHistory6, orderHistory5, orderHistory4, orderHistory3, orderHistory2, orderHistory1);
    }

    public static List<FactoryGetOrderHistoryResponse> buildOfIsCompletedTrue() {
        OrderManufacturing manufacturingOfOrder1 = OrderManufacturing.ofRequest(List.of("bending"));
        LocalDateTime createdAtOfOrder1 = LocalDateTime.parse("2023-10-12 10:20:30", formatter);
        LocalDate deliveryAtOfOrder1 = LocalDate.of(2023, 10, 19);
        FactoryGetOrderHistoryResponse orderHistory1 = new FactoryGetOrderHistoryResponse(1L, "거래 1 이름", "고객 이름 1", "고객 회사 이름 1", "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output.png", Stage.COMPLETED, Boolean.FALSE, manufacturingOfOrder1, createdAtOfOrder1, deliveryAtOfOrder1, 100000000L, null);

        OrderManufacturing manufacturingOfOrder2 = OrderManufacturing.ofRequest(List.of("laser-cutting", "bending"));
        LocalDateTime createdAtOfOrder2 = LocalDateTime.parse("2023-10-14 10:20:30", formatter);
        LocalDate deliveryAtOfOrder2 = LocalDate.of(2023, 10, 20);
        FactoryGetOrderHistoryResponse orderHistory2 = new FactoryGetOrderHistoryResponse(6L, "거래 6 이름", "고객 이름 3", null, "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output6.png", Stage.COMPLETED, Boolean.TRUE, manufacturingOfOrder2, createdAtOfOrder2, deliveryAtOfOrder2, 600000000L, "거래 6 요청사항");

        return List.of(orderHistory2, orderHistory1);
    }

    public static List<FactoryGetOrderHistoryResponse> buildOfIsCompletedFalseAndDateCriterionStart() {
        OrderManufacturing manufacturingOfOrder1 = OrderManufacturing.ofRequest(List.of("bending"));
        LocalDateTime createdAtOfOrder1 = LocalDateTime.parse("2023-10-16 10:20:30", formatter);
        LocalDate deliveryAtOfOrder1 = LocalDate.of(2023, 10, 23);
        FactoryGetOrderHistoryResponse orderHistory1 = new FactoryGetOrderHistoryResponse(7L, "거래 7 이름", "고객 이름 3", null, "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output7.png", Stage.PRODUCTION_COMPLETED, Boolean.TRUE, manufacturingOfOrder1, createdAtOfOrder1, deliveryAtOfOrder1, 700000000L, "거래 7 요청사항");

        OrderManufacturing manufacturingOfOrder2 = OrderManufacturing.ofRequest(List.of("bending", "welding"));
        LocalDateTime createdAtOfOrder2 = LocalDateTime.parse("2023-10-21 10:20:30", formatter);
        LocalDate deliveryAtOfOrder2 = LocalDate.of(2023, 10, 28);
        FactoryGetOrderHistoryResponse orderHistory2 = new FactoryGetOrderHistoryResponse(12L, "거래 12 이름", "고객 이름 4", "고객 회사 이름 3", "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output12.png", Stage.NEW, Boolean.FALSE, manufacturingOfOrder2, createdAtOfOrder2, deliveryAtOfOrder2, 1200000000L, null);

        OrderManufacturing manufacturingOfOrder3 = OrderManufacturing.ofRequest(List.of("bending"));
        LocalDateTime createdAtOfOrder3 = LocalDateTime.parse("2023-10-22 10:20:30", formatter);
        LocalDate deliveryAtOfOrder3 = LocalDate.of(2023, 10, 29);
        FactoryGetOrderHistoryResponse orderHistory3 = new FactoryGetOrderHistoryResponse(8L, "거래 8 이름", "고객 이름 3", null, "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output8.png", Stage.IN_PRODUCTION, Boolean.FALSE, manufacturingOfOrder3, createdAtOfOrder3, deliveryAtOfOrder3, 800000000L, null);

        OrderManufacturing manufacturingOfOrder4 = OrderManufacturing.ofRequest(List.of("welding"));
        LocalDateTime createdAtOfOrder4 = LocalDateTime.parse("2023-10-22 14:20:30", formatter);
        LocalDate deliveryAtOfOrder4 = LocalDate.of(2023, 10, 29);
        FactoryGetOrderHistoryResponse orderHistory4 = new FactoryGetOrderHistoryResponse(13L, "거래 13 이름", "고객 이름 5", null, "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output13.png", Stage.NEW, Boolean.TRUE, manufacturingOfOrder4, createdAtOfOrder4, deliveryAtOfOrder4, 1300000000L, "거래 13 요청사항");

        OrderManufacturing manufacturingOfOrder5 = OrderManufacturing.ofRequest(List.of("bending"));
        LocalDateTime createdAtOfOrder5 = LocalDateTime.parse("2023-10-23 14:20:30", formatter);
        FactoryGetOrderHistoryResponse orderHistory5 = new FactoryGetOrderHistoryResponse(5L, "거래 5 이름", "고객 이름 1", "고객 회사 이름 1", "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output5.png", Stage.NEW, Boolean.FALSE, manufacturingOfOrder5, createdAtOfOrder5, null, null, "거래 5 요청사항");

        OrderManufacturing manufacturingOfOrder6 = OrderManufacturing.ofRequest(List.of("laser-cutting"));
        LocalDateTime createdAtOfOrder6 = LocalDateTime.parse("2023-10-23 10:20:30", formatter);
        FactoryGetOrderHistoryResponse orderHistory6 = new FactoryGetOrderHistoryResponse(14L, "거래 14 이름", "고객 이름 6", "고객 회사 이름 5", "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output14.png", Stage.NEW, Boolean.FALSE, manufacturingOfOrder6, createdAtOfOrder6, null, null, "거래 14 요청사항");

        OrderManufacturing manufacturingOfOrder7 = OrderManufacturing.ofRequest(List.of("bending", "welding"));
        LocalDateTime createdAtOfOrder7 = LocalDateTime.parse("2023-10-24 10:20:30", formatter);
        LocalDate deliveryAtOfOrder7 = LocalDate.of(2023, 10, 31);
        FactoryGetOrderHistoryResponse orderHistory7 = new FactoryGetOrderHistoryResponse(9L, "거래 9 이름", "고객 이름 3", null, "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output9.png", Stage.QUOTE_APPROVAL, Boolean.FALSE, manufacturingOfOrder7, createdAtOfOrder7, deliveryAtOfOrder7, 900000000L, null);

        return List.of(orderHistory7, orderHistory6, orderHistory5, orderHistory4, orderHistory3, orderHistory2, orderHistory1);
    }

    public static List<FactoryGetOrderHistoryResponse> buildOfIsCompletedFalseAndDateCriterionDelivery() {
        OrderManufacturing manufacturingOfOrder1 = OrderManufacturing.ofRequest(List.of("bending", "welding"));
        LocalDateTime createdAtOfOrder1 = LocalDateTime.parse("2023-10-21 10:20:30", formatter);
        LocalDate deliveryAtOfOrder1 = LocalDate.of(2023, 10, 28);
        FactoryGetOrderHistoryResponse orderHistory1 = new FactoryGetOrderHistoryResponse(12L, "거래 12 이름", "고객 이름 4", "고객 회사 이름 3", "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output12.png", Stage.NEW, Boolean.FALSE, manufacturingOfOrder1, createdAtOfOrder1, deliveryAtOfOrder1, 1200000000L, null);

        OrderManufacturing manufacturingOfOrder2 = OrderManufacturing.ofRequest(List.of("bending"));
        LocalDateTime createdAtOfOrder2 = LocalDateTime.parse("2023-10-22 10:20:30", formatter);
        LocalDate deliveryAtOfOrder2 = LocalDate.of(2023, 10, 29);
        FactoryGetOrderHistoryResponse orderHistory2 = new FactoryGetOrderHistoryResponse(8L, "거래 8 이름", "고객 이름 3", null, "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output8.png", Stage.IN_PRODUCTION, Boolean.FALSE, manufacturingOfOrder2, createdAtOfOrder2, deliveryAtOfOrder2, 800000000L, null);

        OrderManufacturing manufacturingOfOrder3 = OrderManufacturing.ofRequest(List.of("welding"));
        LocalDateTime createdAtOfOrder3 = LocalDateTime.parse("2023-10-22 14:20:30", formatter);
        LocalDate deliveryAtOfOrder3 = LocalDate.of(2023, 10, 29);
        FactoryGetOrderHistoryResponse orderHistory3 = new FactoryGetOrderHistoryResponse(13L, "거래 13 이름", "고객 이름 5", null, "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output13.png", Stage.NEW, Boolean.TRUE, manufacturingOfOrder3, createdAtOfOrder3, deliveryAtOfOrder3, 1300000000L, "거래 13 요청사항");

        OrderManufacturing manufacturingOfOrder4 = OrderManufacturing.ofRequest(List.of("bending", "welding"));
        LocalDateTime createdAtOfOrder4 = LocalDateTime.parse("2023-10-24 10:20:30", formatter);
        LocalDate deliveryAtOfOrder4 = LocalDate.of(2023, 10, 31);
        FactoryGetOrderHistoryResponse orderHistory4 = new FactoryGetOrderHistoryResponse(9L, "거래 9 이름", "고객 이름 3", null, "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output9.png", Stage.QUOTE_APPROVAL, Boolean.FALSE, manufacturingOfOrder4, createdAtOfOrder4, deliveryAtOfOrder4, 900000000L, null);

        OrderManufacturing manufacturingOfOrder5 = OrderManufacturing.ofRequest(List.of("laser-cutting", "bending", "welding"));
        LocalDateTime createdAtOfOrder5 = LocalDateTime.parse("2023-10-25 10:20:30", formatter);
        LocalDate deliveryAtOfOrder5 = LocalDate.of(2023, 11, 1);
        FactoryGetOrderHistoryResponse orderHistory5 = new FactoryGetOrderHistoryResponse(10L, "거래 10 이름", "고객 이름 3", null, "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output10.png", Stage.NEW, Boolean.TRUE, manufacturingOfOrder5, createdAtOfOrder5, deliveryAtOfOrder5, 1000000000L, "거래 10 요청사항");

        return List.of(orderHistory5, orderHistory4, orderHistory3, orderHistory2, orderHistory1);
    }

    public static List<FactoryGetOrderHistoryResponse> buildOfIsCompletedFalseAndQuery() {
        OrderManufacturing manufacturingOfOrder1 = OrderManufacturing.ofRequest(List.of("laser-cutting", "bending", "welding"));
        LocalDateTime createdAtOfOrder1 = LocalDateTime.parse("2023-10-15 10:20:30", formatter);
        LocalDate deliveryAtOfOrder1 = LocalDate.of(2023, 10, 22);
        FactoryGetOrderHistoryResponse orderHistory1 = new FactoryGetOrderHistoryResponse(4L, "거래 4 이름", "고객 이름 1", "고객 회사 이름 1", "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output4.png", Stage.QUOTE_APPROVAL, Boolean.FALSE, manufacturingOfOrder1, createdAtOfOrder1, deliveryAtOfOrder1, 4000000000L, "거래 4 요청사항");

        return List.of(orderHistory1);
    }

}
