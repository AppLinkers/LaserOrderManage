package com.laser.ordermanage.common.dto.response;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PageRes<T> {

    private List<T> contents;

    private Integer page;

    private Integer size;

    private Long totalElements;

    private Integer totalPages;

    private Boolean first;

    private Boolean last;

    public PageRes(Page<T> contentPage) {
        this.contents = contentPage.getContent();

        this.page = contentPage.getNumber() + 1;
        this.size = contentPage.getNumberOfElements();
        this.totalElements = contentPage.getTotalElements();
        this.totalPages = contentPage.getTotalPages();
        this.first = (contentPage.getNumber() == 0) ? Boolean.TRUE : Boolean.FALSE;
        this.last = ((contentPage.getNumber() + 1) == contentPage.getTotalPages()) ? Boolean.TRUE : Boolean.FALSE;

    }
}
