package org.example.gym.domain.training.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gym.common.ApiUrls;
import org.example.gym.domain.training.dto.TrainingDTO;
import org.example.gym.domain.training.service.TrainingService;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.List;

@RestController
@Tag(name = "TrainingGymController", description = "Operations pertaining to training management")
@RequiredArgsConstructor
@Validated
@Slf4j
public class TrainingController {
    private final TrainingService trainingService;

    //    12. Get Trainee Trainings List (GET method)
    @GetMapping(ApiUrls.GET_TRAINEE_TRAININGS)
    @Operation(summary = "Get all trainings for a trainee", description = "Returns list of trainings")
    public List<TrainingDTO.Response.TrainingProfileForTrainee> getTraineeTrainings(@Valid @ModelAttribute TrainingDTO.Request.TraineeTrains dto) {
        log.info("GET request to " + ApiUrls.GET_TRAINEE_TRAININGS + " for trainee with username={}, period from {} to {}, trainer={}",
                dto.getTraineeUsername());
//                dto.getPeriodFrom(), dto.getPeriodTo(), dto.getTrainerUsername());
        return null;
//        trainingService.findTraineeList(dto.getTraineeUsername(), dto.getPeriodFrom(), dto.getPeriodTo(), dto.getTrainerUsername(),
//                String.valueOf(dto.getTrainingType()));
    }

    //  13. Get Trainer Trainings List (GET method)
    @GetMapping(ApiUrls.GET_TRAINER_TRAININGS)
    @Operation(summary = "Get all trainings for a trainer", description = "Returns list of trainings")
    public List<TrainingDTO.Response.TrainingProfileForTrainer> getTrainerTrainings(@Valid @ModelAttribute TrainingDTO.Request.TrainerTrainings dto) {
        log.info("GET request to " + ApiUrls.GET_TRAINER_TRAININGS + " for trainer with username={}, period from {} to {}, trainee={}",
                dto.getTrainerUsername(), dto.getPeriodFrom(), dto.getPeriodTo(), dto.getTraineeUsername());
        return trainingService.findTrainerList(dto.getTrainerUsername(), dto.getPeriodFrom(), dto.getPeriodTo(), dto.getTraineeUsername());
    }

    //    14. Add Training (POST method)
    @PostMapping(ApiUrls.CREATE_TRAINING)
    @Operation(summary = "Create training", description = "Returns result of creating")
    public void createTraining(@Valid @RequestBody TrainingDTO.Request.NewTraining dto) {
        log.info("POST request to" + ApiUrls.CREATE_TRAINING + " for creating training for trainee={} with details: {}, {}, {}, trainer name={}",
                dto.getTraineeUsername(), dto.getTrainingName(), dto.getTrainingDate(), dto.getTrainingDuration(), dto.getTrainerUsername());
        trainingService.create(dto.getTraineeUsername(), dto.getTrainerUsername(), dto.getTrainingName(), dto.getTrainingDate(), dto.getTrainingDuration());
    }
    @InitBinder
    public void init(WebDataBinder binder){
        binder.registerCustomEditor(Duration.class,"trainingDuration",new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"),true));
    }
}
