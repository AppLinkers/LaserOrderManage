package com.laser.ordermanage.order.service;

import com.laser.ordermanage.common.cloud.aws.S3Service;
import com.laser.ordermanage.common.util.CADUtil;
import com.laser.ordermanage.customer.dto.response.CustomerGetDrawingResponse;
import com.laser.ordermanage.order.repository.DrawingRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DrawingService {

    @Value("${path.temp-folder}")
    private String tempFolderPath;

    private final S3Service s3Service;

    private final DrawingRepositoryCustom drawingRepository;

    @Transactional(readOnly = true)
    public List<CustomerGetDrawingResponse> getDrawingByCustomerAndOrder(String userName, Long orderId) {
        return drawingRepository.findByCustomerAndOrder(userName, orderId);
    }

    public String extractThumbnail(MultipartFile multipartFile) {
        return CADUtil.extractThumbnail(multipartFile, tempFolderPath);
    }

    public String uploadDrawingFile(MultipartFile multipartFile) {
        return s3Service.upload(multipartFile);
    }

    public String uploadThumbnailFile(String tempThumbnailFileUrl) {
        return s3Service.upload(tempThumbnailFileUrl);
    }
}
