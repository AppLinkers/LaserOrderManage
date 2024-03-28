package com.laser.ordermanage.order.service;

import com.laser.ordermanage.common.cloud.aws.S3Service;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.util.CADUtil;
import com.laser.ordermanage.common.util.FileUtil;
import com.laser.ordermanage.common.util.ImageUtil;
import com.laser.ordermanage.common.util.PDFUtil;
import com.laser.ordermanage.order.domain.Drawing;
import com.laser.ordermanage.order.domain.type.DrawingFileType;
import com.laser.ordermanage.order.dto.response.UploadDrawingFileResponse;
import com.laser.ordermanage.order.exception.OrderErrorCode;
import com.laser.ordermanage.order.repository.DrawingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RequiredArgsConstructor
@Service
public class DrawingService {

    @Value("${path.temp-folder}")
    private String tempFolderPath;

    private final S3Service s3Service;

    private final DrawingRepository drawingRepository;

    @Transactional(readOnly = true)
    public Drawing getDrawingById(Long drawingId) {
        return drawingRepository.findFirstById(drawingId).orElseThrow(() -> new CustomCommonException(OrderErrorCode.NOT_FOUND_DRAWING));
    }

    @Transactional(readOnly = true)
    public Integer countDrawingByOrderId(Long orderId) {
        return drawingRepository.countByOrderId(orderId);
    }

    public File extractThumbnail(MultipartFile multipartFile, DrawingFileType fileType) {
        return switch (fileType) {
            case DWG, DXF -> CADUtil.extractThumbnail(multipartFile, tempFolderPath);
            case PDF -> PDFUtil.extractThumbnail(multipartFile, tempFolderPath);
            // PNG, JPG, JPEG
            default -> ImageUtil.extractThumbnail(multipartFile, tempFolderPath);
        };
    }

    public String uploadThumbnailFile(File thumbnailFile) {
        return s3Service.upload("drawing-thumbnail", thumbnailFile, "drawing-thumbnail.png");
    }

    public UploadDrawingFileResponse uploadDrawingFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        Long fileSize = file.getSize();
        DrawingFileType fileType = DrawingFileType.ofExtension(FileUtil.getExtension(file));

        String fileUrl = s3Service.upload("drawing", file, "drawing." + fileType.getExtension());

        // 썸네일 추출
        File thumbnailFile = this.extractThumbnail(file, fileType);

        // 썸네일 파일 업로드
        String thumbnailFileUrl = this.uploadThumbnailFile(thumbnailFile);

        // 썸네일 파일 삭제
        thumbnailFile.delete();

        UploadDrawingFileResponse uploadDrawingFileResponse = UploadDrawingFileResponse.builder()
                .thumbnailUrl(thumbnailFileUrl)
                .fileName(fileName)
                .fileType(fileType.getExtension())
                .fileUrl(fileUrl)
                .fileSize(fileSize)
                .build();

        return uploadDrawingFileResponse;
    }
}
