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

import java.io.IOException;

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
    public ResponseEntity<?> uploadDrawingFile(@RequestParam @ValidFile(message = "도면 파일은 필수 입력값입니다.") MultipartFile file) throws IOException {
        // File 확장자 확인
        DrawingFileType fileType = DrawingFileType.ofExtension(FileUtil.getExtension(file));

        String fileName = file.getOriginalFilename();
        Long fileSize = file.getSize();
        // 도면 파일 업로드
        String drawingFileUrl = drawingService.uploadDrawingFile(file);

        // 썸네일 추출
        String tempThumbnailFilePath = drawingService.extractThumbnail(file, fileType);

        // 썸네일 파일 업로드
        String thumbnailUrl = drawingService.uploadThumbnailFile(tempThumbnailFilePath);

        UploadDrawingFileResponse uploadDrawingFileResponse = UploadDrawingFileResponse.builder()
                .thumbnailUrl(thumbnailUrl)
                .fileName(fileName)
                .fileType(fileType.getExtension())
                .fileUrl(drawingFileUrl)
                .fileSize(fileSize)
                .build();

        return ResponseEntity.ok(uploadDrawingFileResponse);
    }
}
