package com.laser.ordermanage.common.paging;

import lombok.Getter;

import java.util.List;

@Getter
public class ListResponse<T> {

    private final List<T> contents;

    private final Long totalElements;

    public ListResponse(List<T> contents) {
        this.contents = contents;
        this.totalElements = (long) contents.size();
    }
}
