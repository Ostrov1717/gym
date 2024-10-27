package org.example.profiles;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TraineeProfile {

    private String firstName;

    private String lastName;

    private String username;

    private boolean active;

    private String address;

    private LocalDate dateOfBirth;

//    private Set<Trainer> trainers;
//
//    private Set<Training> trainings;

}
