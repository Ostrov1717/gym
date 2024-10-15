package org.example.services;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
        log.info("Fetching training list for trainee: {}, from: {}, to: {}", traineeUsername, dataFrom, dataTo);
        Trainee trainee = traineeService.selectByUsername(traineeUsername).orElseThrow(() -> {
            log.error("Trainee with username: {} not found", traineeUsername);
            return new IllegalArgumentException("Trainee with username: " + traineeUsername + " not found.");
        });
        List<Training> trainingList;
        if (dataFrom != null && dataTo != null) {
            log.debug("Selecting trainings for trainee {} within the period: {} - {}", traineeUsername, dataFrom, dataTo);
            trainingList = trainingService.selectByPeriod(dataFrom, dataTo)
                    .stream()
                    .filter(tr -> tr.getTraineeId() == trainee.getUserId())
                    .sorted(Comparator.comparing(Training::getTrainingDate))
                    .toList();
        } else {
            log.debug("Selecting all trainings for trainee {}", traineeUsername);
            trainingList = trainingService.selectByTraineeId(trainee.getUserId());
        }
        String result=traineeTrainingList(trainingList,trainee);
        log.info("Successfully retrieved training list for trainee: {}\n", traineeUsername);
        return result;
    }
    public String getTrainerTrainingList(String trainerUsername, LocalDateTime dataFrom, LocalDateTime dataTo) {
        log.info("Fetching training list for trainer: {}, from: {}, to: {}", trainerUsername, dataFrom, dataTo);
        Trainer trainer = trainerService.selectByUsername(trainerUsername).orElseThrow(() -> {
            log.error("Trainer with username: {} not found", trainerUsername);
            return new IllegalArgumentException("Trainer with username: " + trainerUsername + " not found.");
        });
        List<Training> trainingList;
        if (dataFrom != null && dataTo != null) {
            log.debug("Selecting trainings for trainer {} within the period: {} - {}", trainerUsername, dataFrom, dataTo);
            trainingList = trainingService.selectByPeriod(dataFrom, dataTo)
                    .stream()
                    .filter(tr -> tr.getTrainerId() == trainer.getUserId())
                    .sorted(Comparator.comparing(Training::getTrainingDate))
                    .toList();
        } else {
            log.debug("Selecting all trainings for trainer {}", trainerUsername);
            trainingList = trainingService.selectByTrainerId(trainer.getUserId());
        }
        String result=trainerTrainingList(trainingList,trainer);
        log.info("Successfully retrieved training list for trainer: {}\n", trainerUsername);
        return result;
    }

    private String traineeTrainingList(List<Training> trainings, Trainee trainee) {
        log.debug("Formatting training list for trainee: {}", trainee.getUsername());
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
        log.debug("Finished formatting training list for trainee: {}", trainee.getUsername());
        return stringBuilder.toString();
    }
    private String trainerTrainingList(List<Training> trainings, Trainer trainer) {
        log.debug("Formatting training list for trainer: {}", trainer.getUsername());
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
        log.debug("Finished formatting training list for trainer: {}", trainer.getUsername());
        return stringBuilder.toString();
    }
}
