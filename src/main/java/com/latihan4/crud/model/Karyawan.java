package com.latihan4.crud.model;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class Karyawan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nama;
    private Date dob;
    private String status;
    private String alamat;
    @OneToOne
    @JoinColumn(name = "detail_karyawan_id")
    private DetailKaryawan detailKaryawan;
    private Date createdDate;
    private Date updateDate;
    private Date deletedDate;
}
