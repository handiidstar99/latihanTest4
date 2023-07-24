package com.latihan4.crud.model.response;

import com.latihan4.crud.model.Karyawan;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
public class RekeningResponse {
    private Integer id;
    private String nama;
    private String jenis;
    private String rekening;
    private String alamat;
    private Object karyawan;
    private Date createdDate;
    private Date updateDate;
    private Date deletedDate;
}
