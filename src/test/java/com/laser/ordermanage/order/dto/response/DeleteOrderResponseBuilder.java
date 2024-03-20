package com.laser.ordermanage.order.dto.response;

public class DeleteOrderResponseBuilder {
    public static DeleteOrderResponse build() {
        return DeleteOrderResponse.builder()
                .name("삭제 거래 이름")
                .customerUserEmail("user@gmail.com")
                .build();
    }
}
