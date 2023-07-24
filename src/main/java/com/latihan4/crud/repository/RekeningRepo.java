package com.latihan4.crud.repository;

import com.latihan4.crud.model.Rekening;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RekeningRepo extends JpaRepository<Rekening, Integer> {
}
