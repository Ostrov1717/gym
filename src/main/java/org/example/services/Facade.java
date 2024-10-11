package org.example.services;

import org.example.model.Trainee;
import org.example.model.Trainer;
import org.example.model.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class Facade {
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    @Autowired
    public Facade(TraineeService traineeService, TrainerService trainerService, TrainingService trainingService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
    }

    public String getTraineeTrainingList(String username, LocalDateTime dataFrom, LocalDateTime dataTo) {
        Trainee trainee = traineeService.selectByUsername(username).orElseThrow(() -> new IllegalArgumentException("Trainee with username: " + username + " not found."));
        if (dataFrom != null && dataTo != null) {
            List<Training> trainingList = trainingService.selectByPeriod(dataFrom, dataTo).stream().filter(tr -> tr.getTraineeId() == trainee.getUserId()).toList();
            return formTrainingList(trainingList,username);
        } else {
            List<Training> trainingList = trainingService.selectByTraineeId(trainee.getUserId());
            return formTrainingList(trainingList,username);
        }
    }

    private String formTrainingList(List<Training> trainings, String traineeUserName) {
        StringBuilder stringBuilder = new StringBuilder("Training list of " + traineeUserName + ":\n");
        for (Training training : trainings) {
            Trainer trainer = trainerService.selectById(training.getTrainerId()).get();
            stringBuilder.append(training.getTrainingDate()).append(" ")
                    .append(training.getTrainingType()).append(" ")
                    .append(trainer.getLastName()).append("\n");
        }
        return stringBuilder.toString();
    }
}
