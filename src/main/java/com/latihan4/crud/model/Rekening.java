package com.latihan4.crud.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class Rekening {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nama;
    private String jenis;
    private String rekening;
    private String alamat;

    @OneToOne
    @JoinColumn(name = "karyawan_id")
    private Karyawan karyawan;

    private Date createdDate;
    private Date updateDate;
    private Date deletedDate;
}
