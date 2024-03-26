package com.laser.ordermanage.order.unit.domain;

import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.order.domain.OrderPostProcessing;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class OrderPostProcessingUnitTest {
    @Test
    public void toValueList() {
        // given
        final List<String> expectedValueList = List.of("painting", "plating");
        final OrderPostProcessing orderPostProcessing = OrderPostProcessing.ofRequest(expectedValueList);

        // when
        final List<String> actualValueList = orderPostProcessing.toValueList();

        // then
        Assertions.assertThat(actualValueList).containsAll(expectedValueList);
    }

    @Test
    public void ofRequest() {
        // given
        final List<String> requestList = List.of("painting", "plating");

        // when
        final OrderPostProcessing orderPostProcessing = OrderPostProcessing.ofRequest(requestList);

        // then
    Assertions.assertThat(orderPostProcessing.getId()).isNull();
        Assertions.assertThat(orderPostProcessing.getIsPainting()).isTrue();
        Assertions.assertThat(orderPostProcessing.getIsPlating()).isTrue();
    }

    @Test
    public void ofRequest_invalid_request() {
        // given
        final List<String> invalidRequestList = List.of("invalid-request");

        // when & then
        Assertions.assertThatThrownBy(() -> OrderPostProcessing.ofRequest(invalidRequestList))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage("postProcessing 의 타입이 옳바르지 않습니다.");
    }
}
