package com.laser.ordermanage.order.unit.domain.type;

import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.order.domain.type.DrawingFileType;
import com.laser.ordermanage.order.exception.OrderErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class DrawingFileTypeUnitTest {

    @Test
    public void ofExtension() {
        // given
        final DrawingFileType expectedFileType = DrawingFileType.DWG;

        // when
        final DrawingFileType actualFileType = DrawingFileType.ofExtension(expectedFileType.getExtension());

        // then
        Assertions.assertThat(actualFileType).isEqualTo(expectedFileType);
    }

    @Test
    public void ofExtension_실패_UNSUPPORTED_DRAWING_FILE_EXTENSION() {
        // given
        final String invalidExtension = "invalid-extension";

        // when & then
        Assertions.assertThatThrownBy(() -> DrawingFileType.ofExtension(invalidExtension))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(OrderErrorCode.UNSUPPORTED_DRAWING_FILE_EXTENSION.getMessage());
    }
}
