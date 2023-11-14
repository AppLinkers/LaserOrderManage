package com.laser.ordermanage.order.domain.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum FileType {
    DWG("dwg"),
    DXF("dxf"),
    PDF("pdf"),
    JPG("jpg"),
    PNG("png");

    @Getter
    private final String extension;
}
