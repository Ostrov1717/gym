package org.example.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class TrainingType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String trainingType;

    public TrainingType() {
    }

    public TrainingType(String type) {
        this.trainingType = type;
    }

}
