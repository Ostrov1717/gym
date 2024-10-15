package org.example.services;

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

    public Training create(long traineeId, long trainerId, String trainingName, TrainingType type, LocalDateTime trainingDate, Duration duration) {
        log.info("Creation of new training: traineeId={}, trainerId={}, name={}, type={}, date={}, duration={}",
                traineeId, trainerId, trainingName, type, trainingDate, duration);
        if (traineeId == 0) {
            log.error("Error: traineeId cannot be equal to 0");
            throw new IllegalArgumentException("Trainee Id cannot be equal to 0");
        }
        if (trainerId == 0) {
            log.error("Error: trainerId cannot be equal to 0");
            throw new IllegalArgumentException("Trainer Id cannot be equal to 0");
        }
        if (trainingName == null || trainingName.isBlank()) {
            log.error("Error: trainingName cannot be null/blank");
            throw new IllegalArgumentException("Training name cannot be null/blank");
        }
        if (type == null) {
            log.error("Error: training type cannot be null");
            throw new IllegalArgumentException("Training type cannot be null");
        }
        if (trainingDate == null) {
            log.error("Error: training date cannot be null");
            throw new IllegalArgumentException("Training date cannot be null");
        }
        if (duration == null) {
            log.error("Error: training duration cannot be null");
            throw new IllegalArgumentException("Training duration cannot be null.");
        }
        Training training = new Training();
        long id = nextId++;
        training.setTrainingId(id);
        training.setTraineeId(traineeId);
        training.setTrainerId(trainerId);
        training.setTrainingName(trainingName);
        training.setTrainingType(type);
        training.setTrainingDate(trainingDate);
        training.setTrainingDuration(duration);
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
