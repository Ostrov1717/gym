package org.example.services;

import jakarta.annotation.PostConstruct;
import org.example.dao.GenericDAO;
import org.example.model.Training;
import org.example.model.TrainingType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainingService {
    @Autowired
    private GenericDAO<Training> dao;
    private long nextId;

    private static final Logger logger = LoggerFactory.getLogger(TrainingService.class);


    public Training create(long traineeId, long trainerId, String trainingName, TrainingType type, LocalDateTime trainingDate, Duration duration){
        logger.info("Creation of new training: traineeId={}, trainerId={}, name={}, type={}, date={}, duration={}",
                traineeId, trainerId, trainingName, type, trainingDate, duration);
        if (traineeId == 0) {
            logger.error("Error: traineeId cannot be equal to 0");
            throw new IllegalArgumentException("Trainee Id cannot be equal to 0");
        }
        if (trainerId == 0) {
            logger.error("Error: trainerId cannot be equal to 0");
            throw new IllegalArgumentException("Trainer Id cannot be equal to 0");
        }
        if (trainingName == null || trainingName.isBlank()) {
            logger.error("Error: trainingName cannot be null/blank");
            throw new IllegalArgumentException("Training name cannot be null/blank");
        }
        if (type == null) {
            logger.error("Error: training type cannot be null");
            throw new IllegalArgumentException("Training type cannot be null");
        }
        if (trainingDate == null) {
            logger.error("Error: training date cannot be null");
            throw new IllegalArgumentException("Training date cannot be null");
        }
        if (duration == null) {
            logger.error("Error: training duration cannot be null");
            throw new IllegalArgumentException("Training duration cannot be null.");
        }
        Training training=new Training();
        long id=nextId++;
        training.setTrainingId(id);
        training.setTraineeId(traineeId);
        training.setTrainerId(trainerId);
        training.setTrainingName(trainingName);
        training.setTrainingType(type);
        training.setTrainingDate(trainingDate);
        training.setTrainingDuration(duration);
        logger.info("Training has been created: id={}, name={}", id, trainingName);
        return dao.save(training,id);
    }

    public Training selectByTrainingId(long trainingId){
        logger.info("Search training by Id: {}", trainingId);
        return dao.findById(trainingId);
    }
    public List<Training> selectByTrainerId(long trainerId){
        logger.info("Search trainings by trainer Id: {}", trainerId);
        return getAll().stream()
                .filter(training -> training.getTrainerId() == trainerId)
                .collect(Collectors.toList());
    }
    public List<Training> selectByTraineeId(long traineeId){
        logger.info("Search trainings by trainee Id: {}", traineeId);
        return getAll().stream()
                .filter(training -> training.getTraineeId() == traineeId)
                .collect(Collectors.toList());
    }
    public List<Training> selectByPeriod(LocalDateTime dataFrom,LocalDateTime dataTo){
        logger.info("Selecting trainings by period of time from {} to {}", dataFrom,dataTo);
        return getAll().stream()
                .filter(training -> training.getTrainingDate().isAfter(dataFrom)&&training.getTrainingDate().isBefore(dataTo))
                .collect(Collectors.toList());
    }

    public List<Training> getAll(){
        logger.info("Getting all Trainings");
        return new ArrayList<>(dao.getAll().values());
    }

    @PostConstruct
    public void init() {
        logger.info("Initialization of TrainingService and definition of next Training Id");
        this.nextId = dao.getAll().keySet().stream().max(Long::compareTo).orElse(0L)+1;
        logger.info("next Training Id is {}\n", nextId);
    }

}