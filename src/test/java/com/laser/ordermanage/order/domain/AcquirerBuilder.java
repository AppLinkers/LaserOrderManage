package com.laser.ordermanage.order.domain;

import com.laser.ordermanage.common.entity.embedded.File;
import com.laser.ordermanage.order.domain.type.SignatureFileType;

public class AcquirerBuilder {
    public static Acquirer build() {
        File<SignatureFileType> signatureFile = File.<SignatureFileType>builder()
                .name("signature.png")
                .size(12062L)
                .type(SignatureFileType.PNG)
                .url("https://ordermanage.s3.ap-northeast-2.amazonaws.com/acquirer-signature/signature.png")
                .build();

        return Acquirer.builder()
                .name("인수자 1 이름")
                .phone("01012121212")
                .signatureFile(signatureFile)
                .build();
    }
}
