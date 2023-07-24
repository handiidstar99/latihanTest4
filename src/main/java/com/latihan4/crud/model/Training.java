package com.latihan4.crud.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "training")
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String pengajar;
    private String tema;
    private Date createdDate;
    private Date updateDate;
    private Date deletedDate;
}
