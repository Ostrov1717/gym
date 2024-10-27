package org.example.profiles;

import lombok.Data;
import org.example.model.TrainingType;

@Data
public class TrainerProfile {

    private String firstName;

    private String lastName;

    private String username;

    private boolean active;

    private TrainingType specialization;

//    private Set<Trainee> trainees;
//
//    private Set<Training> trainings;
}
