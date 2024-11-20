package org.example.gym.domain.trainer.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gym.domain.trainer.dto.TrainerDTO;
import org.example.gym.domain.trainer.service.TrainerService;
import org.example.gym.domain.training.entity.TrainingType;
import org.example.gym.domain.training.entity.TrainingTypeName;
import org.example.gym.domain.user.dto.UserDTO;
import org.example.gym.domain.user.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

import static org.example.gym.common.ApiUrls.*;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
@RequestMapping(TRAINER_BASE)
public class TrainerController {
    private final TrainerService trainerService;
    private final UserService userService;

    //    2. Trainer Registration (POST method)
    @PostMapping(REGISTER_TRAINER)
    public UserDTO.Response.Login trainerRegistration(@Valid @RequestBody TrainerDTO.Request.TrainerRegistration dto) {
        log.info("POST request to " + REGISTER_TRAINER + " for registration trainer with details: {} {}, specialization={}"
                , dto.getFirstName(), dto.getLastName(), dto.getSpecialization());
        return trainerService.create(dto.getFirstName(), dto.getLastName(),
                TrainingTypeName.valueOf(dto.getSpecialization().getTrainingType()));
    }

    //  8. Get Trainer Profile (GET method)
    @GetMapping(GET_TRAINER_PROFILE)
    public TrainerDTO.Response.TrainerProfile getTrainerProfile(@Valid @ModelAttribute UserDTO.Request.UserLogin dto) {
        log.info("GET request to " + GET_TRAINER_PROFILE + " with trainer username={}", dto.getUsername());
        return trainerService.findByUsername(dto.getUsername(), dto.getPassword());
    }

    //    9. Update Trainer Profile (PUT method)
    @PutMapping(UPDATE_TRAINER)
    public TrainerDTO.Response.TrainerProfile updateTrainerProfile(@Valid @RequestBody TrainerDTO.Request.TrainerUpdate dto) {
        log.info("PUT request to " + UPDATE_TRAINER + " trainer={} with new details: {} {}, specialization={}",
                dto.getUsername(), dto.getFirstName(), dto.getLastName(), dto.getSpecialization());
        return trainerService.update(dto.getFirstName(), dto.getLastName(), dto.getUsername(), dto.getPassword(),
                TrainingTypeName.valueOf(dto.getSpecialization().getTrainingType()), dto.isActive());
    }

    //    10. Get not assigned on trainee active trainers. (GET method)
    @GetMapping(GET_NOT_ASSIGN_TRAINERS)
    public Set<TrainerDTO.Response.TrainerSummury> getNotAssingTrainers(@Valid @ModelAttribute UserDTO.Request.UserLogin dto) {
        log.info("GET request to " + GET_NOT_ASSIGN_TRAINERS + " from trainee username={}", dto.getUsername());
        return trainerService.getAvailableTrainers(dto.getUsername(), dto.getPassword());
    }

    //    16. Activate/De-Activate Trainer (PATCH method)
    @PatchMapping(UPDATE_TRAINER_STATUS)
    public void activateOrDeactivateTrainer(@Valid @RequestBody UserDTO.Request.ActivateOrDeactivate dto) {
        log.info("PATCH request to " + UPDATE_TRAINER_STATUS + " trainer={} with updates active status on: {}", dto.getUsername(), dto.isActive());
        if (dto.isActive()) {
            userService.activate(dto.getUsername(), dto.getPassword());
        } else {
            userService.deactivate(dto.getUsername(), dto.getPassword());
        }
    }

    //  17. Get Training types (GET method)
    @GetMapping(TRAINING_TYPES)
    public List<TrainingType> getTrainingTypes() {
        log.info("GET request to /types for training types in gym");
        return trainerService.trainingTypes();
    }
}
