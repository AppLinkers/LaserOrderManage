package com.laser.ordermanage.common.util;

import com.laser.ordermanage.common.exception.CommonErrorCode;
import com.laser.ordermanage.common.exception.CustomCommonException;
import org.imgscalr.Scalr;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtil {

    private ImageUtil() {

    }

    public static String extractThumbnail(MultipartFile multipartFile, String tempFolderPath) {
        try {
            // MultipartFile to BufferedImage
            BufferedImage bufferedImage = ImageIO.read(multipartFile.getInputStream());

            // resize image
            BufferedImage resizedImage = Scalr.resize(bufferedImage, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_EXACT, 500, 500, Scalr.OP_ANTIALIAS);

            String filePath = tempFolderPath + FileUtil.getFileNameWithoutExtension(multipartFile) + "-thumbnail.png";

            ImageIO.write(resizedImage, "png", new File(filePath));

            return filePath;
        } catch (IOException e) {
            throw new CustomCommonException(CommonErrorCode.UNABLE_TO_EXTRACT_THUMBNAIL);
        }
    }
}
