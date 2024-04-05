package com.laser.ordermanage.common.cloud.aws;

import com.laser.ordermanage.common.exception.CommonErrorCode;
import com.laser.ordermanage.common.exception.CustomCommonException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.UUID;

@Service
public class S3Service {

    private final S3Client s3Client;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public String upload(String folder, MultipartFile multipartFile, String fileName) {
        String key = folder + "/" + UUID.randomUUID() + "-" + fileName;
        try {
            RequestBody requestBody = RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize());
            return putObject(requestBody, key);
        } catch (IOException e) {
            throw new CustomCommonException(CommonErrorCode.UNABLE_TO_AWS_S3_UPLOAD);
        }
    }

    public String upload(String folder, File file, String fileName) {
        String key = folder + "/" + UUID.randomUUID() + "-" + fileName;
        try {
            RequestBody requestBody = RequestBody.fromFile(file);
            String fileUrl = putObject(requestBody, key);
            return fileUrl;
        } catch (IOException e) {
            throw new CustomCommonException(CommonErrorCode.UNABLE_TO_AWS_S3_UPLOAD);
        }
    }

    private String putObject(RequestBody requestBody, String key) throws IOException {
        PutObjectRequest objectRequest = createPutObjectRequest(key);
        s3Client.putObject(objectRequest, requestBody);

        return findUploadKeyUrl(key).toString();
    }

    private PutObjectRequest createPutObjectRequest(String key) {
        return PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
    }


    private URL findUploadKeyUrl(String key) {
        GetUrlRequest getUrlRequest = GetUrlRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        return s3Client.utilities().getUrl(getUrlRequest);
    }
}
