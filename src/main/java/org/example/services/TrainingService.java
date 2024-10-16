package org.example.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.example.dao.GenericDAO;
import org.example.model.Training;
import org.example.model.TrainingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TrainingService {

    private GenericDAO<Training> dao;
    private long nextId;

    @Autowired
    public void setDao(GenericDAO<Training> dao) {
        this.dao = dao;
        log.info("Initialization of TrainingService and definition of next Training Id");
        this.nextId = dao.getAll().keySet().stream().max(Long::compareTo).orElse(0L) + 1;
        log.info("next Training Id is {}\n", nextId);
    }

    public Training create(long traineeId, long trainerId, @NonNull String trainingName, @NonNull TrainingType type, @NonNull LocalDateTime trainingDate, @NonNull Duration duration) {
        log.info("Creation of new training: traineeId={}, trainerId={}, name={}, type={}, date={}, duration={}",
                traineeId, trainerId, trainingName, type, trainingDate, duration);
        if (trainingName.isBlank()) {
            log.error("Error: trainingName cannot beblank");
            throw new IllegalArgumentException("Training name cannot be null/blank");
        }
        long id = nextId++;
        Training training = new Training(id, traineeId, trainerId, trainingName, type, trainingDate, duration);
        log.info("Training has been created: id={}, name={}", id, trainingName);
        return dao.save(training, id);
    }

    public Training selectByTrainingId(long trainingId) {
        log.info("Search training by Id: {}", trainingId);
        return dao.findById(trainingId);
    }

    public List<Training> selectByTrainerId(long trainerId) {
        log.info("Search trainings by trainer Id: {}", trainerId);
        return getAll().stream()
                .filter(training -> training.getTrainerId() == trainerId)
                .collect(Collectors.toList());
    }

    public List<Training> selectByTraineeId(long traineeId) {
        log.info("Search trainings by trainee Id: {}", traineeId);
        return getAll().stream()
                .filter(training -> training.getTraineeId() == traineeId)
                .collect(Collectors.toList());
    }

    public List<Training> selectByPeriod(LocalDateTime dataFrom, LocalDateTime dataTo) {
        log.info("Selecting trainings by period of time from {} to {}", dataFrom, dataTo);
        return getAll().stream()
                .filter(training -> training.getTrainingDate().isAfter(dataFrom) && training.getTrainingDate().isBefore(dataTo))
                .collect(Collectors.toList());
    }

    public List<Training> getAll() {
        log.info("Getting all Trainings");
        return new ArrayList<>(dao.getAll().values());
    }

}
