package org.example.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.example.dao.GenericDAO;
import org.example.model.Trainer;
import org.example.model.TrainingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class TrainerService {

    private GenericDAO<Trainer> dao;
    private long nextId;

    @Autowired
    public void setDao(GenericDAO<Trainer> dao) {
        this.dao = dao;
        log.info("Initialization of TrainerService and definition of next Trainer Id");
        this.nextId = dao.getAll().keySet().stream().max(Long::compareTo).orElse(0L) + 1;
        log.info("next Trainer Id is {}\n", nextId);
    }

    public Trainer create(@NonNull String firstName, @NonNull String lastName, TrainingType specialization) {
        log.info("Creation of new Trainer: {} {}", firstName, lastName);
        if (firstName.isBlank() || lastName.isBlank()) {
            log.error("Impossible to create new Trainer: blank firstname or lastname");
            throw new IllegalArgumentException("Trainer without firstname and lastname cannot be created !");
        }
        long id = nextId++;
        String password = madePassword();
        String userName = madeUserName(firstName, lastName);
        Trainer trainer = new Trainer(id, firstName, lastName, userName, password, true, specialization);
        log.info("Trainer has been created with Id: {}, username: {}", id, userName);
        return dao.save(trainer, id);
    }

    public Optional<Trainer> selectById(long id) {
        log.info("Search Trainer by Id: {}", id);
        return Optional.ofNullable(dao.findById(id));
    }

    public Optional<Trainer> selectByUsername(String username) {
        log.info("Search Trainer by username: {}", username);
        return getAll().stream().filter(el -> el.getUsername().equals(username)).findFirst();
    }

    public void update(String firstName, String lastName, String userName, TrainingType specialization, boolean isActive) throws IllegalArgumentException {
        log.info("Updating Trainer's data with username: {}", userName);
        Trainer trainer = selectByUsername(userName).orElseThrow(() -> new IllegalArgumentException("Trainer with username: " + userName + " not found."));
        trainer.setFirstName(firstName);
        trainer.setLastName(lastName);
        trainer.setSpecialization(specialization);
        trainer.setActive(isActive);
        log.info("Trainer's data with username: {} has been updated", userName);
    }

    public List<Trainer> getAll() {
        log.info("Getting all Trainers");
        return new ArrayList<>(dao.getAll().values());
    }

    private String madeUserName(String firstName, String lastName) {
        String userName = firstName + "." + lastName;
        long alingments = dao.getAll().values().stream()
                .filter(un -> un.getFirstName().equals(firstName) && un.getLastName().equals(lastName))
                .count();
        if (alingments > 0) {
            userName += alingments;
        }
        return userName;
    }

    private String madePassword() {
        Random random = new Random();
        return Stream.generate(() -> (char) random.nextInt(33, 122))
                .filter(Character::isLetter)
                .limit(10)
                .map(String::valueOf)
                .collect(Collectors.joining());
    }
}
