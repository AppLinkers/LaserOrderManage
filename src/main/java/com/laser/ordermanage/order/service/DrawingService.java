package com.laser.ordermanage.order.service;

import com.laser.ordermanage.common.cloud.aws.S3Service;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.util.CADUtil;
import com.laser.ordermanage.common.util.ImageUtil;
import com.laser.ordermanage.order.domain.Drawing;
import com.laser.ordermanage.order.domain.Order;
import com.laser.ordermanage.order.domain.type.DrawingFileType;
import com.laser.ordermanage.order.exception.OrderErrorCode;
import com.laser.ordermanage.order.repository.DrawingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class DrawingService {

    @Value("${path.temp-folder}")
    private String tempFolderPath;

    private final S3Service s3Service;

    private final DrawingRepository drawingRepository;

    @Transactional(readOnly = true)
    public Drawing getDrawingByOrderAndId(Order order, Long drawingId) {
        return drawingRepository.findFirstByOrderAndId(order, drawingId).orElseThrow(() -> new CustomCommonException(OrderErrorCode.NOT_FOUND_DRAWING));
    }

    @Transactional(readOnly = true)
    public Integer countDrawingByOrder(Order order) {
        return drawingRepository.countByOrder(order);
    }

    public String extractThumbnail(MultipartFile multipartFile, DrawingFileType fileType) {
        final String exceptionImageUrl = "https://ordermanage-drawing.s3.ap-northeast-2.amazonaws.com/ce429eb7-3319-45ba-b0b9-48bc6c77cf79_exception.png";

        return switch (fileType) {
            case DWG, DXF -> CADUtil.extractThumbnail(multipartFile, tempFolderPath);
            case PDF -> exceptionImageUrl;
            // PNG, JPG, JPEG
            default -> ImageUtil.extractThumbnail(multipartFile, tempFolderPath);
        };
    }

    public String uploadDrawingFile(MultipartFile multipartFile) {
        return s3Service.upload("drawing", multipartFile);
    }

    public String uploadThumbnailFile(String tempThumbnailFilePath) {
        return s3Service.upload("drawing-thumbnail", tempThumbnailFilePath);
    }
}
