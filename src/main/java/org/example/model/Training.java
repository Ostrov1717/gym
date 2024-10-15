package org.example.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@EqualsAndHashCode
public class Training {
    private long trainingId;
    private long traineeId;
    private long trainerId;
    private String trainingName;
    private TrainingType trainingType;
    private LocalDateTime trainingDate;
    private Duration trainingDuration;

    public Training() {
    }

    public Training(long trainingId, long traineeId, long trainerId, String trainingName, TrainingType trainingType, LocalDateTime trainingDate, Duration trainingDuration) {
        this.trainingId = trainingId;
        this.traineeId = traineeId;
        this.trainerId = trainerId;
        this.trainingName = trainingName;
        this.trainingType = trainingType;
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
    }

    @Override
    public String toString() {
        String duration = String.format("%02d:%02d:%02d", trainingDuration.toHours(), trainingDuration.toMinutesPart(), trainingDuration.toSecondsPart());
        DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        final StringBuffer sb = new StringBuffer("Training{");
        sb.append("id=").append(trainingId);
        sb.append(", traineeId=").append(traineeId);
        sb.append(", trainerId=").append(trainerId);
        sb.append(", name='").append(trainingName).append('\'');
        sb.append(", type=").append(trainingType);
        sb.append(", date=").append(trainingDate.format(CUSTOM_FORMATTER));
        sb.append(", duration=").append(duration);
        sb.append('}');
        return sb.toString();
    }
}
