package org.example.gym.domain.training.repository;

import org.example.gym.domain.training.entity.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TrainingRepository extends JpaRepository<Training, Long> {
    @Query("SELECT t FROM Training t " +
            "JOIN t.trainer trainer " +
            "JOIN t.trainee trainee " +
            "WHERE trainer.user.username = :username " +
            "AND (t.trainingDate BETWEEN :fromDate AND :toDate) " +
            "AND (trainee.user.username = :traineeName OR :traineeName IS NULL) " +
            "ORDER BY t.trainingDate DESC")
    List<Training> findTrainingsByTrainerAndCriteria(
            @Param("username") String username,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate,
            @Param("traineeName") String traineeName);

    @Query("SELECT tr FROM Training tr " +
            "JOIN tr.trainee t " +
            "JOIN tr.trainer trn " +
            "JOIN tr.trainingType type " +
            "WHERE t.user.username = :username " +
            "AND (tr.trainingDate >= :periodFrom) " +
            "AND (tr.trainingDate <= :periodTo) " +
            "AND (:trainerName IS NULL OR trn.user.username LIKE %:trainerName%)")
    List<Training> findTrainingsByTraineeAndCriteria(
            @Param("username") String username,
            @Param("periodFrom") LocalDateTime periodFrom,
            @Param("periodTo") LocalDateTime periodTo,
            @Param("trainerName") String trainerName);
}
