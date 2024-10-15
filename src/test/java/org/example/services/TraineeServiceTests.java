package org.example.services;

import org.example.dao.GenericDAO;
import org.example.model.Trainee;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TraineeServiceTests {

    @Mock
    private GenericDAO<Trainee> dao;

    @InjectMocks
    private TraineeService traineeService;

    private Map<Long,Trainee> traineeMap;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.traineeMap=new HashMap<>();
    }
    @Test
    @DisplayName("Create Trainee Test")
    @Order(1)
    void createTraineeTest() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        String address = "California";
        LocalDate dob = LocalDate.of(1990, 1, 1);

        // Мокируем метод save, чтобы он сохранял Trainee в тестовую карту
        when(dao.save(any(Trainee.class), anyLong())).thenAnswer(invocation -> {
            Trainee trainee = invocation.getArgument(0);
            Long id = invocation.getArgument(1);
            traineeMap.put(id, trainee);
            return trainee; // Сохраняем объект в тестовой карте
        });
        when(dao.getAll()).thenReturn(traineeMap);
        // Act
        Trainee firstTrainee = traineeService.create(firstName, lastName, address, dob);

        // Assert
        assertNotNull(firstTrainee);
        assertEquals(firstName + "." + lastName, firstTrainee.getUsername());
        assertEquals(10, firstTrainee.getPassword().length());
        assertEquals(firstTrainee, traineeMap.get(firstTrainee.getUserId()));

        Trainee secondTrainee = traineeService.create(firstName, lastName, "New York", null);
        assertNotNull(secondTrainee);
        assertEquals("John.Doe1", traineeMap.get(secondTrainee.getUserId()).getUsername());
        assertEquals("New York", traineeMap.get(secondTrainee.getUserId()).getAddress());

        Trainee thirdTrainee = traineeService.create(firstName, lastName, null, LocalDate.of(2000, 1, 1));
        assertNotNull(secondTrainee);
        assertEquals("John.Doe2", traineeMap.get(thirdTrainee.getUserId()).getUsername());
        assertEquals(LocalDate.of(2000, 1, 1), traineeMap.get(thirdTrainee.getUserId()).getDateOfBirth());

        assertThrows(IllegalArgumentException.class,()->traineeService.create(null,"Von","Chicago",null));
        assertThrows(IllegalArgumentException.class,()->traineeService.create("Vince",null,"Chicago",null));

        verify(dao, times(3)).save(any(Trainee.class), anyLong());
    }

    @Test
    @DisplayName("Select Trainee Test by Id - success")
    @Order(2)
    void selectByIdTest() {

        // Arrange
        long id = 1L;
        Trainee trainee = new Trainee(1,"John","Doe","John.Doe","udfdhjhgfg",true,"California",LocalDate.of(1990, 1, 1));
        when(dao.findById(id)).thenReturn(trainee);

        // Act
        Optional<Trainee> result = traineeService.selectById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(trainee, result.get());
        verify(dao, times(1)).findById(id);
    }

    @Test
    @DisplayName("Select Trainee Test by Id - not found")
    @Order(3)
    void selectById_notFound() {
        // Arrange
        long id = 1L;
        when(dao.findById(id)).thenReturn(null);

        // Act
        Optional<Trainee> result = traineeService.selectById(id);

        // Assert
        assertFalse(result.isPresent());
        verify(dao, times(1)).findById(id);
    }
    @Test
    @DisplayName("Select Trainee Test by username - success")
    @Order(4)
    void selectByUsername_found() {
        // Arrange
        String username = "john.doe";
        Trainee trainee1 = new Trainee();
        trainee1.setUsername(username);
        traineeMap.put(1L,trainee1);
        Trainee trainee2 = new Trainee();
        trainee2.setUsername("another.username");
        traineeMap.put(2L,trainee2);
        when(dao.getAll()).thenReturn(traineeMap);

        // Act
        Optional<Trainee> result = traineeService.selectByUsername(username);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(trainee1, result.get());
        verify(dao, times(2)).getAll();
    }

    @Test
    @DisplayName("Select Trainee Test by username - not found")
    @Order(5)
    void selectByUsername_notFound() {
        // Arrange
        String username = "john.doe";
        when(dao.getAll()).thenReturn(new HashMap<>());

        // Act
        Optional<Trainee> result = traineeService.selectByUsername(username);

        // Assert
        assertFalse(result.isPresent());
        verify(dao, times(2)).getAll();
    }
    @Test
    @DisplayName("Update Trainee Test - success")
    @Order(6)
    void updateTraineeTest_success() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        String username = "John.Doe";
        String address = "456 Avenue";
        LocalDate dob = LocalDate.of(1990, 1, 1);
        Trainee trainee = new Trainee();
        trainee.setUsername(username);
        when(dao.getAll()).thenReturn(Map.of(1L, trainee));

        // Act
        traineeService.update(firstName, lastName, username, address, dob, false);

        // Assert
        assertEquals(firstName, trainee.getFirstName());
        assertEquals(lastName, trainee.getLastName());
        assertEquals(address, trainee.getAddress());
        assertFalse(trainee.isActive());
        verify(dao, times(2)).getAll();
    }

    @Test
    @DisplayName("Update Trainee Test - not found")
    @Order(7)
    void updateTraineeTest_notFound() {
        // Arrange
        String username = "john.doe";
        when(dao.getAll()).thenReturn(new HashMap<>());

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                traineeService.update("John", "Doe", username, "456 Avenue", LocalDate.of(1990, 1, 1), true)
        );
        assertEquals("Trainee with username: " + username + " not found.", exception.getMessage());
        verify(dao, times(2)).getAll();
    }


    @Test
    @DisplayName("Delete Trainee Test - success")
    @Order(8)
    void deleteTrainee_success() {
        // Arrange
        String username = "john.doe";
        Trainee trainee = new Trainee();
        trainee.setUserId(1L);
        trainee.setUsername(username);
        when(dao.getAll()).thenReturn(Map.of(1L, trainee));

        // Act
        traineeService.delete(username);

        // Assert
        verify(dao, times(1)).delete(trainee.getUserId());
    }

    @Test
    @DisplayName("Delete Trainee Test - not found")
    @Order(9)
    void deleteTrainee_notFound() {
        // Arrange
        String username = "john.doe";
        when(dao.getAll()).thenReturn(new HashMap<>());

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                traineeService.delete(username)
        );
        assertEquals("Trainee with username: " + username + " not found.", exception.getMessage());
        verify(dao, times(2)).getAll();
    }
    @Test
    @DisplayName("GetAll method TraineeService Test")
    @Order(10)
    void getAllTrainees_success() {
        // Arrange
        Trainee trainee1 = new Trainee();
        Trainee trainee2 = new Trainee();
        when(dao.getAll()).thenReturn(Map.of(1L, trainee1, 2L, trainee2));

        // Act
        List<Trainee> result = traineeService.getAll();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.containsAll(Arrays.asList(trainee1, trainee2)));
        verify(dao, times(2)).getAll();
    }
}
