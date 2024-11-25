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
public enum QuotationFileType implements FileType {
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

    private static final Map<String, QuotationFileType> extensionMap =
            Collections.unmodifiableMap(Stream.of(values())
                    .collect(Collectors.toMap(QuotationFileType::getExtension, Function.identity())));

    public static QuotationFileType ofExtension(String request) {
        return Optional.ofNullable(extensionMap.get(request)).orElseThrow(() -> new CustomCommonException(OrderErrorCode.UNSUPPORTED_QUOTATION_FILE_EXTENSION));
    }

    public static final String FOLDER_NAME = "quotation";

    @Override
    public String getFolderName() {
        return FOLDER_NAME;
    }
}
