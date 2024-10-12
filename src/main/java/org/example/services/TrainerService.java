package org.example.services;

import jakarta.annotation.PostConstruct;
import org.example.dao.GenericDAO;
import org.example.model.Trainer;
import org.example.model.TrainingType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TrainerService {
    @Autowired
    private GenericDAO<Trainer> dao;
    private long nextId;
    private static final Logger logger = LoggerFactory.getLogger(TrainerService.class);

    public Trainer create(String firstName, String lastName, TrainingType specialization){
        logger.info("Creation of new Trainer: {} {}", firstName, lastName);
        if(firstName==null||firstName.isBlank()||lastName==null||lastName.isBlank()) {
            logger.error("Impossible to create new Trainer: blank/null firstname or lastname");
            throw new IllegalArgumentException("Trainer without firstname and lastname cannot be created !");
        }
        Trainer trainer=new Trainer();
        long id=nextId++;
        trainer.setUserId(id);
        trainer.setFirstName(firstName);
        trainer.setLastName(lastName);
        trainer.setActive(true);
        String userName=firstName+"."+lastName;
        long alingments=dao.getAll().values().stream()
                .filter(un->un.getFirstName().equals(firstName)&&un.getLastName().equals(lastName))
                .count();
        if(alingments>0){
           userName+=alingments;
        }
        trainer.setUsername(userName);
        String password=trainer.madePassword();
        trainer.setPassword(password);
        trainer.setSpecialization(specialization);
        logger.info("Trainer has been created with Id: {}, username: {}", id, userName);
        return dao.save(trainer,id);
    }
    public Optional<Trainer> selectById(long id){
        logger.info("Search Trainer by Id: {}", id);
        return Optional.ofNullable(dao.findById(id));
    }
    public Optional<Trainer> selectByUsername(String username){
        logger.info("Search Trainer by username: {}", username);
        return getAll().stream().filter(el->el.getUsername().equals(username)).findFirst();
    }

    public void update (String firstName, String lastName,  String userName, TrainingType specialization,boolean isActive) throws IllegalArgumentException{
        logger.info("Updating Trainer's data with username: {}", userName);
        Trainer trainer=selectByUsername(userName).orElseThrow(()->new IllegalArgumentException("Trainer with username: " + userName + " not found."));
        trainer.setFirstName(firstName);
        trainer.setLastName(lastName);
        trainer.setSpecialization(specialization);
        trainer.setActive(isActive);
        logger.info("Trainer's data with username: {} has been updated", userName);
    }
    public List<Trainer> getAll(){
        logger.info("Getting all Trainers");
        return new ArrayList<>(dao.getAll().values());
    }

    @PostConstruct
    public void init() {
        logger.info("Initialization of TrainerService and definition of next Trainer Id");
        this.nextId = dao.getAll().keySet().stream().max(Long::compareTo).orElse(0L)+1;
        logger.info("next Trainer Id is {}\n", nextId);
    }
}
