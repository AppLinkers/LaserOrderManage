package com.laser.ordermanage.order.domain;

import com.laser.ordermanage.order.domain.type.DrawingFileType;
import com.laser.ordermanage.order.domain.type.Ingredient;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
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
}
