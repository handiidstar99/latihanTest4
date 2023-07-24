package com.latihan4.crud.model.request;

import com.latihan4.crud.model.Karyawan;
import com.latihan4.crud.model.Training;
import lombok.Data;

@Data
public class KaryawanTrainingRequest {
    private Karyawan karyawan;
    private Training training;
    private String tanggal;
}
