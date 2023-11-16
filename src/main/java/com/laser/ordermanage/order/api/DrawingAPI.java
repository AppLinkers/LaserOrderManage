package com.laser.ordermanage.order.api;

import com.laser.ordermanage.common.cloud.aws.S3Service;
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

    private final S3Service s3Service;

    @PostMapping("")
    public ResponseEntity<?> uploadDrawingFile(@RequestBody MultipartFile file) {
        return ResponseEntity.ok(s3Service.upload(file));
    }
}
