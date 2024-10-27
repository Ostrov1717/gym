package org.example.services;

import org.example.dao.TraineeRepository;
import org.example.model.Trainee;
import org.example.model.User;
import org.example.profiles.TraineeMapper;
import org.example.profiles.TraineeProfile;
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
    private TraineeRepository traineeRepository;

    @InjectMocks
    private TraineeService traineeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Create Trainee Test")
    @Order(1)
    void createTraineeTest() {

        String firstName = "John";
        String lastName = "Doe";
        String address = "California";
        LocalDate dob = LocalDate.of(1990, 1, 1);

        Trainee trainee = new Trainee(new User(firstName, lastName, "John.Doe", "1234", false), address, dob);
        when(traineeRepository.save(any(Trainee.class))).thenReturn(trainee);

        Optional<TraineeProfile> result = traineeService.create(firstName, lastName, address, dob);

        assertTrue(result.isPresent());
        assertEquals("John.Doe", result.get().getUsername());
        assertEquals("California", result.get().getAddress());
        assertEquals(dob, result.get().getDateOfBirth());
        verify(traineeRepository, times(1)).save(any(Trainee.class));
    }

    @Test
    @DisplayName("Select Trainee Test by Id - success")
    @Order(2)
    void selectByIdTest() {

        long id = 1L;
        Trainee trainee = new Trainee
                (new User("John", "Doe", "John.Doe", "udfdhjhgfg", true), "California", LocalDate.of(1990, 1, 1));

        when(traineeRepository.findById(id)).thenReturn(Optional.of(trainee));

        Optional<TraineeProfile> result = traineeService.findById(id);

        assertTrue(result.isPresent());
        assertEquals(TraineeMapper.toProfile(trainee), result.get());
        verify(traineeRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Select Trainee Test by username - success")
    @Order(3)
    void findByUsername_success() {

        String username = "John.Doe";
        String password = "password123";
        Trainee trainee = new Trainee(new User("John", "Doe", username, password, true), "123 Street", LocalDate.of(1990, 1, 1));

        when(traineeRepository.findByUsernameAndPassword(username, password)).thenReturn(Optional.of(trainee));
        when(traineeRepository.findByUserUsername(username)).thenReturn(Optional.of(trainee));

        Optional<TraineeProfile> result = traineeService.findByUsername(username, password);

        assertTrue(result.isPresent());
        assertEquals(username, result.get().getUsername());
        verify(traineeRepository, times(1)).findByUsernameAndPassword(username, password);
        verify(traineeRepository, times(1)).findByUserUsername(username);
    }

    @Test
    @DisplayName("Select Trainee Test by username - authentication failed")
    @Order(4)
    void findByUsername_authenticationFailed() {

        String username = "John.Doe";
        String password = "wrongPassword";

        when(traineeRepository.findByUsernameAndPassword(username, password)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                traineeService.findByUsername(username, password)
        );
        assertEquals("Invalid username or password", exception.getMessage());
        verify(traineeRepository, times(1)).findByUsernameAndPassword(username, password);
        verify(traineeRepository, never()).findByUserUsername(anyString());
    }

    @Test
    @DisplayName("Select Trainee Test by username - not found")
    @Order(5)
    void findByUsername_traineeNotFound() {

        String username = "John.Doe";
        String password = "password123";

        when(traineeRepository.findByUsernameAndPassword(username, password)).thenReturn(Optional.of(new Trainee()));
        when(traineeRepository.findByUserUsername(username)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                traineeService.findByUsername(username, password)
        );
        assertEquals("Trainee with username: " + username + " not found.", exception.getMessage());
        verify(traineeRepository, times(1)).findByUsernameAndPassword(username, password);
        verify(traineeRepository, times(1)).findByUserUsername(username);
    }
    @Test
    @DisplayName("Update Trainee Test - success")
    @Order(6)
    void updateTrainee_success() {

        String firstName = "John";
        String lastName = "Doe";
        String username = "john.doe";
        String password = "password123";
        String address = "123 New Street";
        LocalDate dateOfBirth = LocalDate.of(1990, 1, 1);
        boolean isActive = true;

        Trainee trainee = new Trainee(new User(firstName, lastName, username, password, false), "Old Address", LocalDate.of(1980, 1, 1));

        when(traineeRepository.findByUsernameAndPassword(username, password)).thenReturn(Optional.of(trainee));
        when(traineeRepository.findByUserUsername(username)).thenReturn(Optional.of(trainee));

        Optional<TraineeProfile> result = traineeService.update(firstName, lastName, username, password, address, dateOfBirth, isActive);

        assertTrue(result.isPresent());
        assertEquals(firstName, result.get().getFirstName());
        assertEquals(lastName, result.get().getLastName());
        assertEquals(address, trainee.getAddress());
        assertEquals(dateOfBirth, trainee.getDateOfBirth());
        assertEquals(isActive, trainee.getUser().isActive());
        verify(traineeRepository, times(1)).findByUsernameAndPassword(username, password);
        verify(traineeRepository, times(1)).findByUserUsername(username);
    }

    @Test
    @DisplayName("Delete Trainee Test - success")
    @Order(7)
    void deleteTrainee_success() {

        String username = "john.doe";
        String password = "password123";
        Trainee trainee = new Trainee(new User("John", "Doe", username, password, true), "123 Street", LocalDate.of(1990, 1, 1));

        when(traineeRepository.findByUsernameAndPassword(username, password)).thenReturn(Optional.of(trainee));
        when(traineeRepository.findByUserUsername(username)).thenReturn(Optional.of(trainee));

        traineeService.delete(username, password);

        verify(traineeRepository, times(1)).findByUsernameAndPassword(username, password);
        verify(traineeRepository, times(1)).findByUserUsername(username);
        verify(traineeRepository, times(1)).delete(trainee);
    }

}
