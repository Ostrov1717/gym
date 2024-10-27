package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.example.config.ProjectConfig;
import org.example.model.Trainer;
import org.example.model.enums.TrainingTypeName;
import org.example.services.TraineeService;
import org.example.services.TrainerService;
import org.example.services.TrainingService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Slf4j
public class App {
    @PersistenceContext
    EntityManager entityManager;

    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        var context = new AnnotationConfigApplicationContext(ProjectConfig.class);
        TrainerService trainerService = context.getBean(TrainerService.class);
        TraineeService traineeService = context.getBean(TraineeService.class);
        TrainingService trainingService = context.getBean(TrainingService.class);
//        1. Create Trainer profile.
        log.info(trainerService.create("Brad", "Pitt", TrainingTypeName.RESISTANCE).toString() + "\n");

//        2. Create Trainee profile.
        log.info(traineeService.create("Tony", "Scott", "France", LocalDate.parse("1975-01-02")).toString() + "\n");
//
//        В базу добавлен (файл src/main/resources/data.sql) Trainee {username="Olga.Kurilenko", password="WRqqRQMsoy"}
//        3. Trainee username and password matching.
        traineeService.authenticate("Olga.Kurilenko", "WRqqRQMsoy");
        log.info("___________________________________________________________________________________" + "\n");

//        В базу добавлен (файл src/main/resources/data.sql) Trainer {username="Monica.Dobs", password="55555"}
//        4. Trainer username and password matching.
        trainerService.authenticate("Monica.Dobs", "55555");

//        5. Select Trainer profile by username.
        log.info(trainerService.findByUsername("Monica.Dobs", "55555").toString() + "\n");

//        6. Select Trainee profile by username.
        log.info(traineeService.findByUsername("Olga.Kurilenko", "WRqqRQMsoy").toString() + "\n");

//        7. Trainee password change.
        traineeService.changePassword("Olga.Kurilenko", "WRqqRQMsoy", "1");
        traineeService.authenticate("Olga.Kurilenko", "1");
        log.info("___________________________________________________________________________________" + "\n");

//        8. Trainer password change.
        trainerService.changePassword("Monica.Dobs", "55555", "2");
        trainerService.authenticate("Monica.Dobs", "2");
        log.info("___________________________________________________________________________________" + "\n");

//        9. Update trainer profile.
        log.info(trainerService.findByUsername("Monica.Dobs", "2").toString());
        log.info(trainerService.update("Monica", "Braun", "Monica.Dobs", "2", TrainingTypeName.ZUMBA, false).toString());
        log.info(trainerService.findByUsername("Monica.Dobs", "2").toString());
        log.info("___________________________________________________________________________________" + "\n");

//        10. Update trainee profile.
        log.info(traineeService.findByUsername("Olga.Kurilenko", "1").toString());
        log.info(traineeService.update("Vasilisa", "Klopotenko", "Olga.Kurilenko", "1", "Lviv", LocalDate.parse("1990-02-05"), false).toString());
        log.info(traineeService.findByUsername("Olga.Kurilenko", "1").toString());
        log.info("___________________________________________________________________________________" + "\n");

//        11. Activate/De-activate trainee.
        traineeService.activate("Olga.Kurilenko", "1");
        log.info(traineeService.findByUsername("Olga.Kurilenko", "1").toString());
        log.info("___________________________________________________________________________________" + "\n");

//        12. Activate/De-activate trainer.
        trainerService.activate("Monica.Dobs", "2");
        log.info(trainerService.findByUsername("Monica.Dobs", "2").toString());
        log.info("___________________________________________________________________________________" + "\n");

//        13. Delete trainee profile by username.
        traineeService.delete("Olga.Kurilenko", "1");
        log.info("___________________________________________________________________________________" + "\n");


//        14. Get Trainee Trainings List by trainee username and criteria (from date, to date, trainer
//                name, training type).
        trainingService.findTraineeList("Tomas.Kuk", LocalDateTime.parse("2020-01-01T00:00"), LocalDateTime.now(), null, null).forEach(System.out::println);
        log.info("___________________________________________________________________________________" + "\n");
//
//        15. Get Trainer Trainings List by trainer username and criteria (from date, to date, trainee
//                name).
        trainingService.findTrainerList("Wallace.Tim",LocalDateTime.parse("2020-01-01T00:00"), LocalDateTime.now(), null).forEach(System.out::println);
        log.info("___________________________________________________________________________________" + "\n");

//        16. Add training.
        log.info(trainingService.create(2L,3L,"Fitness class",LocalDateTime.now(), Duration.ofHours(1)).toString());
        log.info("___________________________________________________________________________________" + "\n");

//        17. Get trainers list that not assigned on trainee by trainee's username.
        List<Trainer> freeTrainers=trainerService.getAvailableTrainers("Tomas.Kuk");
        freeTrainers.forEach(System.out::println);
        log.info("___________________________________________________________________________________" + "\n");

//        18. Update Tranee's trainers list
        traineeService.updateTraineeTrainers("Tomas.Kuk",new HashSet<>(freeTrainers));


        entityManager.close();
        entityManagerFactory.close();

    }
}
