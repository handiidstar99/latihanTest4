package com.latihan4.crud.repository;

import com.latihan4.crud.model.Training;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingRepo extends JpaRepository<Training, Integer> {
}
