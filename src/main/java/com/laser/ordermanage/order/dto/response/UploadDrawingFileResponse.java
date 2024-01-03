package com.laser.ordermanage.order.dto.response;

import lombok.Builder;

@Builder
public record UploadDrawingFileResponse(
        String thumbnailUrl,
        String fileName,
        Long fileSize,
        String fileType,
        String fileUrl
) { }
