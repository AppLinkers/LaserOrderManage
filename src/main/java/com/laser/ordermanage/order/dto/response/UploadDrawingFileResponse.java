package com.laser.ordermanage.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UploadDrawingFileResponse {

    private final String thumbnailUrl;

    private final String fileName;

    private final Long fileSize;

    private final String fileType;

    private final String fileUrl;

}
