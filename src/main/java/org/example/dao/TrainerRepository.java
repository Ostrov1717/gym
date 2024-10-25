package org.example.dao;

import org.example.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainerRepository extends JpaRepository<Trainer,Long> {

    Optional<Trainer> findByUserUsername(String username);
}
