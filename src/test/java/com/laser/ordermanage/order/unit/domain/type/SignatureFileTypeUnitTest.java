package com.laser.ordermanage.order.unit.domain.type;

import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.order.domain.type.SignatureFileType;
import com.laser.ordermanage.order.exception.OrderErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class SignatureFileTypeUnitTest {

    @Test
    public void ofExtension() {
        // given
        final SignatureFileType expectedFileType = SignatureFileType.PNG;

        // when
        final SignatureFileType actualFileType = SignatureFileType.ofExtension(expectedFileType.getExtension());

        // then
        Assertions.assertThat(actualFileType).isEqualTo(expectedFileType);
    }

    @Test
    public void ofExtension_실패_UNSUPPORTED_SIGNATURE_FILE_EXTENSION() {
        // given
        final String invalidExtension = "invalid-extension";

        // when & then
        Assertions.assertThatThrownBy(() -> SignatureFileType.ofExtension(invalidExtension))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(OrderErrorCode.UNSUPPORTED_SIGNATURE_FILE_EXTENSION.getMessage());
    }
}
