package com.laser.ordermanage.customer.dto.response;

import com.laser.ordermanage.order.domain.OrderManufacturing;
import com.laser.ordermanage.order.domain.type.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CustomerGetOrderHistoryResponseBuilder {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static List<CustomerGetOrderHistoryResponse> buildListOfCustomer1() {
        OrderManufacturing manufacturingOfOrder1 = OrderManufacturing.ofRequest(List.of("bending"));
        LocalDateTime createdAtOfOrder1 = LocalDateTime.parse("2023-10-12 10:20:30", formatter);
        LocalDate deliveryAtOfOrder1 = LocalDate.of(2023, 10, 19);
        CustomerGetOrderHistoryResponse orderHistory1 = new CustomerGetOrderHistoryResponse(1L, "거래 1 이름", "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output.png", Stage.COMPLETED, Boolean.FALSE, manufacturingOfOrder1, createdAtOfOrder1, deliveryAtOfOrder1, 100000000L, null);

        OrderManufacturing manufacturingOfOrder2 = OrderManufacturing.ofRequest(List.of("laser-cutting", "bending"));
        LocalDateTime createdAtOfOrder2 = LocalDateTime.parse("2023-10-13 10:20:30", formatter);
        LocalDate deliveryAtOfOrder2 = LocalDate.of(2023, 10, 20);
        CustomerGetOrderHistoryResponse orderHistory2 = new CustomerGetOrderHistoryResponse(2L, "거래 2 이름", "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output2.png", Stage.PRODUCTION_COMPLETED, Boolean.FALSE, manufacturingOfOrder2, createdAtOfOrder2, deliveryAtOfOrder2, 200000000L, "거래 2 요청사항");

        OrderManufacturing manufacturingOfOrder3 = OrderManufacturing.ofRequest(List.of("bending", "welding"));
        LocalDateTime createdAtOfOrder3 = LocalDateTime.parse("2023-10-14 10:20:30", formatter);
        LocalDate deliveryAtOfOrder3 = LocalDate.of(2023, 10, 21);
        CustomerGetOrderHistoryResponse orderHistory3 = new CustomerGetOrderHistoryResponse(3L, "거래 3 이름", "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output3.png", Stage.IN_PRODUCTION, Boolean.TRUE, manufacturingOfOrder3, createdAtOfOrder3, deliveryAtOfOrder3, 300000L, null);

        OrderManufacturing manufacturingOfOrder4 = OrderManufacturing.ofRequest(List.of("laser-cutting", "bending", "welding"));
        LocalDateTime createdAtOfOrder4 = LocalDateTime.parse("2023-10-15 10:20:30", formatter);
        LocalDate deliveryAtOfOrder4 = LocalDate.of(2023, 10, 22);
        CustomerGetOrderHistoryResponse orderHistory4 = new CustomerGetOrderHistoryResponse(4L, "거래 4 이름", "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output4.png", Stage.QUOTE_APPROVAL, Boolean.FALSE, manufacturingOfOrder4, createdAtOfOrder4, deliveryAtOfOrder4, 4000000000L, "거래 4 요청사항");

        OrderManufacturing manufacturingOfOrder5 = OrderManufacturing.ofRequest(List.of("bending"));
        LocalDateTime createdAtOfOrder5 = LocalDateTime.parse("2023-10-23 14:20:30", formatter);
        CustomerGetOrderHistoryResponse orderHistory5 = new CustomerGetOrderHistoryResponse(5L, "거래 5 이름", "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output5.png", Stage.NEW, Boolean.FALSE, manufacturingOfOrder5, createdAtOfOrder5, null, null, "거래 5 요청사항");

        return List.of(orderHistory5, orderHistory4, orderHistory3, orderHistory2, orderHistory1);
    }

    public static List<CustomerGetOrderHistoryResponse> buildListOfCustomer1AndStageIsCompleted() {
        OrderManufacturing manufacturingOfOrder1 = OrderManufacturing.ofRequest(List.of("bending"));
        LocalDateTime createdAtOfOrder1 = LocalDateTime.parse("2023-10-12 10:20:30", formatter);
        LocalDate deliveryAtOfOrder1 = LocalDate.of(2023, 10, 19);
        CustomerGetOrderHistoryResponse orderHistory1 = new CustomerGetOrderHistoryResponse(1L, "거래 1 이름", "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output.png", Stage.COMPLETED, Boolean.FALSE, manufacturingOfOrder1, createdAtOfOrder1, deliveryAtOfOrder1, 100000000L, null);

        return List.of(orderHistory1);
    }

    public static List<CustomerGetOrderHistoryResponse> buildListOfCustomer1AndManufacturingIsLaserCutting() {
        OrderManufacturing manufacturingOfOrder1 = OrderManufacturing.ofRequest(List.of("laser-cutting", "bending"));
        LocalDateTime createdAtOfOrder1 = LocalDateTime.parse("2023-10-13 10:20:30", formatter);
        LocalDate deliveryAtOfOrder1 = LocalDate.of(2023, 10, 20);
        CustomerGetOrderHistoryResponse orderHistory1 = new CustomerGetOrderHistoryResponse(2L, "거래 2 이름", "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output2.png", Stage.PRODUCTION_COMPLETED, Boolean.FALSE, manufacturingOfOrder1, createdAtOfOrder1, deliveryAtOfOrder1, 200000000L, "거래 2 요청사항");

        OrderManufacturing manufacturingOfOrder2 = OrderManufacturing.ofRequest(List.of("laser-cutting", "bending", "welding"));
        LocalDateTime createdAtOfOrder2 = LocalDateTime.parse("2023-10-15 10:20:30", formatter);
        LocalDate deliveryAtOfOrder2 = LocalDate.of(2023, 10, 22);
        CustomerGetOrderHistoryResponse orderHistory2 = new CustomerGetOrderHistoryResponse(4L, "거래 4 이름", "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output4.png", Stage.QUOTE_APPROVAL, Boolean.FALSE, manufacturingOfOrder2, createdAtOfOrder2, deliveryAtOfOrder2, 4000000000L, "거래 4 요청사항");

        return List.of(orderHistory2, orderHistory1);
    }

    public static List<CustomerGetOrderHistoryResponse> buildListOfCustomer1AndManufacturingIsWelding() {
        OrderManufacturing manufacturingOfOrder3 = OrderManufacturing.ofRequest(List.of("bending", "welding"));
        LocalDateTime createdAtOfOrder3 = LocalDateTime.parse("2023-10-14 10:20:30", formatter);
        LocalDate deliveryAtOfOrder3 = LocalDate.of(2023, 10, 21);
        CustomerGetOrderHistoryResponse orderHistory3 = new CustomerGetOrderHistoryResponse(3L, "거래 3 이름", "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output3.png", Stage.IN_PRODUCTION, Boolean.TRUE, manufacturingOfOrder3, createdAtOfOrder3, deliveryAtOfOrder3, 300000L, null);

        OrderManufacturing manufacturingOfOrder4 = OrderManufacturing.ofRequest(List.of("laser-cutting", "bending", "welding"));
        LocalDateTime createdAtOfOrder4 = LocalDateTime.parse("2023-10-15 10:20:30", formatter);
        LocalDate deliveryAtOfOrder4 = LocalDate.of(2023, 10, 22);
        CustomerGetOrderHistoryResponse orderHistory4 = new CustomerGetOrderHistoryResponse(4L, "거래 4 이름", "https://ordermanage.s3.ap-northeast-2.amazonaws.com/output4.png", Stage.QUOTE_APPROVAL, Boolean.FALSE, manufacturingOfOrder4, createdAtOfOrder4, deliveryAtOfOrder4, 4000000000L, "거래 4 요청사항");

        return List.of(orderHistory4, orderHistory3);
    }
}
