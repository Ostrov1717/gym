package org.example.services;


import org.example.dao.TrainerRepository;
import org.example.dao.TrainingTypeRepository;
import org.example.model.Trainer;
import org.example.model.TrainingType;
import org.example.model.User;
import org.example.model.enums.TrainingTypeName;
import org.example.profiles.TrainerProfile;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TrainerServiceTests {

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @InjectMocks
    private TrainerService trainerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Create Trainer Test")
    @Order(1)
    void createTrainer_success() {
        String firstName = "John";
        String lastName = "Doe";
        TrainingTypeName trainingTypeName = TrainingTypeName.YOGA;
        TrainingType specialization = new TrainingType();
        when(trainingTypeRepository.findByTrainingType("YOGA")).thenReturn(Optional.of(specialization));
        when(trainerRepository.save(any(Trainer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<TrainerProfile> result = trainerService.create(firstName, lastName, trainingTypeName);

        assertTrue(result.isPresent());
        assertEquals(firstName, result.get().getFirstName());
        assertEquals(lastName, result.get().getLastName());
        verify(trainingTypeRepository, times(1)).findByTrainingType(trainingTypeName.name());
        verify(trainerRepository, times(1)).save(any(Trainer.class));
    }

    @Test
    @DisplayName("Create Trainer failed - training type not found")
    @Order(2)
    void createTrainer_trainingTypeNotFound() {
        String firstName = "John";
        String lastName = "Doe";
        TrainingTypeName trainingTypeName = TrainingTypeName.RESISTANCE;
        when(trainingTypeRepository.findByTrainingType(String.valueOf(trainingTypeName))).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                trainerService.create(firstName, lastName, trainingTypeName)
        );
        assertEquals("Specialization not found", exception.getMessage());
        verify(trainingTypeRepository, times(1)).findByTrainingType(trainingTypeName.name());
        verify(trainerRepository, never()).save(any(Trainer.class));
    }

    @Test
    @DisplayName("Select Trainer Test by username - success")
    @Order(4)
    void findByUsername_success() {

        String username = "john.doe";
        String password = "password123";
        Trainer trainer = new Trainer(null, new User("John", "Doe", username, password, true));

        when(trainerRepository.findByUsernameAndPassword(username, password)).thenReturn(Optional.of(trainer));
        when(trainerRepository.findByUserUsername(username)).thenReturn(Optional.of(trainer));

        Optional<TrainerProfile> result = trainerService.findByUsername(username, password);

        assertTrue(result.isPresent());
        verify(trainerRepository, times(1)).findByUsernameAndPassword(username, password);
    }

    @Test
    @DisplayName("Select Trainer Test by username failed autentification")
    @Order(5)
    void findByUsername_invalidCredentials() {

        String username = "john.doe";
        String password = "wrongpassword";

        when(trainerRepository.findByUsernameAndPassword(username, password)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                trainerService.findByUsername(username, password)
        );
        assertEquals("Invalid username or password", exception.getMessage());
        verify(trainerRepository, times(1)).findByUsernameAndPassword(username, password);
    }

    @Test
    @DisplayName("Update Trainer Test - success")
    @Order(6)
    void updateTrainer_success() {

        String firstName = "Jane";
        String lastName = "Doe";
        String username = "jane.doe";
        String password = "password123";
        TrainingTypeName trainingTypeName = TrainingTypeName.YOGA;
        boolean isActive = true;

        Trainer trainer = new Trainer();
        trainer.setUser(new User("Jane", "Doe", username, password, true));
        TrainingType specialization = new TrainingType();

        when(trainerRepository.findByUsernameAndPassword(username, password)).thenReturn(Optional.of(trainer));
        when(trainerRepository.findByUserUsername(username)).thenReturn(Optional.of(trainer));
        when(trainingTypeRepository.findByTrainingType(trainingTypeName.name())).thenReturn(Optional.of(specialization));
        when(trainerRepository.save(any(Trainer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<TrainerProfile> result = trainerService.update(firstName, lastName, username, password, trainingTypeName, isActive);

        assertTrue(result.isPresent());
        assertEquals(firstName, result.get().getFirstName());
        assertEquals(lastName, result.get().getLastName());
        assertEquals(isActive, result.get().isActive());
        verify(trainerRepository, times(1)).findByUserUsername(username);
        verify(trainingTypeRepository, times(1)).findByTrainingType(trainingTypeName.name());
    }

    @Test
    @DisplayName("Update Trainer failed - trainer not found")
    @Order(7)
    void updateTrainer_trainerNotFound() {
        String username = "nonexistent";
        String password = "password123";

        when(trainerRepository.findByUsernameAndPassword(username, password)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                trainerService.update("Jane", "Doe", username, password, TrainingTypeName.YOGA, true)
        );
        assertEquals("Invalid username or password", exception.getMessage());
        verify(trainerRepository, times(1)).findByUsernameAndPassword(username, password);
        verify(trainingTypeRepository, never()).findByTrainingType(any());
        verify(trainerRepository, never()).save(any(Trainer.class));
    }

}

