package com.laser.ordermanage.order.api;

import com.laser.ordermanage.common.util.FileUtil;
import com.laser.ordermanage.order.domain.type.DrawingFileType;
import com.laser.ordermanage.order.dto.response.UploadDrawingFileResponse;
import com.laser.ordermanage.order.service.DrawingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping("/drawing")
@RestController
public class DrawingAPI {

    private final DrawingService drawingService;

    @PostMapping("")
    public ResponseEntity<?> uploadDrawingFile(@RequestBody MultipartFile file) {
        // File 확장자 확인
        DrawingFileType fileType = DrawingFileType.ofRequest(FileUtil.getExtension(file));

        String fileName = file.getOriginalFilename();
        Long fileSize = file.getSize();
        // 도면 파일 업로드
        String drawingFileUrl = drawingService.uploadDrawingFile(file);

        String thumbnailFileUrl;
        if (fileType.equals(DrawingFileType.DWG) || fileType.equals(DrawingFileType.DXF)) {
            // 썸네일 추출
            String tempThumbnailFileUrl = drawingService.extractThumbnail(file);

            // 썸네일 파일 업로드
            thumbnailFileUrl = drawingService.uploadThumbnailFile(tempThumbnailFileUrl);

        } else {
            // TODO: pdf, png, jpg, jpeg 파일 썸네일 추출 기능
            // 썸네일 파일 업로드
            thumbnailFileUrl = "https://ordermanage-drawing.s3.ap-northeast-2.amazonaws.com/ce429eb7-3319-45ba-b0b9-48bc6c77cf79_exception.png";
        }

        UploadDrawingFileResponse uploadDrawingFileResponse = UploadDrawingFileResponse.builder()
                .thumbnailImgUrl(thumbnailFileUrl)
                .fileName(fileName)
                .fileUrl(drawingFileUrl)
                .fileSize(fileSize)
                .build();

        return ResponseEntity.ok(uploadDrawingFileResponse);
    }
}
