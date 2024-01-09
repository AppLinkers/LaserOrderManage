package com.laser.ordermanage.common.util;

import com.laser.ordermanage.common.exception.CommonErrorCode;
import com.laser.ordermanage.common.exception.CustomCommonException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.imgscalr.Scalr;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class PDFUtil {

    private PDFUtil() {

    }

    public static String extractThumbnail(MultipartFile multipartFile, String tempFolderPath) {
        try {
            // 일시적인 파일 생성
            Path tempFile = Files.createTempFile("uploadedDrawing-", ".pdf");
            Files.copy(multipartFile.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);

            try (PDDocument document = Loader.loadPDF(tempFile.toFile())) {
                PDFRenderer renderer = new PDFRenderer(document);
                BufferedImage originalImage = renderer.renderImageWithDPI(0, 300);

                // 이미지 크기 조정
                BufferedImage resizedImage = Scalr.resize(originalImage, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_EXACT, 500, 500, Scalr.OP_ANTIALIAS);

                String filePath = tempFolderPath + FileUtil.getFileNameWithoutExtension(multipartFile) + "-thumbnail.png";
                ImageIO.write(resizedImage, "png", new File(filePath));

                // 임시 파일 삭제
                Files.deleteIfExists(tempFile);

                return filePath;
            }
        } catch (IOException e) {
            throw new CustomCommonException(CommonErrorCode.UNABLE_TO_EXTRACT_THUMBNAIL);
        }
    }
}
