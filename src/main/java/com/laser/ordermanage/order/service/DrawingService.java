package com.laser.ordermanage.order.service;

import com.laser.ordermanage.common.cloud.aws.S3Service;
import com.laser.ordermanage.common.util.CADUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class DrawingService {

    @Value("${path.temp-folder}")
    private String tempFolderPath;

    private final S3Service s3Service;

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
