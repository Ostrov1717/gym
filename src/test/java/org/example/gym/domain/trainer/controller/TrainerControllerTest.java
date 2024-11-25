package org.example.gym.domain.trainer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.example.gym.common.exception.WrongTrainingTypeException;
import org.example.gym.config.MetricsFilter;
import org.example.gym.domain.trainer.dto.TrainerDTO;
import org.example.gym.domain.trainer.service.TrainerService;
import org.example.gym.domain.training.entity.TrainingType;
import org.example.gym.domain.training.entity.TrainingTypeName;
import org.example.gym.domain.user.dto.UserDTO;
import org.example.gym.domain.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.example.gym.common.ApiUrls.*;
import static org.example.gym.common.TestConstants.*;

@WebMvcTest(value = TrainerController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = MetricsFilter.class))
class TrainerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserService userService;

    @MockBean
    private TrainerService trainerService;

    @Test
    @SneakyThrows
    void trainerRegistrationTest_Success() {
        TrainerDTO.Request.TrainerRegistration validRequest =
                new TrainerDTO.Request.TrainerRegistration(FIRST_NAME, LAST_NAME, SPECIALIZATION);
        UserDTO.Response.Login expectedResponse =
                new UserDTO.Response.Login(USERNAME, PASSWORD);
        when(trainerService.create(FIRST_NAME, LAST_NAME, TrainingTypeName.valueOf(SPECIALIZATION.getTrainingType())))
                .thenReturn(expectedResponse);
        mockMvc.perform(post(TRAINER_BASE + REGISTER_TRAINER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(USERNAME))
                .andExpect(jsonPath("$.password").isString());
        verify(trainerService, times(1))
                .create(FIRST_NAME, LAST_NAME, TrainingTypeName.valueOf(SPECIALIZATION.getTrainingType()));
    }

    @Test
    @SneakyThrows
    void trainerRegistrationTest_Failure() {
        TrainingType wrongSpecialization = new TrainingType("BREAK_DANCE");
        TrainerDTO.Request.TrainerRegistration validRequest =
                new TrainerDTO.Request.TrainerRegistration(FIRST_NAME, LAST_NAME, wrongSpecialization);
        doThrow(new WrongTrainingTypeException("Such trainer's specialization not exist in gym"))
                .when(trainerService).create(FIRST_NAME, LAST_NAME, TrainingTypeName.valueOf(wrongSpecialization.getTrainingType()));
        mockMvc.perform(post(TRAINER_BASE + REGISTER_TRAINER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(validRequest)))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().string("Such trainer's specialization not exist in gym"));
        verify(trainerService, times(1))
                .create(FIRST_NAME, LAST_NAME, TrainingTypeName.valueOf(wrongSpecialization.getTrainingType()));
    }

    @Test
    @SneakyThrows
    void getTrainerProfileTest() {
        TrainerDTO.Response.TrainerProfile profileResponse =
                new TrainerDTO.Response.TrainerProfile(FIRST_NAME, LAST_NAME, SPECIALIZATION, true, new HashSet<>());
        when(trainerService.findByUsername(USERNAME, PASSWORD)).thenReturn(profileResponse);
        mockMvc.perform(get(TRAINER_BASE + GET_TRAINER_PROFILE)
                        .param("username", USERNAME)
                        .param("password", PASSWORD)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Brad"))
                .andExpect(jsonPath("$.lastName").value("Pitt"))
                .andExpect(jsonPath("$.specialization.trainingType").value("RESISTANCE"))
                .andExpect(jsonPath("$.active").value("true"))
                .andExpect(jsonPath("$.trainees").isArray());
        verify(trainerService, times(1)).findByUsername(USERNAME, PASSWORD);
    }

    @Test
    @SneakyThrows
    void updateTrainerProfileTest() {
        TrainerDTO.Request.TrainerUpdate trainerUpdate =
                new TrainerDTO.Request.TrainerUpdate(USERNAME, PASSWORD, FIRST_NAME, LAST_NAME, SPECIALIZATION, true);
        TrainerDTO.Response.TrainerProfile trainerNewProfile =
                new TrainerDTO.Response.TrainerProfile(FIRST_NAME, LAST_NAME, SPECIALIZATION, true, new HashSet<>());
        when(trainerService
                .update(FIRST_NAME, LAST_NAME, USERNAME, PASSWORD, TrainingTypeName.valueOf(SPECIALIZATION.getTrainingType()), true))
                .thenReturn(trainerNewProfile);
        mockMvc.perform(put(TRAINER_BASE + UPDATE_TRAINER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(trainerUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Brad"))
                .andExpect(jsonPath("$.lastName").value("Pitt"))
                .andExpect(jsonPath("$.specialization.trainingType").value("RESISTANCE"))
                .andExpect(jsonPath("$.active").value("true"))
                .andExpect(jsonPath("$.trainees").isArray());
        verify(trainerService, times(1))
                .update(FIRST_NAME, LAST_NAME, USERNAME, PASSWORD, TrainingTypeName.valueOf(SPECIALIZATION.getTrainingType()), true);
    }

    @Test
    @SneakyThrows
    void getNotAssingTrainersTest() {
        Set<TrainerDTO.Response.TrainerSummury> trainers = Set.of(
                new TrainerDTO.Response.TrainerSummury("Monica.Dobs", "Monica", "Dobs", null),
                new TrainerDTO.Response.TrainerSummury("Wallace.Tim", "Wallace", "Tim", null));
        when(trainerService.getAvailableTrainers(USERNAME, PASSWORD)).thenReturn(trainers);
        mockMvc.perform(get(TRAINER_BASE + GET_NOT_ASSIGN_TRAINERS)
                        .param("username", USERNAME)
                        .param("password", PASSWORD)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[*].username",
                        containsInAnyOrder("Monica.Dobs", "Wallace.Tim")));
        verify(trainerService, times(1)).getAvailableTrainers(USERNAME, PASSWORD);
    }

    @Test
    @SneakyThrows
    void activateTrainerTest() {
        UserDTO.Request.ActivateOrDeactivate trainer = new UserDTO.Request.ActivateOrDeactivate(USERNAME, PASSWORD, true);
        when(userService.activate(USERNAME, PASSWORD)).thenReturn(true);
        mockMvc.perform(patch(TRAINER_BASE + UPDATE_TRAINER_STATUS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(trainer)))
                .andExpect(status().isOk());
        verify(userService, times(1)).activate(USERNAME, PASSWORD);
        verify(userService, never()).deactivate(anyString(), anyString());
    }

    @Test
    @SneakyThrows
    void deactivateTrainerTest() {
        UserDTO.Request.ActivateOrDeactivate trainer = new UserDTO.Request.ActivateOrDeactivate(USERNAME, PASSWORD, false);
        when(userService.deactivate(USERNAME, PASSWORD)).thenReturn(false);
        mockMvc.perform(patch(TRAINER_BASE + UPDATE_TRAINER_STATUS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(trainer)))
                .andExpect(status().isOk());
        verify(userService, times(1)).deactivate(USERNAME, PASSWORD);
        verify(userService, never()).activate(anyString(), anyString());

    }

    @Test
    @SneakyThrows
    void getTrainingTypesTest() {
        List<TrainingType> expectedTrainingTypes = Arrays.asList(
                new TrainingType("FITNESS"),
                new TrainingType("YOGA"),
                new TrainingType("ZUMBA"),
                new TrainingType("STRETCHING"),
                new TrainingType("RESISTANCE"));
        when(trainerService.trainingTypes()).thenReturn(expectedTrainingTypes);
        mockMvc.perform(get(TRAINER_BASE + TRAINING_TYPES)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[*].trainingType",
                        containsInAnyOrder("FITNESS", "YOGA", "ZUMBA", "STRETCHING", "RESISTANCE")));
    }
}