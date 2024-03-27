package com.laser.ordermanage.order.unit.domain.type;

import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.order.domain.type.Stage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class StageUnitTest {

    @Test
    public void ofRequest() {
        // given
        final Stage expectedStage = Stage.NEW;

        // when
        final Stage actualStage = Stage.ofRequest(expectedStage.getRequest());

        // then
        Assertions.assertThat(actualStage).isEqualTo(expectedStage);
    }

    @Test
    public void ofRequest_실패_INVALID_PARAMETER() {
        // given
        final String invalidStage = "invalid-stage";

        // when & then
        Assertions.assertThatThrownBy(() -> Stage.ofRequest(invalidStage))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage("stage 파라미터가 올바르지 않습니다.");
    }
}
