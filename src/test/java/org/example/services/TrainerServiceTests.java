package org.example.services;

import org.example.dao.GenericDAO;
import org.example.model.Trainer;
import org.example.model.TrainingType;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TrainerServiceTests {
    @Mock
    private GenericDAO<Trainer> dao;

    @InjectMocks
    private TrainerService trainerService;

    private Map<Long, Trainer> trainerMap;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.trainerMap = new HashMap<>();
    }

    @Test
    @DisplayName("Create Trainer Test")
    @Order(1)
    void createTrainerTest() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        TrainingType specialization = TrainingType.ZUMBA;

        when(dao.save(any(Trainer.class), anyLong())).thenAnswer(invocation -> {
            Trainer trainer = invocation.getArgument(0);
            Long id = invocation.getArgument(1);
            trainerMap.put(id, trainer);
            return trainer;
        });
        when(dao.getAll()).thenReturn(trainerMap);

        //Act
        Trainer firstTrainer = trainerService.create(firstName, lastName, specialization);

        //Assert
        assertNotNull(firstTrainer);
        assertEquals(firstName + "." + lastName, firstTrainer.getUsername());
        assertEquals(10, firstTrainer.getPassword().length());
        assertEquals(firstTrainer, trainerMap.get(firstTrainer.getUserId()));

        Trainer secondTrainer = trainerService.create(firstName, lastName, TrainingType.FITNESS);
        assertNotNull(secondTrainer);
        assertEquals("John.Doe1", trainerMap.get(secondTrainer.getUserId()).getUsername());
        assertEquals(TrainingType.FITNESS, trainerMap.get(secondTrainer.getUserId()).getSpecialization());

        Trainer thirdTrainer = trainerService.create(firstName, lastName, null);
        assertNotNull(secondTrainer);
        assertEquals("John.Doe2", trainerMap.get(thirdTrainer.getUserId()).getUsername());

        assertThrows(NullPointerException.class, () -> trainerService.create(null, "Von", TrainingType.YOGA));
        assertThrows(NullPointerException.class, () -> trainerService.create("Vince", null, TrainingType.RESISTANCE));

        verify(dao, times(3)).save(any(Trainer.class), anyLong());
    }

    @Test
    @DisplayName("Select Trainer Test by Id - success")
    @Order(2)
    void selectByIdTest() {

        // Arrange
        long id = 1L;
        Trainer trainer = new Trainer(1, "John", "Doe", "John.Doe", "udfdhjhgfg", true, TrainingType.RESISTANCE);
        when(dao.findById(id)).thenReturn(trainer);

        // Act
        Optional<Trainer> result = trainerService.selectById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(trainer, result.get());
        verify(dao, times(1)).findById(id);
    }

    @Test
    @DisplayName("Select Trainer Test by Id - not found")
    @Order(3)
    void selectById_notFound() {
        // Arrange
        long id = 1L;
        when(dao.findById(id)).thenReturn(null);

        // Act
        Optional<Trainer> result = trainerService.selectById(id);

        // Assert
        assertFalse(result.isPresent());
        verify(dao, times(1)).findById(id);
    }

    @Test
    @DisplayName("Select Trainer Test by username - success")
    @Order(4)
    void selectByUsername_found() {
        // Arrange
        String username = "john.doe";
        Trainer trainer1 = new Trainer();
        trainer1.setUsername(username);
        trainerMap.put(1L, trainer1);
        Trainer trainee2 = new Trainer();
        trainee2.setUsername("another.username");
        trainerMap.put(2L, trainee2);
        when(dao.getAll()).thenReturn(trainerMap);

        // Act
        Optional<Trainer> result = trainerService.selectByUsername(username);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(trainer1, result.get());
        verify(dao, times(2)).getAll();
    }

    @Test
    @DisplayName("Select Trainer Test by username - not found")
    @Order(5)
    void selectByUsername_notFound() {
        // Arrange
        String username = "john.doe";
        when(dao.getAll()).thenReturn(new HashMap<>());

        // Act
        Optional<Trainer> result = trainerService.selectByUsername(username);

        // Assert
        assertFalse(result.isPresent());
        verify(dao, times(2)).getAll();
    }

    @Test
    @DisplayName("Update Trainer Test - success")
    @Order(6)
    void updateTrainerTest_success() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        String username = "John.Doe";
        TrainingType specialization = TrainingType.STRETCHING;
        Trainer trainer = new Trainer();
        trainer.setUsername(username);
        when(dao.getAll()).thenReturn(Map.of(1L, trainer));

        // Act
        trainerService.update(firstName, lastName, username, specialization, true);

        // Assert
        assertEquals(firstName, trainer.getFirstName());
        assertEquals(lastName, trainer.getLastName());
        assertEquals(specialization, trainer.getSpecialization());
        assertTrue(trainer.isActive());
        verify(dao, times(2)).getAll();
    }

    @Test
    @DisplayName("Update Trainer Test - not found")
    @Order(7)
    void updateTraineeTest_notFound() {
        // Arrange
        String username = "john.doe";
        when(dao.getAll()).thenReturn(new HashMap<>());

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                trainerService.update("John", "Doe", username, TrainingType.FITNESS, true)
        );
        assertEquals("Trainer with username: " + username + " not found.", exception.getMessage());
        verify(dao, times(2)).getAll();
    }

    @Test
    @DisplayName("GetAll method TrainerService Test")
    @Order(8)
    void getAllTrainers_success() {
        // Arrange
        Trainer trainer1 = new Trainer();
        Trainer trainer2 = new Trainer();
        when(dao.getAll()).thenReturn(Map.of(1L, trainer1, 2L, trainer2));

        // Act
        List<Trainer> result = trainerService.getAll();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.containsAll(Arrays.asList(trainer1, trainer2)));
        verify(dao, times(2)).getAll();
    }

}
