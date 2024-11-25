package com.laser.ordermanage.order.dto.response;

import com.laser.ordermanage.common.entity.embedded.FileEntity;
import com.laser.ordermanage.order.domain.type.DrawingFileType;
import lombok.Builder;

@Builder
public record UploadDrawingFileResponse(
        String thumbnailUrl,
        String fileName,
        Long fileSize,
        String fileType,
        String fileUrl
) {
    public static UploadDrawingFileResponse fromDTO(FileEntity<DrawingFileType> drawingFile, String thumbnailUrl) {
        return UploadDrawingFileResponse.builder()
                .thumbnailUrl(thumbnailUrl)
                .fileName(drawingFile.getName())
                .fileType(drawingFile.getType().getExtension())
                .fileUrl(drawingFile.getUrl())
                .fileSize(drawingFile.getSize())
                .build();
    }

}
