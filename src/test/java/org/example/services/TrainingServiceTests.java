package org.example.services;

import org.example.dao.GenericDAO;
import org.example.model.Training;
import org.example.model.TrainingType;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TrainingServiceTests {
    @Mock
    private GenericDAO<Training> dao;

    @InjectMocks
    private TrainingService trainingService;

    private Map<Long,Training> trainingMap;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.trainingMap=new HashMap<>();
    }

    @Test
    @DisplayName("Create Training Test")
    @Order(1)
    void createTrainingTest() {
        // Arrange
        String trainingName = "Morning stretching";
        LocalDateTime data = LocalDateTime.of(2024, 10, 10,10,30);
        Duration duration=Duration.ofHours(1);

        when(dao.save(any(Training.class), anyLong())).thenAnswer(invocation -> {
            Training training = invocation.getArgument(0);
            Long id = invocation.getArgument(1);
            trainingMap.put(id, training);
            return training; // Сохраняем объект в тестовой карте
        });
        // Act
        Training training1 = trainingService.create(1L,1L, trainingName, TrainingType.STRETCHING,data,duration);

        // Assert
        assertNotNull(training1);
        assertEquals(training1, trainingMap.get(training1.getTrainingId()));
        Training training2 = trainingService.create(1L,1L, trainingName, TrainingType.STRETCHING,data.plusDays(1),duration);
        assertNotNull(training1);
        assertEquals(training2, trainingMap.get(training2.getTrainingId()));

        assertThrows(IllegalArgumentException.class,()->trainingService.create(0,1L, trainingName, TrainingType.STRETCHING,data,duration));
        assertThrows(IllegalArgumentException.class,()->trainingService.create(1L,0, trainingName, TrainingType.STRETCHING,data,duration));
        assertThrows(IllegalArgumentException.class,()->trainingService.create(1L,1L, "  ", TrainingType.STRETCHING,data,duration));
        assertThrows(IllegalArgumentException.class,()->trainingService.create(1L,1L, trainingName, null,data,duration));
        assertThrows(IllegalArgumentException.class,()->trainingService.create(1L,1L, trainingName, TrainingType.STRETCHING,null,duration));
        assertThrows(IllegalArgumentException.class,()->trainingService.create(1L,1L, trainingName, TrainingType.STRETCHING,data,null));

        verify(dao, times(2)).save(any(Training.class), anyLong());
    }
    @Test
    @DisplayName("Select by Trainer Id method TrainingService Test")
    @Order(2)
    void selectByTrainerId() {
        // Настройка карты данных только для этого теста
        Training training1 = new Training();
        training1.setTrainerId(1);

        Training training2 = new Training();
        training2.setTrainerId(2);

        Training training3 = new Training();
        training3.setTrainerId(1);

        when(dao.getAll()).thenReturn(Map.of(1L, training1, 2L, training2,3L, training3));

        List<Training> result = trainingService.selectByTrainerId(1);

        // Проверка результатов
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getTrainerId());
        assertEquals(1, result.get(1).getTrainerId());
        assertTrue(result.containsAll(Arrays.asList(training1, training3)));
    }

    @Test
    @DisplayName("Select by Trainee Id method TrainingService Test")
    @Order(3)
    void selectByTraineeId() {
        Training training1 = new Training();
        training1.setTraineeId(101);

        Training training2 = new Training();
        training2.setTraineeId(102);

        when(dao.getAll()).thenReturn(Map.of(1L, training1, 2L, training2));

        List<Training> result = trainingService.selectByTraineeId(101);

        assertEquals(1, result.size());
        assertEquals(101, result.get(0).getTraineeId());
    }

    @Test
    @DisplayName("Select by period of time method TrainingService Test")
    @Order(4)
    void selectByPeriod() {
        Training training1 = new Training();
        training1.setTrainingDate(LocalDateTime.of(2024, 9, 1, 10, 0));

        Training training2 = new Training();
        training2.setTrainingDate(LocalDateTime.of(2024, 10, 1, 10, 0));

        Training training3 = new Training();
        training3.setTrainingDate(LocalDateTime.of(2024, 10, 10, 10, 0));

        when(dao.getAll()).thenReturn(Map.of(1L, training1, 2L, training2, 3L, training3));

        LocalDateTime fromDate = LocalDateTime.of(2024, 9, 15, 0, 0);
        LocalDateTime toDate = LocalDateTime.of(2024, 10, 11, 23, 59);

        List<Training> result = trainingService.selectByPeriod(fromDate, toDate);

        assertEquals(2, result.size());
        assertTrue(result.get(0).getTrainingDate().isAfter(fromDate));
        assertTrue(result.get(1).getTrainingDate().isBefore(toDate));
    }

    @Test
    @DisplayName("GetAll method TrainingService Test")
    @Order(5)
    void getAllTrainings_success() {
        Training training1 = new Training();
        Training training2 = new Training();
        when(dao.getAll()).thenReturn(Map.of(1L, training1, 2L, training2));

        List<Training> result = trainingService.getAll();

        assertEquals(2, result.size());
        assertTrue(result.containsAll(Arrays.asList(training1, training2)));
        verify(dao, times(1)).getAll();
    }
}
