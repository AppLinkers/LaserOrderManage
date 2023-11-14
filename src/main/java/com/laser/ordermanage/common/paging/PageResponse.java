package com.laser.ordermanage.common.paging;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PageResponse<T> {

    private final List<T> contents;

    private final Integer page;

    private final Integer size;

    private final Long totalElements;

    private final Integer totalPages;

    private final Boolean first;

    private final Boolean last;

    public PageResponse(Page<T> contentPage) {
        this.contents = contentPage.getContent();

        this.page = contentPage.getNumber() + 1;
        this.size = contentPage.getNumberOfElements();
        this.totalElements = contentPage.getTotalElements();
        this.totalPages = contentPage.getTotalPages();
        this.first = (contentPage.getNumber() == 0) ? Boolean.TRUE : Boolean.FALSE;
        this.last = ((contentPage.getNumber() + 1) == contentPage.getTotalPages()) ? Boolean.TRUE : Boolean.FALSE;

    }
}
