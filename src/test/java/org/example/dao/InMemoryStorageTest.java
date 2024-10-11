package org.example.dao;

import org.example.model.Trainee;
import org.example.model.Trainer;
import org.example.model.Training;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryStorageTest {
    private InMemoryStorage storage;

    @BeforeEach
    void setUp() {
        storage = new InMemoryStorage();
    }

    @Test
    @DisplayName("Save Entities Test")
    void saveEntitiesTest() {
        Long id = 1L;
        Trainee trainee = new Trainee();
        Trainer trainer = new Trainer();
        Training training = new Training();

        Trainee savedTrainee = storage.save(Trainee.class, id, trainee);
        Trainer savedTrainer = storage.save(Trainer.class, id, trainer);
        Training savedTraining = storage.save(Training.class, id, training);

        assertSame(trainee,savedTrainee);
        assertSame(trainer,savedTrainer);
        assertSame(training,savedTraining);
    }

    @Test
    @DisplayName("Find by Id Entities Test")
    void findEntitiesByIdTest() {
        Long id = 1L;
        Trainee trainee = new Trainee();
        Trainer trainer = new Trainer();
        Training training = new Training();

        storage.save(Trainee.class, id, trainee);
        storage.save(Trainer.class, id, trainer);
        storage.save(Training.class, id, training);

        assertEquals(trainee, storage.findById(Trainee.class,id));
        assertEquals(trainer, storage.findById(Trainer.class,id));
        assertEquals(training, storage.findById(Training.class,id));
        assertNull(storage.findById(Trainee.class,2L));
        assertNull(storage.findById(Trainer.class,2L));
        assertNull(storage.findById(Trainer.class,2L));
    }

    @Test
    @DisplayName("Delete Entities Test")
    void deleteEntityTest() {
        Long id = 1L;
        Trainee trainee = new Trainee();
        Trainer trainer = new Trainer();
        Training training = new Training();

        storage.save(Trainee.class, id, trainee);
        storage.save(Trainer.class, id, trainer);
        storage.save(Training.class, id, training);

        storage.delete(Trainee.class, id);
        storage.delete(Trainer.class, id);
        storage.delete(Training.class, id);

        assertNull(storage.findById(Trainee.class, id));
        assertNull(storage.findById(Trainer.class, id));
        assertNull(storage.findById(Training.class, id));
    }

    @Test
    @DisplayName("GetAll Entities Test")
    void getAllEntities() {
        Trainee trainee1 = new Trainee();
        Trainee trainee2 = new Trainee();
        storage.save(Trainee.class, 1L, trainee1);
        storage.save(Trainee.class, 2L, trainee2);

        Trainer trainer1 = new Trainer();
        storage.save(Trainer.class,1L,trainer1);

        Map<Long, Trainee> allTrainees = storage.getAll(Trainee.class);
        Map<Long, Trainer> allTrainers = storage.getAll(Trainer.class);
        Map<Long, Training> allTrainings = storage.getAll(Training.class);

        assertEquals(2, allTrainees.size());
        assertEquals(1,allTrainers.size());
        assertTrue(allTrainings.isEmpty());
        assertEquals(trainee1, allTrainees.get(1L));
        assertEquals(trainee2, allTrainees.get(2L));
    }
}
