package com.latihan4.crud.model;

import lombok.Data;

import java.util.List;

@Data
public class PageList {
    private List content;
    private Pageable pageable;
    private Boolean last;
    private Long totalElements;
    private Integer totalPages;
    private Integer size;
    private Integer number;
    private Sort sort;
    private Boolean first;
    private Integer numberOfElements;
    private Boolean empty;
}
