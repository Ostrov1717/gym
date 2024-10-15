package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.config.ProjectConfig;
import org.example.services.Facade;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDateTime;

@Slf4j
public class App {
    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(ProjectConfig.class);

        Facade facade = context.getBean(Facade.class);
        LocalDateTime dataFrom = LocalDateTime.of(2024, 10, 1, 1, 0);
        LocalDateTime dataTo = LocalDateTime.of(2024, 10, 10, 1, 0);
        log.info(facade.getTraineeTrainingList("tomas.kuk", dataFrom, dataTo));
        log.info(facade.getTrainerTrainingList("Tim.Wallace", dataFrom, dataTo));
    }
}
