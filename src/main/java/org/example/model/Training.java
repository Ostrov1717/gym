package org.example.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Training {
    private long trainingId;
    private long traineeId;
    private long trainerId;
    private String trainingName;
    private TrainingType trainingType;
    private LocalDateTime trainingDate;
    private Duration trainingDuration;


    public Training(){}

    public Training(long trainingId,long traineeId, long trainerId, String trainingName, TrainingType trainingType, LocalDateTime trainingDate, Duration trainingDuration) {
        this.trainingId=trainingId;
        this.traineeId = traineeId;
        this.trainerId = trainerId;
        this.trainingName = trainingName;
        this.trainingType = trainingType;
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
    }

    public long getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(long trainingId) {
        this.trainingId = trainingId;
    }

    public long getTraineeId() {
        return traineeId;
    }

    public void setTraineeId(long traineeId) {
        this.traineeId = traineeId;
    }

    public long getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(long trainerId) {
        this.trainerId = trainerId;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public TrainingType getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(TrainingType trainingType) {
        this.trainingType = trainingType;
    }

    public LocalDateTime getTrainingDate() {
        return trainingDate;
    }

    public void setTrainingDate(LocalDateTime trainingDate) {
        this.trainingDate = trainingDate;
    }

    public Duration getTrainingDuration() {
        return trainingDuration;
    }

    public void setTrainingDuration(Duration trainingDuration) {
        this.trainingDuration = trainingDuration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Training training = (Training) o;

        if (trainingId != training.trainingId) return false;
        if (traineeId != training.traineeId) return false;
        if (trainerId != training.trainerId) return false;
        if (!Objects.equals(trainingName, training.trainingName))
            return false;
        if (trainingType != training.trainingType) return false;
        if (!Objects.equals(trainingDate, training.trainingDate))
            return false;
        return Objects.equals(trainingDuration, training.trainingDuration);
    }

    @Override
    public int hashCode() {
        int result = (int) (trainingId ^ (trainingId >>> 32));
        result = 31 * result + (int) (traineeId ^ (traineeId >>> 32));
        result = 31 * result + (int) (trainerId ^ (trainerId >>> 32));
        result = 31 * result + (trainingName != null ? trainingName.hashCode() : 0);
        result = 31 * result + (trainingType != null ? trainingType.hashCode() : 0);
        result = 31 * result + (trainingDate != null ? trainingDate.hashCode() : 0);
        result = 31 * result + (trainingDuration != null ? trainingDuration.hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        String duration=String.format("%02d:%02d:%02d", trainingDuration.toHours(), trainingDuration.toMinutesPart(), trainingDuration.toSecondsPart());
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
