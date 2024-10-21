package org.example.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Trainer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trainerId;

    @ManyToOne
    @JoinColumn(name = "specialization_id", referencedColumnName = "id")
    private TrainingType specialization;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToMany(mappedBy = "trainers", fetch = FetchType.LAZY)
    private Set<Trainee> trainees = new HashSet<>();

    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL)
    private Set<Training> trainings = new HashSet<>();


    public Trainer() {
    }

    public Trainer(TrainingType specialization, User user) {
        this.specialization = specialization;
        this.user = user;
    }

    //    public Trainer(long trainerId, String firstName, String lastName, String username, String password, boolean isActive, TrainingType specialization) {
//        super(firstName, lastName, username, password, isActive);
//        this.trainerId = trainerId;
//        this.specialization = specialization;
//    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Trainer{");
        sb.append("userID=").append(trainerId);
        sb.append(super.toString());
        sb.append(", specialization=").append(specialization);
        sb.append('}');
        return sb.toString();
    }
}
