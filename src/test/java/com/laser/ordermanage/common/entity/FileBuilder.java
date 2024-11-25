package com.laser.ordermanage.common.entity;

import com.laser.ordermanage.common.entity.embedded.FileEntity;
import com.laser.ordermanage.order.domain.type.*;

public class FileBuilder {
    public static FileEntity<FileType> drawingFileBuild() {
        return FileEntity.<FileType>builder()
                .name("drawing.dwg")
                .size(140801L)
                .type(DrawingFileType.DWG)
                .url("drawing-file-url.dwg")
                .build();
    }

    public static FileEntity<FileType> quotationFileBuild() {
        return FileEntity.<FileType>builder()
                .name("quotation.xlsx")
                .size(306480L)
                .type(QuotationFileType.XLSX)
                .url("quotation-file-url.xlsx")
                .build();
    }

    public static FileEntity<FileType> purchaseOrderFileBuild() {
        return FileEntity.<FileType>builder()
                .name("purchase-order.png")
                .size(3608L)
                .type(PurchaseOrderFileType.PNG)
                .url("purchase-order-file-url.xlsx")
                .build();
    }

    public static FileEntity<FileType> signatureFileBuild() {
        return FileEntity.<FileType>builder()
                .name("signature.png")
                .size(12062L)
                .type(SignatureFileType.PNG)
                .url("signature-file-url.png")
                .build();
    }
}
