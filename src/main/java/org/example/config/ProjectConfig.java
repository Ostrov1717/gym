package org.example.config;

import org.example.dao.GenericDAO;
import org.example.dao.Storage;
import org.example.model.Trainee;
import org.example.model.Trainer;
import org.example.model.Training;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = "org.example")
@PropertySource("classpath:application.properties")
public class ProjectConfig {

    @Bean
    public GenericDAO<Trainee> traineeDAO(Storage storage) {
        return new GenericDAO<>(storage, Trainee.class);
    }

    @Bean
    public GenericDAO<Trainer> trainerDAO(Storage storage) {
        return new GenericDAO<>(storage, Trainer.class);
    }

    @Bean
    public GenericDAO<Training> trainingDAO(Storage storage) {
        return new GenericDAO<>(storage, Training.class);
    }

}
