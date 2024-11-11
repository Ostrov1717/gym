package org.example.gym.domain.training.repository;

import org.example.gym.domain.training.entity.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.Optional;

@RepositoryDefinition(domainClass = TrainingType.class, idClass = Long.class)
public interface TrainingTypeRepository extends JpaRepository<TrainingType, Long> {

    Optional<TrainingType> findByTrainingType(String trainingType);
}
