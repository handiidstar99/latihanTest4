package com.latihan4.crud.model;

import lombok.Data;

@Data
public class Pageable {
    private Sort sort;
    private Long offset;
    private Integer pageNumber;
    private Integer pageSize;
    private Boolean unpaged;
    private Boolean paged;
}
