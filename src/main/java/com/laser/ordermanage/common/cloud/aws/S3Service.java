package com.laser.ordermanage.common.cloud.aws;

import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Utilities;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;

@Service
public class S3Service {

    private final S3Client s3Client;
    private final S3Utilities s3Utilities;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
        this.s3Utilities = s3Client.utilities();
    }

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public String upload(MultipartFile multipartFile) {
        String key = UUID.randomUUID() + "_" + multipartFile.getOriginalFilename();
        try {
            return putObject(multipartFile, key);
        } catch (IOException e) {
            throw new CustomCommonException(ErrorCode.UNABLE_TO_AWS_S3_UPLOAD);
        }
    }

    private String putObject(MultipartFile multipartFile, String key) throws IOException {
        PutObjectRequest objectRequest = createPutObjectRequest(key);
        RequestBody requestBody = createFileRequestBody(multipartFile);
        s3Client.putObject(objectRequest, requestBody);

        return findUploadKeyUrl(key).toString();
    }

    private PutObjectRequest createPutObjectRequest(String key) {
        return PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
    }

    private RequestBody createFileRequestBody(MultipartFile multipartFile) throws IOException{
        return RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize());
    }

    private URL findUploadKeyUrl(String key) {
        GetUrlRequest getUrlRequest = GetUrlRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        return s3Utilities.getUrl(getUrlRequest);
    }
}
