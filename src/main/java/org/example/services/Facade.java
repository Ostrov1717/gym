package org.example.services;

import org.example.model.Trainee;
import org.example.model.Trainer;
import org.example.model.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
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

    public String getTraineeTrainingList(String traineeUsername, LocalDateTime dataFrom, LocalDateTime dataTo) {
        Trainee trainee = traineeService.selectByUsername(traineeUsername).orElseThrow(() -> new IllegalArgumentException("Trainee with username: " + traineeUsername + " not found."));
        if (dataFrom != null && dataTo != null) {
            List<Training> trainingList = trainingService.selectByPeriod(dataFrom, dataTo)
                    .stream()
                    .filter(tr -> tr.getTraineeId() == trainee.getUserId())
                    .sorted(Comparator.comparing(Training::getTrainingDate))
                    .toList();
            return traineeTrainingList(trainingList,trainee);
        } else {
            List<Training> trainingList = trainingService.selectByTraineeId(trainee.getUserId());
            return traineeTrainingList(trainingList,trainee);
        }
    }
    public String getTrainerTrainingList(String trainerUsername, LocalDateTime dataFrom, LocalDateTime dataTo) {
        Trainer trainer = trainerService.selectByUsername(trainerUsername).orElseThrow(() -> new IllegalArgumentException("Trainer with username: " + trainerUsername + " not found."));
        if (dataFrom != null && dataTo != null) {
            List<Training> trainingList = trainingService.selectByPeriod(dataFrom, dataTo)
                    .stream()
                    .filter(tr -> tr.getTrainerId() == trainer.getUserId())
                    .sorted(Comparator.comparing(Training::getTrainingDate))
                    .toList();
            return trainerTrainingList(trainingList,trainer);
        } else {
            List<Training> trainingList = trainingService.selectByTrainerId(trainer.getUserId());
            return trainerTrainingList(trainingList,trainer);
        }
    }

    private String traineeTrainingList(List<Training> trainings, Trainee trainee) {
        StringBuilder stringBuilder = new StringBuilder("Training list of trainee - " + trainee.getFirstName()+" "+trainee.getLastName() +":\n");
        for (Training training : trainings) {
            Trainer trainer = trainerService.selectById(training.getTrainerId()).orElse(new Trainer());
            String duration=String.format("%02d:%02d:%02d", training.getTrainingDuration().toHours(), training.getTrainingDuration().toMinutesPart(), training.getTrainingDuration().toSecondsPart());
            stringBuilder.append(training.getTrainingName()).append(" - ")
                    .append("Data and time:").append(training.getTrainingDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .append(", duration:").append(duration)
                    .append(", training type:").append(training.getTrainingType())
                    .append(", trainer's name:").append(trainer.getLastName()).append("\n");
        }
        return stringBuilder.toString();
    }
    private String trainerTrainingList(List<Training> trainings, Trainer trainer) {
        StringBuilder stringBuilder = new StringBuilder("Training list of trainer - " + trainer.getFirstName()+" "+trainer.getLastName() + ":\n");
        for (Training training : trainings) {
            Trainee trainee = traineeService.selectById(training.getTraineeId()).orElse(new Trainee());
            String duration=String.format("%02d:%02d:%02d", training.getTrainingDuration().toHours(), training.getTrainingDuration().toMinutesPart(), training.getTrainingDuration().toSecondsPart());
            stringBuilder.append(training.getTrainingName()).append(" - ")
                    .append("Data and time:").append(training.getTrainingDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .append(", duration:").append(duration)
                    .append(", training type:").append(training.getTrainingType())
                    .append(", trainee's name:").append(trainee.getLastName()).append("\n");
        }
        return stringBuilder.toString();
    }

}
