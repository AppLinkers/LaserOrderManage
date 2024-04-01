package com.laser.ordermanage.order.unit.domain.type;

import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.order.domain.type.QuotationFileType;
import com.laser.ordermanage.order.exception.OrderErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class QuotationFileTypeUnitTest {

    @Test
    public void ofExtension() {
        // given
        final QuotationFileType expectedFileType = QuotationFileType.PDF;

        // when
        final QuotationFileType actualFileType = QuotationFileType.ofExtension(expectedFileType.getExtension());

        // then
        Assertions.assertThat(actualFileType).isEqualTo(expectedFileType);
    }

    @Test
    public void ofExtension_실패_UNSUPPORTED_QUOTATION_FILE_EXTENSION() {
        // given
        final String invalidExtension = "invalid-extension";

        // when & then
        Assertions.assertThatThrownBy(() -> QuotationFileType.ofExtension(invalidExtension))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(OrderErrorCode.UNSUPPORTED_QUOTATION_FILE_EXTENSION.getMessage());
    }
}
