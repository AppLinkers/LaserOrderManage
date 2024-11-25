package com.laser.ordermanage.order.domain.type;

public interface FileType {
    String getExtension();
    String getFolderName();
    default String getFileName() {
        return getFolderName() + "." + getExtension();
    }
}
