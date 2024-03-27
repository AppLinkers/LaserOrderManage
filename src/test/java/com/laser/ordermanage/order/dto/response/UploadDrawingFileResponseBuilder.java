package com.laser.ordermanage.order.dto.response;

import com.laser.ordermanage.order.domain.type.DrawingFileType;

public class UploadDrawingFileResponseBuilder {
    public static UploadDrawingFileResponse buildOfDWGDrawing() {
        return UploadDrawingFileResponse.builder()
                .thumbnailUrl("thumbnail-url.dwg")
                .fileName("drawing.dwg")
                .fileSize(140801L)
                .fileType(DrawingFileType.DWG.getExtension())
                .fileUrl("drawing-file-url.dwg")
                .build();
    }

    public static UploadDrawingFileResponse buildOfPDFDrawing() {
        return UploadDrawingFileResponse.builder()
                .thumbnailUrl("thumbnail-url.pdf")
                .fileName("drawing.pdf")
                .fileSize(160737L)
                .fileType(DrawingFileType.PDF.getExtension())
                .fileUrl("drawing-file-url.pdf")
                .build();
    }

    public static UploadDrawingFileResponse buildOfPNGDrawing() {
        return UploadDrawingFileResponse.builder()
                .thumbnailUrl("thumbnail-url.png")
                .fileName("drawing.png")
                .fileSize(95120L)
                .fileType(DrawingFileType.PNG.getExtension())
                .fileUrl("drawing-file-url.png")
                .build();
    }
}
