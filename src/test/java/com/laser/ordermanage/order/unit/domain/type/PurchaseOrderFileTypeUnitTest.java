package com.laser.ordermanage.order.unit.domain.type;

import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.order.domain.type.PurchaseOrderFileType;
import com.laser.ordermanage.order.exception.OrderErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class PurchaseOrderFileTypeUnitTest {

    @Test
    public void ofExtension() {
        // given
        final PurchaseOrderFileType expectedFileType = PurchaseOrderFileType.PDF;

        // when
        final PurchaseOrderFileType actualFileType = PurchaseOrderFileType.ofExtension(expectedFileType.getExtension());

        // then
        Assertions.assertThat(actualFileType).isEqualTo(expectedFileType);
    }

    @Test
    public void ofExtension_실패_UNSUPPORTED_QUOTATION_FILE_EXTENSION() {
        // given
        final String invalidExtension = "invalid-extension";

        // when & then
        Assertions.assertThatThrownBy(() -> PurchaseOrderFileType.ofExtension(invalidExtension))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(OrderErrorCode.UNSUPPORTED_QUOTATION_FILE_EXTENSION.getMessage());
    }
}
