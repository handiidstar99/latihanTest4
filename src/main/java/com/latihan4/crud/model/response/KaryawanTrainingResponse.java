package com.latihan4.crud.model.response;

import com.latihan4.crud.model.Training;
import lombok.Data;

import java.util.Date;

@Data
public class KaryawanTrainingResponse {
    private Integer id;
    private Date tanggal;
    private Training training;
    private KaryawanResponse karyawan;
    private Date createdDate;
    private Date updateDate;
    private Date deletedDate;
}
