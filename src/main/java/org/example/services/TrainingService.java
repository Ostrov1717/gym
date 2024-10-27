package org.example.services;

import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.example.dao.TraineeRepository;
import org.example.dao.TrainerRepository;
import org.example.dao.TrainingRepository;
import org.example.model.Trainee;
import org.example.model.Trainer;
import org.example.model.Training;
import org.example.model.TrainingType;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TrainingService {

    private final TrainingRepository trainingRepository;
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;


    public TrainingService(TrainingRepository trainingRepository,
                           TraineeRepository traineeRepository,
                           TrainerRepository trainerRepository) {
        this.trainingRepository = trainingRepository;
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
    }

    @Transactional
    public Optional<Training> create(@NonNull Long traineeId, @NonNull Long trainerId, @NonNull String trainingName, @NonNull LocalDateTime trainingDate, @NonNull Duration duration) {
        log.info("Creation of new training: traineeId={}, trainerId={}, name={}, date={}, duration={}",
                traineeId, trainerId, trainingName, trainingDate, duration);
        if (trainingName.isBlank()) {
            log.error("Error: trainingName cannot be blank");
            throw new IllegalArgumentException("Training name cannot be null/blank");
        }
        Trainee trainee = traineeRepository.findById(traineeId)
                .orElseThrow(() -> new RuntimeException("Trainee not found with ID: " + traineeId));

        Trainer trainer = trainerRepository.findById(trainerId)
                .orElseThrow(() -> new RuntimeException("Trainer not found with ID: " + trainerId));

        TrainingType trainingType = trainer.getSpecialization();

        Training training = new Training(trainee, trainer, trainingName, trainingType, trainingDate, duration);
        log.info("Training has been created: name={}, date={}", trainingName, trainingDate);
        trainingRepository.save(training);
        return Optional.of(training);
    }

    @Transactional
    public List<Training> findTrainerList(String trainerUsername, LocalDateTime fromDate, LocalDateTime toDate, String traineeName) {
        log.info("Search trainings for trainer: {}", trainerUsername);
        List<Training> trainings=trainingRepository.findTrainingsByTrainerAndCriteria(trainerUsername, fromDate, toDate, traineeName);
        log.info("Found {} trainings for trainer: {}", trainings.size(), trainerUsername);
        return trainings;
    }

    @Transactional
    public List<Training> findTraineeList(String traineeUsername, LocalDateTime fromDate, LocalDateTime toDate, String trainerName, String trainingType) {
        log.info("Search trainings for trainee: {}", traineeUsername);
        List<Training> trainings=trainingRepository.findTrainingsByTraineeAndCriteria(traineeUsername, fromDate, toDate, trainerName, trainingType);
        log.info("Found {} trainings for trainee: {}", trainings.size(), traineeUsername);
        return trainings;
    }

}
