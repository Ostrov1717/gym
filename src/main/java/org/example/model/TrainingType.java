package org.example.model;

import jakarta.persistence.*;

@Entity
public class TrainingType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String trainingType;

    public TrainingType() {
    }

    public TrainingType(String trainingType) {
        this.trainingType = trainingType;
    }
}
