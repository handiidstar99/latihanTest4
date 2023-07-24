package com.latihan4.crud.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "detail_karyawan")
public class DetailKaryawan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nik;
    private String npwp;
    private Date createdDate;
    private Date updateDate;
    private Date deletedDate;
}
