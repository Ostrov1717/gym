package org.example.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.example.dao.GenericDAO;
import org.example.model.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@Slf4j
public class TraineeService {
    private GenericDAO<Trainee> dao;
    private long nextId;

    @Autowired
    public void setDao(GenericDAO<Trainee> dao) {
        this.dao = dao;
        log.debug("Initialization of TraineeService and definition of next Trainee Id");
        this.nextId = dao.getAll().keySet().stream().max(Long::compareTo).orElse(0L) + 1;
        log.debug("next Trainee Id is {}\n", nextId);
    }

    public Trainee create(@NonNull String firstName, @NonNull String lastName, String address, LocalDate dateOfBirth) {
        log.info("Creation of new Trainee: {} {}", firstName, lastName);
        if (firstName.isBlank() || lastName.isBlank()) {
            log.error("Impossible to create new Trainee: blank firstname or lastname");
            throw new IllegalArgumentException("Trainee without firstname and lastname cannot be created !");
        }
        long id = nextId++;
        String userName = getUsername(firstName, lastName);
        String password = madePassword();
        Trainee trainee = new Trainee(id,firstName,lastName,userName,password,true,address,dateOfBirth);
        log.info("Trainee has been created with Id: {}, username: {}", id, userName);
        return dao.save(trainee, id);
    }

    public Optional<Trainee> selectById(long id) {
        log.info("Search Trainee by Id: {}", id);
        return Optional.ofNullable(dao.findById(id));
    }

    public Optional<Trainee> selectByUsername(String username) {
        log.info("Search Trainee by username: {}", username);
        return getAll().stream().filter(el -> el.getUsername().equals(username)).findFirst();
    }

    public void update(String firstName, String lastName, String userName, String address, LocalDate dateOfBirth, boolean isActive) throws IllegalArgumentException {
        log.info("Updating Trainee's data with username: {}", userName);
        Trainee trainee = selectByUsername(userName).orElseThrow(() -> new IllegalArgumentException("Trainee with username: " + userName + " not found."));
        trainee.setFirstName(firstName);
        trainee.setLastName(lastName);
        trainee.setAddress(address);
        trainee.setDateOfBirth(dateOfBirth);
        trainee.setActive(isActive);
        log.info("Trainee's data with username: {} has been updated", userName);
    }

    public void delete(String userName) {
        log.info("Deleting Trainee with username: {}", userName);
        Trainee trainee = selectByUsername(userName).orElseThrow(() -> new IllegalArgumentException("Trainee with username: " + userName + " not found."));
        log.info("Trainee with username: {} successfully deleted", userName);
        dao.delete(trainee.getUserId());
    }

    public List<Trainee> getAll() {
        log.info("Getting all Trainees");
        return new ArrayList<>(dao.getAll().values());
    }

    private String getUsername(String firstName, String lastName) {
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
