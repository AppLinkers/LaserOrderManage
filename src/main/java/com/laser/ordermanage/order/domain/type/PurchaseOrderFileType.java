package com.laser.ordermanage.order.domain.type;

import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.order.exception.OrderErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
public enum PurchaseOrderFileType {
    PDF("pdf"),
    HWP("hwp"),
    CELL("cell"),
    DOCX("docx"),
    DOC("doc"),
    XLSX("xlsx"),
    XLS("xls"),
    JPG("jpg"),
    JPEG("jpeg"),
    PNG("png");

    @Getter
    private final String extension;

    private static final Map<String, PurchaseOrderFileType> extensionMap =
            Collections.unmodifiableMap(Stream.of(values())
                    .collect(Collectors.toMap(PurchaseOrderFileType::getExtension, Function.identity())));

    public static PurchaseOrderFileType ofExtension(String request) {
        return Optional.ofNullable(extensionMap.get(request)).orElseThrow(() -> new CustomCommonException(OrderErrorCode.UNSUPPORTED_QUOTATION_FILE_EXTENSION));
    }
}
