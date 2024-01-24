package com.laser.ordermanage.common.entity.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class File<T> {

    @Column(name = "file_name", nullable = false)
    private String name;

    @Column(name = "file_size", nullable = false)
    private Long size;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "file_type", nullable = false)
    private T type;

    @Column(name = "file_url", nullable = false)
    private String url;

    @Builder
    public File(String name, Long size, T type, String url) {
        this.name = name;
        this.size = size;
        this.type = type;
        this.url = url;
    }
}
