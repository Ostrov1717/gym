package org.example.services;

import jakarta.annotation.PostConstruct;
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
public class TrainingService {
    @Autowired
    private GenericDAO<Training> dao;
    private long nextId;

    public Training create(long traineeId, long trainerId, String trainingName, TrainingType type, LocalDateTime trainingDate, Duration duration){
        if (traineeId == 0) throw new IllegalArgumentException("Trainee Id cannot be zero.");
        if (trainerId == 0) throw new IllegalArgumentException("Trainer Id cannot be zero.");
        if (trainingName == null || trainingName.isBlank()) throw new IllegalArgumentException("Training name cannot be null or empty.");
        if (type == null) throw new IllegalArgumentException("Training type cannot be null.");
        if (trainingDate == null) throw new IllegalArgumentException("Training date cannot be null.");
        if (duration == null) throw new IllegalArgumentException("Training duration cannot be null.");
        Training training=new Training();
        long id=++nextId;
        training.setTrainingId(id);
        training.setTraineeId(traineeId);
        training.setTrainerId(trainerId);
        training.setTrainingName(trainingName);
        training.setTrainingType(type);
        training.setTrainingDate(trainingDate);
        training.setTrainingDuration(duration);
        return dao.save(training,id);
    }

    public Training selectByTrainingId(long trainingId){
        return dao.findById(trainingId);
    }
    public List<Training> selectByTrainerId(long trainerId){
        return getAll().stream()
                .filter(training -> training.getTrainerId() == trainerId)
                .collect(Collectors.toList());
    }
    public List<Training> selectByTraineeId(long traineeId){
        return getAll().stream()
                .filter(training -> training.getTraineeId() == traineeId)
                .collect(Collectors.toList());
    }
    public List<Training> selectByPeriod(LocalDateTime dataFrom,LocalDateTime dataTo){
        return getAll().stream()
                .filter(training -> training.getTrainingDate().isAfter(dataFrom)&&training.getTrainingDate().isBefore(dataTo))
                .collect(Collectors.toList());
    }

    public List<Training> getAll(){
        return new ArrayList<>(dao.getAll().values());
    }

    @PostConstruct
    public void init() {
        this.nextId = dao.getAll().keySet().stream().max(Long::compareTo).orElse(0L);
    }

}
