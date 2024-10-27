package org.example.profiles;

import lombok.extern.slf4j.Slf4j;
import org.example.model.Trainer;
@Slf4j
public class TrainerMapper {
    public static TrainerProfile toProfile(Trainer trainer) {
        if (trainer == null) {
            log.warn("Provided Trainer object is null, returning null.");
            return null;
        }
        log.debug("Mapping Trainer [{}] to TrainerProfile.", trainer);

        TrainerProfile trainerProfile = new TrainerProfile();
        trainerProfile.setFirstName(trainer.getUser().getFirstName());
        trainerProfile.setLastName(trainer.getUser().getLastName());
        trainerProfile.setUsername(trainer.getUser().getUsername());
        trainerProfile.setActive(trainer.getUser().isActive());
        trainerProfile.setSpecialization(trainer.getSpecialization());

        log.debug("Mapped basic user info: firstName [{}], lastName [{}], username [{}], active [{}].",
                trainerProfile.getFirstName(), trainerProfile.getLastName(), trainerProfile.getUsername(),
                trainerProfile.isActive());
//        trainerProfile.setTrainees(trainer.getTrainees());
//        trainerProfile.setTrainings(trainer.getTrainings());
        return trainerProfile;
    }

//    public static Trainer trainerProfileToTrainer(TrainerProfile trainerProfile) {
//        log.debug("Mapping TrainerProfile [{}] to Trainer.", trainerProfile);
//    }
}
