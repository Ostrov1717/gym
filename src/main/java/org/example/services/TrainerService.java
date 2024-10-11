package org.example.services;

import jakarta.annotation.PostConstruct;
import org.example.dao.GenericDAO;
import org.example.model.Trainer;
import org.example.model.TrainingType;
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

    public Trainer create(String firstName, String lastName, TrainingType specialization){
        if(firstName==null||firstName.isBlank()||lastName==null||lastName.isBlank()) throw new IllegalArgumentException("Trainer without firstname and lastname cannot be created !");
        Trainer trainer=new Trainer();
        long id=++nextId;
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
        return dao.save(trainer,id);
    }
    public Optional<Trainer> selectById(long id){
        return Optional.ofNullable(dao.findById(id));
    }
    public Optional<Trainer> selectByUsername(String username){
        return getAll().stream().filter(el->el.getUsername().equals(username)).findFirst();
    }

    public void update (String firstName, String lastName,  String userName, TrainingType specialization,boolean isActive) throws IllegalArgumentException{
        Trainer trainer=selectByUsername(userName).orElseThrow(()->new IllegalArgumentException("Trainer with username: " + userName + " not found."));
        trainer.setFirstName(firstName);
        trainer.setLastName(lastName);
        trainer.setSpecialization(specialization);
        trainer.setActive(isActive);
    }
    public List<Trainer> getAll(){
        return new ArrayList<>(dao.getAll().values());
    }

    @PostConstruct
    public void init() {
        this.nextId = dao.getAll().keySet().stream().max(Long::compareTo).orElse(0L);
    }
}
