package com.laser.ordermanage.order.api;

import com.laser.ordermanage.common.validation.constraints.ValidFile;
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
        UploadDrawingFileResponse uploadDrawingFileResponse = drawingService.uploadDrawingFile(file);

        return ResponseEntity.ok(uploadDrawingFileResponse);
    }
}
