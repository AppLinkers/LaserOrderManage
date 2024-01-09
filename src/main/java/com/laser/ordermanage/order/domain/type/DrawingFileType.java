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
public enum DrawingFileType {
    DWG("dwg"),
    DXF("dxf"),
    PDF("pdf"),
    JPG("jpg"),
    JPEG("jpeg"),
    PNG("png");

    @Getter
    private final String extension;

    private static final Map<String, DrawingFileType> extensionMap =
            Collections.unmodifiableMap(Stream.of(values())
                    .collect(Collectors.toMap(DrawingFileType::getExtension, Function.identity())));

    public static DrawingFileType ofExtension(String request) {
        return Optional.ofNullable(extensionMap.get(request)).orElseThrow(() -> new CustomCommonException(OrderErrorCode.NOT_SUPPORTED_DRAWING_FILE_EXTENSION));
    }
}
