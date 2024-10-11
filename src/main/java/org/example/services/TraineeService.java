package org.example.services;

import jakarta.annotation.PostConstruct;
import org.example.dao.GenericDAO;
import org.example.model.Trainee;
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

    public Trainee create(String firstName, String lastName, String address, LocalDate dateOfBirth){
        if(firstName==null||firstName.isBlank()||lastName==null||lastName.isBlank()) throw new IllegalArgumentException("Trainee without firstname and lastname cannot be created !");
        Trainee trainee=new Trainee();
        long id=++nextId;
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
        return dao.save(trainee,id);
    }
    public Optional<Trainee> selectById(long id){
        return Optional.ofNullable(dao.findById(id));
    }
    public Optional<Trainee> selectByUsername(String username){
        return getAll().stream().filter(el->el.getUsername().equals(username)).findFirst();
    }
    public void update(String firstName, String lastName,  String userName, String address, LocalDate dateOfBirth ,boolean isActive) throws IllegalArgumentException{
        Trainee trainee=selectByUsername(userName).orElseThrow(()->new IllegalArgumentException("Trainee with username: " + userName + " not found."));
                    trainee.setFirstName(firstName);
                    trainee.setLastName(lastName);
                    trainee.setAddress(address);
                    trainee.setDateOfBirth(dateOfBirth);
                    trainee.setActive(isActive);
    }

    public void delete(String userName){
        Trainee trainee=selectByUsername(userName).orElseThrow(() -> new IllegalArgumentException("Trainee with username: " + userName + " not found."));
        dao.delete(trainee.getUserId());
    }

    public List<Trainee> getAll(){
        return new ArrayList<>(dao.getAll().values());
    }

    @PostConstruct
    public void init() {
        this.nextId = dao.getAll().keySet().stream().max(Long::compareTo).orElse(0L);
    }
}
