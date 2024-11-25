package com.laser.ordermanage.order.service;

import com.laser.ordermanage.common.component.FileComponent;
import com.laser.ordermanage.common.entity.embedded.FileEntity;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Slf4j
@RequiredArgsConstructor
@Service
public class DrawingService {

    @Value("${path.temp-folder}")
    private String tempFolderPath;

    private final FileComponent fileComponent;

    private final DrawingRepository drawingRepository;

    @Transactional(readOnly = true)
    public Drawing getDrawingById(Long drawingId) {
        return drawingRepository.findFirstById(drawingId).orElseThrow(() -> new CustomCommonException(OrderErrorCode.NOT_FOUND_DRAWING));
    }

    @Transactional(readOnly = true)
    public Integer countDrawingByOrderId(Long orderId) {
        return drawingRepository.countByOrderId(orderId);
    }

    public File extractThumbnail(MultipartFile file) {
        DrawingFileType fileType = DrawingFileType.ofExtension(FileUtil.getExtension(file));
        return switch (fileType) {
            case DWG, DXF -> CADUtil.extractThumbnail(file, tempFolderPath);
            case PDF -> PDFUtil.extractThumbnail(file, tempFolderPath);
            // PNG, JPG, JPEG
            default -> ImageUtil.extractThumbnail(file, tempFolderPath);
        };
    }

    public UploadDrawingFileResponse uploadDrawingFile(MultipartFile file) {
        FileEntity<DrawingFileType> drawingFile = fileComponent.uploadFile(file, DrawingFileType::ofExtension);

        // 썸네일 파일 추출
        File thumbnailFile = extractThumbnail(file);
        // 썸네일 파일 업로드
        String thumbnailUrl = fileComponent.uploadFile("drawing-thumbnail", thumbnailFile, "drawing-thumbnail.png");
        thumbnailFile.delete();

        return UploadDrawingFileResponse.fromDTO(drawingFile, thumbnailUrl);
    }
}
