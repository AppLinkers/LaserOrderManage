package com.laser.ordermanage.order.unit.domain;

import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.order.domain.OrderManufacturing;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class OrderManufacturingUnitTest {
    @Test
    public void toValueList() {
        // given
        final List<String> expectedValueList = List.of("laser-cutting", "bending", "welding");
        final OrderManufacturing orderManufacturing = OrderManufacturing.ofRequest(expectedValueList);

        // when
        final List<String> actualValueList = orderManufacturing.toValueList();

        // then
        Assertions.assertThat(actualValueList).containsAll(expectedValueList);
    }

    @Test
    public void ofRequest() {
        // given
        final List<String> requestList = List.of("laser-cutting", "bending", "welding");

        // when
        final OrderManufacturing orderManufacturing = OrderManufacturing.ofRequest(requestList);

        // then
        Assertions.assertThat(orderManufacturing.getId()).isNull();
        Assertions.assertThat(orderManufacturing.getIsLaserCutting()).isTrue();
        Assertions.assertThat(orderManufacturing.getIsBending()).isTrue();
        Assertions.assertThat(orderManufacturing.getIsWelding()).isTrue();
    }

    @Test
    public void ofRequest_실패_INVALID_REQUEST_BODY_FIELDS() {
        // given
        final List<String> invalidRequestList = List.of("invalid-request");

        // when & then
        Assertions.assertThatThrownBy(() -> OrderManufacturing.ofRequest(invalidRequestList))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage("manufacturing 의 타입이 옳바르지 않습니다.");
    }
}
