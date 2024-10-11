package org.example.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class GetSetTest {

    @Test
    @DisplayName("Trainee fields successfully tested")
    void testTrainee() {
        GetSetVerifier verifier = new GetSetVerifier(Trainee.class);
        verifier.verify("userId", 1L, 2L);
        verifier.verify("firstName", "Alex", "Mark");
        verifier.verify("lastName", "Black", "White");
        verifier.verify("username","one.username","two.username" );
        verifier.verify("password", "pasword1", "password2");
        verifier.verify("active", true, false);
        verifier.verify("address", "California", "Alaska");
        verifier.verify("dateOfBirth", LocalDate.now(), LocalDate.now().minusDays(10));
    }

    @Test
    @DisplayName("Trainer fields successfully tested")
    void testTrainer() {
        GetSetVerifier verifier = new GetSetVerifier(Trainer.class);
        verifier.verify("userId", 1L, 2L);
        verifier.verify("firstName", "Alex", "Mark");
        verifier.verify("lastName", "Black", "White");
        verifier.verify("username","one.username","two.username" );
        verifier.verify("password", "pasword1", "password2");
        verifier.verify("active", true, false);
        verifier.verify("specialization", TrainingType.YOGA, TrainingType.FITNESS);
    }

    @Test
    @DisplayName("Training fields successfully tested")
    void testTraining() {
        GetSetVerifier verifier = new GetSetVerifier(Training.class);
        verifier.verify("trainingId", 1L, 2L);
        verifier.verify("traineeId", 1L, 2L);
        verifier.verify("trainerId", 1L, 2L);
        verifier.verify("trainingName", "Morning workout", "Evening jogging");
        verifier.verify("trainingType", TrainingType.YOGA, TrainingType.FITNESS);
        verifier.verify("trainingDate", LocalDateTime.now(),LocalDateTime.now().minusDays(2));
        verifier.verify("trainingDuration", Duration.ofHours(1), Duration.ofHours(2));
    }

}
