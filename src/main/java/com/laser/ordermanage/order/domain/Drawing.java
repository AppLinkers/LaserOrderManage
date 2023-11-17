package com.laser.ordermanage.order.domain;

import com.laser.ordermanage.order.domain.type.DrawingFileType;
import com.laser.ordermanage.order.domain.type.Ingredient;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Drawing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    private Order order;

    @NotNull
    private String fileName;

    @NotNull
    private Long fileSize;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private DrawingFileType fileType;

    @NotNull
    private String fileUrl;

    @NotNull
    private String thumbnailUrl;

    private Integer count;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private Ingredient ingredient;

    @Builder
    public Drawing(Order order, String fileName, Long fileSize, DrawingFileType fileType, String fileUrl, String thumbnailUrl, Integer count, String ingredient) {
        this.order = order;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileType = fileType;
        this.fileUrl = fileUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.count = count;
        this.ingredient = Ingredient.valueOf(ingredient);
    }
}