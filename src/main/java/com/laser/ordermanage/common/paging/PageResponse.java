package com.laser.ordermanage.common.paging;

import org.springframework.data.domain.Page;

import java.util.List;

public record PageResponse<T>(
        List<T> contents,
        Integer page,
        Integer size,
        Long totalElements,
        Integer totalPages,
        Boolean first,
        Boolean last
) {
    public PageResponse(Page<T> contentPage) {
        this(
                contentPage.getContent(),
                contentPage.getNumber() + 1,
                contentPage.getNumberOfElements(),
                contentPage.getTotalElements(),
                contentPage.getTotalPages(),
                contentPage.getNumber() == 0,
                (contentPage.getNumber() + 1) == contentPage.getTotalPages()
        );
    }
}
