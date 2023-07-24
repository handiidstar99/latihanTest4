package com.latihan4.crud.model;

import lombok.Data;

@Data
public class Response {
    private Integer code;
    private Object data;
    private String status;
}
