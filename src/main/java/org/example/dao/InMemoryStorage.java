package org.example.dao;

import jakarta.annotation.PostConstruct;
import org.example.model.Trainee;
import org.example.model.Trainer;
import org.example.model.Training;
import org.example.model.TrainingType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryStorage implements Storage {

        private final Map<Class<?>, Map<Long, Object>> storage = new HashMap<>();

        private String initFilePath;

        // Метод для получения карты по типу сущности
        private Map<Long, Object> getNamespace(Class<?> entityClass) {
            return storage.computeIfAbsent(entityClass, k -> new HashMap<>());
        }

        // Сохранение объекта в конкретное пространство имён
        @Override
        public <T> T save(Class<T> entityClass, Long id, T entity) {
            Map<Long, Object> namespace = getNamespace(entityClass);
            return entityClass.cast(namespace.put(id, entity));
        }

        // Получение объекта по id и типу сущности
        @Override
        public <T> T findById(Class<T> entityClass, Long id) {
            Map<Long, Object> namespace = getNamespace(entityClass);
            return entityClass.cast(namespace.get(id));
        }

        // Удаление объекта по id и типу сущности
        @Override
        public <T> void delete(Class<T> entityClass, Long id) {
            Map<Long, Object> namespace = getNamespace(entityClass);
            namespace.remove(id);
        }

        // Получение всех объектов конкретного типа
        @Override
        public <T> Map<Long, T> getAll(Class<T> entityClass) {
            Map<Long, Object> namespace = getNamespace(entityClass);
            Map<Long, T> result = new HashMap<>();
            for (Map.Entry<Long, Object> entry : namespace.entrySet()) {
                result.put(entry.getKey(), entityClass.cast(entry.getValue()));
            }
            return result;
        }
    @Value("${storage.init.file}")
    public void setInitFilePath(String initFilePath) {
        this.initFilePath = initFilePath;
    }
    @PostConstruct
    public void init(){
        System.out.println(initFilePath);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(initFilePath)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (line.startsWith("Trainer")) {
                    long id = Long.parseLong(parts[1]);
                    String firstName = parts[2];
                    String lastName = parts[3];
                    String username = parts[4];
                    String password=parts[5];
                    boolean isActive=Boolean.parseBoolean(parts[6]);
                    TrainingType specialization = TrainingType.valueOf(parts[7]);
                    Trainer trainer = new Trainer(id,firstName,lastName,username,password,isActive,specialization);
                    this.save(Trainer.class,id, trainer);
                } else if (line.startsWith("Trainee")) {
                    long id = Long.parseLong(parts[1]);
                    boolean isActive=Boolean.parseBoolean(parts[6]);
                    LocalDate dateOfBirth=LocalDate.parse(parts[8]);
                    Trainee trainee = new Trainee(id,parts[2],parts[3],parts[4],parts[5],isActive,parts[7],dateOfBirth);
                    this.save(Trainee.class,id, trainee);
                } else if (line.startsWith("Training")) {
                    long id = Long.parseLong(parts[1]);
                    long traineeId = Long.parseLong(parts[2]);
                    long trainerId = Long.parseLong(parts[3]);
                    String name=parts[4];
                    TrainingType type = TrainingType.valueOf(parts[5]);
                    LocalDateTime date=LocalDateTime.parse(parts[6]);
                    Duration duration=Duration.parse(parts[7]);
                    Training training = new Training(id,traineeId,trainerId,name,type,date,duration);
                    this.save(Training.class,id,training);
                } else {
                    throw new IllegalArgumentException("Unknown type: " + parts[0]);
                }
            }
        } catch (IOException|IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}
