package org.example.model;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RealDataTest {
    static final Locale DEFAULT_LOCALE = Locale.getDefault();
    static final TimeZone DEFAULT_TIMEZONE = TimeZone.getDefault();

    @BeforeAll
    static void setUp() {
        Locale.setDefault(new Locale("en"));
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @AfterAll
    static void tearDown() {
        Locale.setDefault(DEFAULT_LOCALE);
        TimeZone.setDefault(DEFAULT_TIMEZONE);
    }

    @ParameterizedTest
    @DisplayName("Trainee values successfully tested")
    @CsvFileSource(files = "src/test/resources/trainee.csv", numLinesToSkip = 1)
    void testTrainee(long id, String firstName, String lastName,String username, String password, String active, String address, String dateOfBirth, String expected) {
        LocalDate date = LocalDate.parse(dateOfBirth, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        boolean isActive=Boolean.parseBoolean(active);
        String actual = new Trainee(id, firstName, lastName,username, password, isActive,address, date).toString();
        assertEquals(expected, actual);
    }
    @ParameterizedTest
    @DisplayName("Trainer values successfully tested")
    @CsvFileSource(files = "src/test/resources/trainer.csv", numLinesToSkip = 1)
    void testTrainer(long id, String firstName, String lastName,String username, String password, String active, String specialization, String expected) {
        boolean isActive=Boolean.parseBoolean(active);
        TrainingType type=TrainingType.valueOf(specialization);
        String actual = new Trainer(id, firstName, lastName,username, password, isActive,type).toString();
        assertEquals(expected, actual);
    }
    @ParameterizedTest
    @DisplayName("Training values successfully tested")
    @CsvFileSource(files = "src/test/resources/training.csv", numLinesToSkip = 1)
    void testTraining(long trainingId, long traineeId,long trainerId,String trainingName, String trainingType,String trainingDate, String trainingDuration, String expected) {
        TrainingType type=TrainingType.valueOf(trainingType);
        LocalDateTime dateTime=LocalDateTime.parse(trainingDate,DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        Duration duration=Duration.parse(trainingDuration);
        String actual = new Training(trainingId, traineeId,trainerId,trainingName,type,dateTime,duration).toString();
        assertEquals(expected, actual);
    }
}
