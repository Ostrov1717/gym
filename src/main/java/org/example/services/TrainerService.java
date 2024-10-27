package org.example.services;

import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.example.dao.TrainerRepository;
import org.example.dao.TrainingTypeRepository;
import org.example.model.Trainer;
import org.example.model.TrainingType;
import org.example.model.User;
import org.example.model.enums.TrainingTypeName;
import org.example.profiles.TrainerMapper;
import org.example.profiles.TrainerProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TrainerService {
    private final TrainerRepository trainerRepository;
    private final TrainingTypeRepository trainingTypeRepository;

    @Autowired
    public TrainerService(TrainerRepository trainerRepository, TrainingTypeRepository trainingTypeRepository) {
        this.trainerRepository = trainerRepository;
        this.trainingTypeRepository = trainingTypeRepository;
    }

    @Transactional
    public Optional<TrainerProfile> create(@NonNull String firstName, @NonNull String lastName, TrainingTypeName trainingTypeName) {
        validateNames(firstName, lastName);
        log.info("Creating a new Trainer: {} {}", firstName, lastName);
        TrainingType specialization = findTrainingType(trainingTypeName);
        Trainer trainer = new Trainer(specialization, new User(firstName, lastName, generateUserName(firstName, lastName), generatePassword(), false));
        trainerRepository.save(trainer);
        log.info("Trainer created with username: {}", trainer.getUser().getUsername());
        return Optional.of(TrainerMapper.toProfile(trainer));
    }

    @Transactional
    public Optional<TrainerProfile> findByUsername(String username, String password) {
        authenticate(username, password);
        log.info("Searching Trainer by username: {}", username);
        Trainer trainer = findTrainerByUsername(username);
        return Optional.of(TrainerMapper.toProfile(trainer));
    }

    @Transactional
    public void authenticate(String username, String password) {
        log.info("Attempting to authenticate Trainer with username: {}", username);
        boolean authenticated = trainerRepository.findByUsernameAndPassword(username, password).isPresent();
        if (!authenticated) {
            log.warn("Authentication failed for username: {}", username);
            throw new IllegalArgumentException("Invalid username or password");
        }
        log.info("Authentication successful for username: {}", username);
    }

    @Transactional
    private Trainer findTrainerByUsername(String username) {
        return trainerRepository.findByUserUsername(username).orElseThrow(() -> new IllegalArgumentException("Trainer with username: " + username + " not found."));
    }

    @Transactional
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        authenticate(username, oldPassword);
        log.info("Changing password of Trainer with username: {}", username);
        Trainer trainer = findTrainerByUsername(username);
        trainer.getUser().setPassword(newPassword);
        log.info("Password successfully changed");
        return true;

    }

    @Transactional
    public Optional<TrainerProfile> update(String firstName, String lastName, String username, String password, TrainingTypeName trainingTypeName, boolean isActive) {
        authenticate(username, password);
        log.info("Updating Trainer's data with username: {}", username);
        Trainer trainer = findTrainerByUsername(username);
        TrainingType specialization = findTrainingType(trainingTypeName);
        trainer.getUser().setFirstName(firstName);
        trainer.getUser().setLastName(lastName);
        trainer.setSpecialization(specialization);
        trainer.getUser().setActive(isActive);
        log.info("Trainer's data with username: {} has been updated", username);
        return Optional.of(TrainerMapper.toProfile(trainer));
    }
    @Transactional
    public boolean activate(String username, String password) {
        authenticate(username, password);
        log.info("Activating Trainer with username: {}", username);
        boolean activated = updateActiveStatus(username, true);
        log.info("Trainer with username: {} activated", username);
        return activated;
    }

    @Transactional
    private boolean updateActiveStatus(String username, boolean isActive) {
        Trainer trainer = findTrainerByUsername(username);
        trainer.getUser().setActive(isActive);
        return true;
    }
    @Transactional
    public boolean deactivate(String username, String password) {
        authenticate(username, password);
        log.info("Deactivating Trainer with username: {}", username);
        boolean deactivated = updateActiveStatus(username, false);
        log.info("Trainer with username: {} deactivated", username);
        return true;
    }
    public List<Trainer> getAvailableTrainers(String traineeUsername) {
        log.info("Search trainers  that not assigned on trainee: {}", traineeUsername);
        List<Trainer> trainers=trainerRepository.findTrainersNotAssignedToTraineeByUsername(traineeUsername);
        log.info("Found {} trainers for trainee: {}", trainers.size(), traineeUsername);
        return trainers;
    }

    private void validateNames(String firstName, String lastName) {
        if (firstName.isBlank() || lastName.isBlank()) {
            log.error("Trainer creation failed: blank firstname or lastname");
            throw new IllegalArgumentException("Trainer without firstname and lastname cannot be created!");
        }
    }

    private TrainingType findTrainingType(TrainingTypeName trainingTypeName) {
        return trainingTypeRepository.findByTrainingType(trainingTypeName.name())
                .orElseThrow(() -> new IllegalArgumentException("Specialization not found"));
    }

    private String generateUserName(String firstName, String lastName) {
        String baseUserName = firstName + "." + lastName;
        long count = trainerRepository.findAll().stream()
                .filter(tr -> tr.getUser().getFirstName().equals(firstName) && tr.getUser().getLastName().equals(lastName))
                .count();
        return count > 0 ? baseUserName + count : baseUserName;
    }

    private String generatePassword() {
        Random random = new Random();
        return random.ints(33, 122)
                .filter(Character::isLetter)
                .limit(10)
                .mapToObj(c -> String.valueOf((char) c))
                .collect(Collectors.joining());
    }

    @Transactional
    public Optional<TrainerProfile> findById(Long id) {
        Trainer trainer = trainerRepository.findById(id).orElseThrow(() -> new RuntimeException("Trainee not found"));
        return Optional.of(TrainerMapper.toProfile(trainer));
    }

}
