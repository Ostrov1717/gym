package org.example.dao;

import org.example.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {

    Optional<Trainer> findByUserUsername(String username);

    @Query("SELECT t FROM Trainer t WHERE t.user.username = :username AND t.user.password = :password")
    Optional<Trainer> findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    @Query("SELECT t FROM Trainer t WHERE t NOT IN (SELECT tr FROM Trainee trainee JOIN trainee.trainers tr WHERE trainee.user.username = :username)")
    List<Trainer> findTrainersNotAssignedToTraineeByUsername(@Param("username") String username);

}
