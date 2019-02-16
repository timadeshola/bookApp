package com.booktest.resources.model.response;

import lombok.Data;

import java.util.List;

@Data
public class PaginateResponse<T> {

    private List<T> contents;
    private long totalElements;
}
