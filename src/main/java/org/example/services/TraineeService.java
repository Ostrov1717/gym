package org.example.services;

import jakarta.annotation.PostConstruct;
import org.example.dao.GenericDAO;
import org.example.model.Trainee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class TraineeService {
    @Autowired
    private GenericDAO<Trainee> dao;
    private long nextId;

    private static final Logger logger = LoggerFactory.getLogger(TraineeService.class);

    public Trainee create(String firstName, String lastName, String address, LocalDate dateOfBirth){
        logger.info("Creation of new Trainee: {} {}", firstName, lastName);
        if(firstName==null||firstName.isBlank()||lastName==null||lastName.isBlank()) {
            logger.error("Impossible to create new Trainee: blank/null firstname or lastname");
            throw new IllegalArgumentException("Trainee without firstname and lastname cannot be created !");
        }
        Trainee trainee=new Trainee();
        long id=nextId++;
        trainee.setUserId(id);
        trainee.setFirstName(firstName);
        trainee.setLastName(lastName);
        trainee.setActive(true);
        String userName=firstName+"."+lastName;
        long alingments=dao.getAll().values().stream()
                .filter(un->un.getFirstName().equals(firstName)&&un.getLastName().equals(lastName))
                .count();
        if(alingments>0){
            userName+=alingments;
        }
        trainee.setUsername(userName);
        String password=trainee.madePassword();
        trainee.setPassword(password);
        trainee.setAddress(address);
        trainee.setDateOfBirth(dateOfBirth);
        logger.info("Trainee has been created with Id: {}, username: {}", id, userName);
        return dao.save(trainee,id);
    }
    public Optional<Trainee> selectById(long id){
        logger.info("Search Trainee by Id: {}", id);
        return Optional.ofNullable(dao.findById(id));
    }
    public Optional<Trainee> selectByUsername(String username){
        logger.info("Search Trainee by username: {}", username);
        return getAll().stream().filter(el->el.getUsername().equals(username)).findFirst();
    }
    public void update(String firstName, String lastName,  String userName, String address, LocalDate dateOfBirth ,boolean isActive) throws IllegalArgumentException{
        logger.info("Updating Trainee's data with username: {}", userName);
        Trainee trainee=selectByUsername(userName).orElseThrow(()->new IllegalArgumentException("Trainee with username: " + userName + " not found."));
                    trainee.setFirstName(firstName);
                    trainee.setLastName(lastName);
                    trainee.setAddress(address);
                    trainee.setDateOfBirth(dateOfBirth);
                    trainee.setActive(isActive);
        logger.info("Trainee's data with username: {} has been updated", userName);
    }

    public void delete(String userName){
        logger.info("Deleting Trainee with username: {}", userName);
        Trainee trainee=selectByUsername(userName).orElseThrow(() -> new IllegalArgumentException("Trainee with username: " + userName + " not found."));
        logger.info("Trainee with username: {} successfully deleted", userName);
        dao.delete(trainee.getUserId());
    }

    public List<Trainee> getAll(){
        logger.info("Getting all Trainees");
        return new ArrayList<>(dao.getAll().values());
    }

    @PostConstruct
    public void init() {
        logger.info("Initialization of TraineeService and definition of next Trainee Id");
        this.nextId = dao.getAll().keySet().stream().max(Long::compareTo).orElse(0L)+1;
        logger.info("next Trainee Id is {}\n", nextId);
    }
}
