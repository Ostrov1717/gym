package org.example.profiles;

import lombok.extern.slf4j.Slf4j;
import org.example.model.Trainee;

@Slf4j
public class TraineeMapper {
    public static TraineeProfile toProfile(Trainee trainee) {
        if (trainee == null) {
            log.warn("Provided Trainee object is null, returning null.");
            return null;
        }
        log.debug("Mapping Trainee [{}] to TraineeProfile.", trainee);

        TraineeProfile traineeProfile = new TraineeProfile();
        traineeProfile.setFirstName(trainee.getUser().getFirstName());
        traineeProfile.setLastName(trainee.getUser().getLastName());
        traineeProfile.setUsername(trainee.getUser().getUsername());
        traineeProfile.setActive(trainee.getUser().isActive());
        traineeProfile.setAddress(trainee.getAddress());
        traineeProfile.setDateOfBirth(trainee.getDateOfBirth());

        log.debug("Mapped basic user info: firstName [{}], lastName [{}], username [{}], active [{}].",
                traineeProfile.getFirstName(), traineeProfile.getLastName(), traineeProfile.getUsername(),
                traineeProfile.isActive());

//        traineeProfile.setTrainers(trainee.getTrainers());
//        traineeProfile.setTrainings(trainee.getTrainings());
        return traineeProfile;
    }

//    public static Trainee traineeProfileToTrainee(TraineeProfile traineeProfile) {
//        log.debug("Mapping TraineeProfile [{}] to Trainee.", traineeProfile);
//    }
}
