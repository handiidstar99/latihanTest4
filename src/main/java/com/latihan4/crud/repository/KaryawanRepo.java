package com.latihan4.crud.repository;

import com.latihan4.crud.model.Karyawan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KaryawanRepo extends JpaRepository<Karyawan, Integer> {
}
