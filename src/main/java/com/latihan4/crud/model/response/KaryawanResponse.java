package com.latihan4.crud.model.response;

import com.latihan4.crud.model.DetailKaryawan;
import lombok.Data;
import java.util.Date;

@Data
public class KaryawanResponse {
    private Integer id;
    private String nama;
    private Date dob;
    private String status;
    private String alamat;
    private DetailKaryawan detailKaryawan;
    private Date createdDate;
    private Date updateDate;
    private Date deletedDate;
}
