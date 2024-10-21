package org.example.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Entity
public class Training implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long trainingId;
    @ManyToOne
    @JoinColumn(name = "traineeid", referencedColumnName = "traineeid")
    private Trainee trainee;
    @ManyToOne
    @JoinColumn(name = "trainerid", referencedColumnName = "trainerid")
    private Trainer trainer;

    private String trainingName;

    @ManyToOne
    @JoinColumn(name = "trainingTypeId", referencedColumnName = "id")
    private TrainingType trainingType;

    private LocalDateTime trainingDate;

    private Duration trainingDuration;

    public Training() {
    }

    public Training(Trainee trainee, Trainer trainer, String trainingName, TrainingType trainingType, LocalDateTime trainingDate, Duration trainingDuration) {
        this.trainee = trainee;
        this.trainer = trainer;
        this.trainingName = trainingName;
        this.trainingType = trainingType;
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
    }
    //    public Training(long trainingId, long traineeId, long trainerId, String trainingName, TrainingType trainingType, LocalDateTime trainingDate, Duration trainingDuration) {
//        this.trainingId = trainingId;
//        this.traineeId = traineeId;
//        this.trainerId = trainerId;
//        this.trainingName = trainingName;
//        this.trainingType = trainingType;
//        this.trainingDate = trainingDate;
//        this.trainingDuration = trainingDuration;
//    }

    @Override
    public String toString() {
        String duration = String.format("%02d:%02d:%02d", trainingDuration.toHours(), trainingDuration.toMinutesPart(), trainingDuration.toSecondsPart());
        DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        final StringBuffer sb = new StringBuffer("Training{");
        sb.append("id=").append(trainingId);
//        sb.append(", traineeId=").append(traineeId);
//        sb.append(", trainerId=").append(trainerId);
        sb.append(", name='").append(trainingName).append('\'');
        sb.append(", type=").append(trainingType);
        sb.append(", date=").append(trainingDate.format(CUSTOM_FORMATTER));
        sb.append(", duration=").append(duration);
        sb.append('}');
        return sb.toString();
    }
}
