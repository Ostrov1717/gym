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

@Service
public class TrainingService {
    @Autowired
    private GenericDAO<Training> dao;
    private long nextId;

    public void create(long traineeId, long trainerId, String trainingName, TrainingType type, LocalDateTime trainingDate, Duration duration){
        Training training=new Training();
        long id=++nextId;
        training.setTrainingId(id);
        training.setTraineeId(traineeId);
        training.setTrainerId(trainerId);
        training.setTrainingName(trainingName);
        training.setTrainingType(type);
        training.setTrainingDate(trainingDate);
        training.setTrainingDuration(duration);
        dao.save(training,id);
    }

    public Training selectByTrainingId(long trainingId){
        return dao.findById(trainingId);
    }

    public List<Training> getAll(){
        return new ArrayList<>(dao.getAll().values());
    }

    @PostConstruct
    public void init() {
        this.nextId = dao.getAll().keySet().stream().max(Long::compareTo).orElse(0L);
    }

}
