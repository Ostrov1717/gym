package org.example.services;


import org.example.dao.TraineeRepository;
import org.example.dao.TrainerRepository;
import org.example.dao.TrainingRepository;
import org.example.model.*;
import org.example.model.enums.TrainingTypeName;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TrainingServiceTests {
    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @InjectMocks
    private TrainingService trainingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Create Training Test")
    @Order(1)
    void createTraining_success() {

        Long traineeId = 1L;
        Long trainerId = 2L;
        String trainingName = "Yoga Training";
        LocalDateTime trainingDate = LocalDateTime.now();
        Duration duration = Duration.ofHours(2);

        Trainee trainee = new Trainee();
        Trainer trainer = new Trainer();
        trainer.setSpecialization(new TrainingType(TrainingTypeName.YOGA.name()));

        when(traineeRepository.findById(traineeId)).thenReturn(Optional.of(trainee));
        when(trainerRepository.findById(trainerId)).thenReturn(Optional.of(trainer));
        when(trainingRepository.save(any(Training.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Optional<Training> result = trainingService.create(traineeId, trainerId, trainingName, trainingDate, duration);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(trainingName, result.get().getTrainingName());
        verify(traineeRepository, times(1)).findById(traineeId);
        verify(trainerRepository, times(1)).findById(trainerId);
        verify(trainingRepository, times(1)).save(any(Training.class));
    }

    @Test
    @DisplayName("Create Training failed - Trainee not found")
    @Order(2)
    void createTraining_traineeNotFound() {

        Long traineeId = 1L;
        Long trainerId = 2L;
        String trainingName = "Yoga Training";
        LocalDateTime trainingDate = LocalDateTime.now();
        Duration duration = Duration.ofHours(2);

        when(traineeRepository.findById(traineeId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                trainingService.create(traineeId, trainerId, trainingName, trainingDate, duration)
        );
        assertEquals("Trainee not found with ID: " + traineeId, exception.getMessage());
        verify(traineeRepository, times(1)).findById(traineeId);
        verify(trainerRepository, never()).findById(anyLong());
        verify(trainingRepository, never()).save(any(Training.class));
    }

    @Test
    @DisplayName("Create Training failed - Trainer not found")
    @Order(3)
    void createTraining_trainerNotFound() {

        Long traineeId = 1L;
        Long trainerId = 2L;
        String trainingName = "Yoga Training";
        LocalDateTime trainingDate = LocalDateTime.now();
        Duration duration = Duration.ofHours(2);

        Trainee trainee = new Trainee();
        when(traineeRepository.findById(traineeId)).thenReturn(Optional.of(trainee));
        when(trainerRepository.findById(trainerId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                trainingService.create(traineeId, trainerId, trainingName, trainingDate, duration)
        );
        assertEquals("Trainer not found with ID: " + trainerId, exception.getMessage());
        verify(traineeRepository, times(1)).findById(traineeId);
        verify(trainerRepository, times(1)).findById(trainerId);
        verify(trainingRepository, never()).save(any(Training.class));
    }

    @Test
    @DisplayName("Create Training failed - Training name blank")
    @Order(4)
    void createTraining_trainingNameBlank() {
        // Arrange
        Long traineeId = 1L;
        Long trainerId = 2L;
        String trainingName = "";
        LocalDateTime trainingDate = LocalDateTime.now();
        Duration duration = Duration.ofHours(2);

        Trainee trainee = new Trainee();
        Trainer trainer = new Trainer();
        when(traineeRepository.findById(traineeId)).thenReturn(Optional.of(trainee));
        when(trainerRepository.findById(trainerId)).thenReturn(Optional.of(trainer));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                trainingService.create(traineeId, trainerId, trainingName, trainingDate, duration)
        );
        assertEquals("Training name cannot be null/blank", exception.getMessage());
        verify(traineeRepository, times(0)).findById(traineeId);
        verify(trainerRepository, times(0)).findById(trainerId);
        verify(trainingRepository, never()).save(any(Training.class));
    }

    @Test
    @DisplayName("Find trainer list")
    @Order(5)
    void findTrainerList_success() {
        String trainerUsername = "trainer1";
        LocalDateTime fromDate = LocalDateTime.now().minusDays(1);
        LocalDateTime toDate = LocalDateTime.now().plusDays(1);
        Training training = new Training();
        training.setTrainee(new Trainee(new User("Olga", "Kurilenko", "Olga.Kurilenko","WRqqRQMsoy",true),
                "California", LocalDate.parse("1980-01-01")));
        training.setTrainer(new Trainer(new TrainingType(TrainingTypeName.STRETCHING.name()),
                new User("Monica", "Dobs", "55555", "Monica.Dobs",true)));
        training.setTrainingName("Yoga class");
        training.setTrainingDate(LocalDateTime.now());
        training.setTrainingDuration(Duration.ofHours(2));

        when(trainingRepository.findTrainingsByTrainerAndCriteria(trainerUsername, fromDate, toDate, null))
                .thenReturn(Collections.singletonList(training));

        List<Training> result = trainingService.findTrainerList(trainerUsername, fromDate, toDate, null);

        assertEquals(1, result.size());
        assertEquals(training, result.get(0));
        verify(trainingRepository, times(1)).findTrainingsByTrainerAndCriteria(trainerUsername, fromDate, toDate, null);
    }

    @Test
    @DisplayName("Find trainee list")
    @Order(6)
    void findTraineeList_success() {
        String traineeUsername = "trainee1";
        LocalDateTime fromDate = LocalDateTime.now().minusDays(1);
        LocalDateTime toDate = LocalDateTime.now().plusDays(1);
        Training training = new Training();
        training.setTrainee(new Trainee(new User("Olga", "Kurilenko", "Olga.Kurilenko", "WRqqRQMsoy", true),
                "California", LocalDate.parse("1980-01-01")));
        training.setTrainer(new Trainer(new TrainingType(TrainingTypeName.STRETCHING.name()),
                new User("Monica", "Dobs", "55555", "Monica.Dobs", true)));
        training.setTrainingName("Yoga class");
        training.setTrainingDate(LocalDateTime.now());
        training.setTrainingDuration(Duration.ofHours(2));

        when(trainingRepository.findTrainingsByTraineeAndCriteria(traineeUsername, fromDate, toDate, null, null))
                .thenReturn(Collections.singletonList(training));

        List<Training> result = trainingService.findTraineeList(traineeUsername, fromDate, toDate, null, null);

        assertEquals(1, result.size());
        assertEquals(training, result.get(0));
        verify(trainingRepository, times(1)).findTrainingsByTraineeAndCriteria(traineeUsername, fromDate, toDate, null, null);
    }
}
