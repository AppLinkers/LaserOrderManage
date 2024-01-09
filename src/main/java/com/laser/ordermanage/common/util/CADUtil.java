package com.laser.ordermanage.common.util;

import com.aspose.cad.Image;
import com.aspose.cad.imageoptions.CadRasterizationOptions;
import com.aspose.cad.imageoptions.PngOptions;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.exception.CommonErrorCode;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class CADUtil {

    private CADUtil() {
    }

    public static String extractThumbnail(MultipartFile multipartFile, String tempFolderPath) {
        try {
            Image image = Image.load(multipartFile.getInputStream());

            CadRasterizationOptions rasterizationOptions = new CadRasterizationOptions();

            rasterizationOptions.setPageWidth(500);
            rasterizationOptions.setPageHeight(500);

            PngOptions pngOptions = new PngOptions();

            pngOptions.setVectorRasterizationOptions(rasterizationOptions);

            String filePath = tempFolderPath + FileUtil.getFileNameWithoutExtension(multipartFile) + "-thumbnail.png";

            image.save(filePath, pngOptions);

            return filePath;
        } catch (IOException e) {
            throw new CustomCommonException(CommonErrorCode.UNABLE_TO_EXTRACT_THUMBNAIL);
        }
    }
}
