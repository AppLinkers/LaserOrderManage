package com.laser.ordermanage.order.api;

import com.laser.ordermanage.common.util.FileUtil;
import com.laser.ordermanage.common.validation.constraints.ValidFile;
import com.laser.ordermanage.order.domain.type.DrawingFileType;
import com.laser.ordermanage.order.dto.response.UploadDrawingFileResponse;
import com.laser.ordermanage.order.service.DrawingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Validated
@RequiredArgsConstructor
@RequestMapping("/drawing")
@RestController
public class DrawingAPI {

    private final DrawingService drawingService;

    /**
     * 도면 파일 업로드
     * - 파일 확장자 확인 (DWG, DXF, PDF, PNG, JPG, JPEG)
     * - 도면 파일 AWS S3 에 업로드
     * - 도면 파일의 썸네일 이미지 추출
     * - 썸네일 이미지 파일 AWS S3 에 업로드
     */
    @PostMapping("")
    public ResponseEntity<?> uploadDrawingFile(@RequestParam @ValidFile(message = "도면 파일은 필수 입력값입니다.") MultipartFile file) {
        // File 확장자 확인
        DrawingFileType fileType = DrawingFileType.ofExtension(FileUtil.getExtension(file));

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
                .fileType(fileType.getExtension())
                .fileUrl(drawingFileUrl)
                .fileSize(fileSize)
                .build();

        return ResponseEntity.ok(uploadDrawingFileResponse);
    }
}
