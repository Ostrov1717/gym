package org.example.gym.domain.trainee.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gym.common.ApiUrls;
import org.example.gym.domain.trainee.dto.TraineeDTO;
import org.example.gym.domain.trainer.dto.TrainerDTO;
import org.example.gym.domain.user.dto.UserDTO;
import org.example.gym.domain.trainer.entity.Trainer;
import org.example.gym.domain.trainee.service.TraineeService;
import org.example.gym.domain.trainer.service.TrainerService;
import org.example.gym.domain.user.service.UserService;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Set;

@RestController
@Tag(name = "TraineeGymController", description = "Operations pertaining to Trainee management")
@RequiredArgsConstructor
@Slf4j
@RequestMapping(ApiUrls.TRAINEE_BASE)
public class TraineeController {
    private final TraineeService traineeService;
    private final UserService userService;
    private final TrainerService trainerService;

    //    1. Trainee Registration (POST method)
    @PostMapping(ApiUrls.REGISTER_TRAINEE)
    @Operation(summary = "Trainee registration", description = "Returns username and password of trainee")
    public UserDTO.Response.Login traineeRegistration(@RequestBody TraineeDTO.Request.TraineeRegistration dto) {
        log.info("POST request to " + ApiUrls.REGISTER_TRAINEE + " for registration trainee with details: {} {}, date of birth={}, address={}"
                , dto.getFirstName(), dto.getLastName(), dto.getDateOfBirth(), dto.getAddress());
        return traineeService.create(dto.getFirstName(), dto.getLastName(), dto.getAddress(), dto.getDateOfBirth());
    }

    //  5. Get Trainee Profile (GET method
    @GetMapping(ApiUrls.GET_TRAINEE_PROFILE)
    @Operation(summary = "Get trainee's profile", description = "Returns trainee's profile")
    public TraineeDTO.Response.TraineeProfile getTraineeProfile(@Valid @ModelAttribute UserDTO.Request.UserLogin dto) {
        log.info("GET request to " + ApiUrls.GET_TRAINEE_PROFILE + " with trainee username={}", dto.getUsername());
        return traineeService.findByUsername(dto.getUsername(), dto.getPassword());
    }

    //  6. Update Trainee Profile (PUT method)
    @PutMapping(ApiUrls.UPDATE_TRAINEE)
    @Operation(summary = "Update trainee's profile", description = "Returns updated trainee's profile")
    public TraineeDTO.Response.TraineeProfileFull updateTraineeProfile(@Valid @RequestBody TraineeDTO.Request.TraineeUpdate dto) {
        log.info("PUT request to " + ApiUrls.UPDATE_TRAINEE + " trainee={} with new details: {} {}, date of birth={}, address={}",
                dto.getUsername(), dto.getFirstName(), dto.getLastName(), dto.getAddress(), dto.getDateOfBirth());
        return traineeService.update(dto.getFirstName(), dto.getLastName(), dto.getUsername(),
                dto.getPassword(), dto.getAddress(), dto.getDateOfBirth(), dto.isActive());
    }

    //  7. Delete Trainee Profile (DELETE method)
    @DeleteMapping(ApiUrls.DELETE_TRAINEE)
    @Operation(summary = "Delete trainee", description = "Returns of removing trainee's profile")
    public void deleteTraineeProfile(@Valid @RequestBody UserDTO.Request.UserLogin dto) {
        log.info("DELETE request to " + ApiUrls.DELETE_TRAINEE + " trainee={}", dto.getUsername());
        traineeService.delete(dto.getUsername(), dto.getPassword());
    }

    //    11. Update Trainee's Trainer List (PUT method)
    @PutMapping(ApiUrls.UPDATE_TRAINERS_LIST)
    @Operation(summary = "Update trainers assigned to trainee", description = "Returns list of newly assigned trainers")
    public Set<TrainerDTO.Response.TrainerSummury> updateTraineeTrainers(@Valid @RequestBody TraineeDTO.Request.UpdateTrainers dto) {
        log.info("PUT request to " + ApiUrls.UPDATE_TRAINERS_LIST + " for trainee={} with new trainers: {}",
                dto.getUsername(), dto.getTrainersUsernames());
        Set<Trainer> newTrainers = trainerService.getTrainerFromList(dto.getTrainersUsernames());
        return traineeService.updateTraineeTrainers(dto.getUsername(), dto.getPassword(), newTrainers);
    }

    //    15. Activate/De-Activate Trainee (PATCH method)
    @PatchMapping(ApiUrls.UPDATE_TRAINEE_STATUS)
    @Operation(summary = "Change activity status of trainee", description = "Returns result of changing activity status")
    public void activateOrDeactivateTrainee(@Valid @RequestBody UserDTO.Request.ActivateOrDeactivate dto) {
        log.info("PATCH request to " + ApiUrls.UPDATE_TRAINEE_STATUS + " trainee={} with updates active status on: {}", dto.getUsername(), dto.isActive());
        if (dto.isActive()) {
            userService.activate(dto.getUsername(), dto.getPassword());
        } else {
            userService.deactivate(dto.getUsername(), dto.getPassword());
        }
    }
    @InitBinder
    public void init(WebDataBinder binder){
        binder.registerCustomEditor(LocalDate.class,"dateOfBirth",new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"),true));
    }
}
