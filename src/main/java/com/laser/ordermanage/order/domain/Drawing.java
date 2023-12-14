package com.laser.ordermanage.order.domain;

import com.laser.ordermanage.customer.dto.request.CustomerUpdateDrawingRequest;
import com.laser.ordermanage.order.domain.type.DrawingFileType;
import com.laser.ordermanage.order.domain.type.Ingredient;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "drawing")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Drawing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, updatable = false)
    private Order order;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "file_type", nullable = false)
    private DrawingFileType fileType;

    @Column(name = "file_url", nullable = false)
    private String fileUrl;

    @Column(name = "thumbnail_url", nullable = false)
    private String thumbnailUrl;

    @Column(name = "count")
    private Integer count;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "ingredient", nullable = false)
    private Ingredient ingredient;

    @Column(name = "thickness", nullable = false)
    private Integer thickness;

    @Builder
    public Drawing(Order order, String fileName, Long fileSize, String fileType, String fileUrl, String thumbnailUrl, Integer count, String ingredient, Integer thickness) {
        this.order = order;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileType = DrawingFileType.ofExtension(fileType);
        this.fileUrl = fileUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.count = count;
        this.ingredient = Ingredient.ofValue(ingredient);
        this.thickness = thickness;
    }

    public void updateDrawingProperties(CustomerUpdateDrawingRequest request) {
        this.count = request.getCount();
        this.ingredient = Ingredient.ofValue(request.getIngredient());
        this.thickness = request.getThickness();
    }
}
