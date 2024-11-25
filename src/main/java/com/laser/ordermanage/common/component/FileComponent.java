package com.laser.ordermanage.common.component;

import com.laser.ordermanage.common.cloud.aws.S3Service;
import com.laser.ordermanage.common.entity.embedded.FileEntity;
import com.laser.ordermanage.common.util.FileUtil;
import com.laser.ordermanage.order.domain.type.FileType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.function.Function;

@RequiredArgsConstructor
@Component
public class FileComponent {

    private final S3Service s3Service;

    public <T extends FileType> FileEntity<T> uploadFile(MultipartFile file, Function<String, T> fileTypeResolver) {
        String fileName = file.getOriginalFilename();
        Long fileSize = file.getSize();
        T fileType = fileTypeResolver.apply(FileUtil.getExtension(file));

        // 파일 업로드
        String fileUrl = s3Service.upload(fileType.getFolderName(), file, fileType.getFileName());

        // File 객체 생성
        return FileEntity.<T>builder()
                .name(fileName)
                .size(fileSize)
                .type(fileType)
                .url(fileUrl)
                .build();
    }

    public String uploadFile(String folderName, File file, String fileName) {
        return s3Service.upload(folderName, file, fileName);
    }
}
