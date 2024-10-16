package org.example.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Trainee;
import org.example.model.Trainer;
import org.example.model.Training;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryStorage implements Storage {

    private final Map<Class<?>, Map<Long, Object>> storage = new HashMap<>();

    private String initFilePaths;

    private Map<Long, Object> getNamespace(Class<?> entityClass) {
        return storage.computeIfAbsent(entityClass, k -> new HashMap<>());
    }

    @Override
    public <T> T save(Class<T> entityClass, Long id, T entity) {
        log.info("Saving entity of type {} with Id: {}", entityClass.getSimpleName(), id);
        Map<Long, Object> namespace = getNamespace(entityClass);
        namespace.put(id, entity);
        log.debug("Saved entity: {}", entity);
        return entity;
    }

    @Override
    public <T> T findById(Class<T> entityClass, Long id) {
        log.info("Searching entity of type {} with Id: {}", entityClass.getSimpleName(), id);
        Map<Long, Object> namespace = getNamespace(entityClass);
        T entity = entityClass.cast(namespace.get(id));
        if (entity != null) {
            log.debug("Found entity: {}", entity);
        } else {
            log.warn("Entity of type {} with Id: {} not found", entityClass.getSimpleName(), id);
        }
        return entity;
    }

    @Override
    public <T> void delete(Class<T> entityClass, Long id) {
        log.info("Deleting entity of type {} with Id: {}", entityClass.getSimpleName(), id);
        Map<Long, Object> namespace = getNamespace(entityClass);
        if (namespace.remove(id) != null) {
            log.debug("Entity with Id: {} successfully deleted", id);
        } else {
            log.warn("Failed to delete. Entity of type {} with Id: {} not found", entityClass.getSimpleName(), id);
        }
    }

    @Override
    public <T> Map<Long, T> getAll(Class<T> entityClass) {
        log.info("Getting all entities of type {}", entityClass.getSimpleName());
        Map<Long, Object> namespace = getNamespace(entityClass);
        Map<Long, T> result = new HashMap<>();
        for (Map.Entry<Long, Object> entry : namespace.entrySet()) {
            result.put(entry.getKey(), entityClass.cast(entry.getValue()));
        }
        log.info("Got {} entities of type {}", result.size(), entityClass.getSimpleName());
        return result;
    }

    @Value("${storage.files}")
    public void setInitFilePaths(String initFilePaths) {
        this.initFilePaths = initFilePaths;
    }

    @PostConstruct
    public void init() {
        String[] paths = initFilePaths.split(",");
        List<Trainee> traineeList = readJson(Trainee.class, paths[0]);
        List<Trainer> trainerList = readJson(Trainer.class, paths[1]);
        List<Training> trainingList = readJson(Training.class, paths[2]);
        traineeList.forEach(trainee -> save(Trainee.class, trainee.getUserId(), trainee));
        trainerList.forEach(trainer -> save(Trainer.class, trainer.getUserId(), trainer));
        trainingList.forEach(training -> save(Training.class, training.getTrainingId(), training));
    }

    private <T> List<T> readJson(Class<T> entityClass, String filePath) {
        log.info("Initializing storage from file: {}", filePath);
        File jsonFile = new File(filePath);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        List<T> entities = new ArrayList<>();
        try {
            entities = objectMapper.readValue(jsonFile, objectMapper.getTypeFactory().constructCollectionType(List.class, entityClass));
        } catch (IOException e) {
            log.error("Failed to initialize storage from file: {}", filePath, e);
        }
        log.info("Storage {} successfully initialized from file: {}\n", entityClass, filePath);
        return entities;
    }

}
