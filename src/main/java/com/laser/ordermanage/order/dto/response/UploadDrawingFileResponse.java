package com.laser.ordermanage.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UploadDrawingFileResponse {

    private final String thumbnailImgUrl;

    private final String fileName;

    private final String fileUrl;

    private final Long fileSize;
}
