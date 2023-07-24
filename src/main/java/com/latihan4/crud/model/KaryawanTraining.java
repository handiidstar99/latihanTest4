package com.latihan4.crud.model;

import lombok.Data;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "karyawan_training")
public class KaryawanTraining {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Date tanggal;
    @OneToOne
    @JoinColumn(name = "training_id")
    private Training training;
    @OneToMany()
    @JoinTable(
            joinColumns = @JoinColumn(name = "karyawan_training_id"),
            inverseJoinColumns = @JoinColumn(name = "karyawan_id")
    )
    private List<Karyawan> karyawan;
    public void addKaryawan(Karyawan karyawan){
        this.karyawan.add(karyawan);
    }
    private Date createdDate;
    private Date updateDate;
    private Date deletedDate;
}
