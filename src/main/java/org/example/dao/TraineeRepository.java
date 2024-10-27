package org.example.dao;

import org.example.model.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TraineeRepository extends JpaRepository<Trainee, Long> {

    Optional<Trainee> findByUserUsername(String username);

    @Query("SELECT t FROM Trainee t WHERE t.user.username = :username AND t.user.password = :password")
    Optional<Trainee> findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
}
