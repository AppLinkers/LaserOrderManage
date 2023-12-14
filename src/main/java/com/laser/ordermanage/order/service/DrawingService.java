package com.laser.ordermanage.order.service;

import com.laser.ordermanage.common.cloud.aws.S3Service;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.exception.ErrorCode;
import com.laser.ordermanage.common.util.CADUtil;
import com.laser.ordermanage.order.domain.Drawing;
import com.laser.ordermanage.order.domain.Order;
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
        return drawingRepository.findFirstByOrderAndId(order, drawingId).orElseThrow(() -> new CustomCommonException(ErrorCode.NOT_FOUND_ENTITY, "drawing"));
    }

    @Transactional(readOnly = true)
    public Integer countDrawingByOrder(Order order) {
        return drawingRepository.countByOrder(order);
    }

    public String extractThumbnail(MultipartFile multipartFile) {
        return CADUtil.extractThumbnail(multipartFile, tempFolderPath);
    }

    public String uploadDrawingFile(MultipartFile multipartFile) {
        return s3Service.upload("drawing", multipartFile);
    }

    public String uploadThumbnailFile(String tempThumbnailFileUrl) {
        return s3Service.upload(tempThumbnailFileUrl);
    }
}
