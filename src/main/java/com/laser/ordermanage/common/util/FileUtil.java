package com.laser.ordermanage.common.util;

import org.springframework.web.multipart.MultipartFile;

public class FileUtil {

    private FileUtil() {
    }

    public static String getFileNameWithoutExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(0, fileName.lastIndexOf("."));
        } else {
            return fileName; // 확장자가 없는 경우 전체 이름 반환
        }
    }

    public static String getExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return ""; // 확장자가 없는 경우
        }
    }

    public static void deleteFile(String filePath) {

    }

}
