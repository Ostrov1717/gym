package org.example.gym.domain.trainee.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.gym.domain.trainer.dto.TrainerDTO;

import java.time.LocalDate;
import java.util.Set;

@Schema(description = "Enum DTO contains details about Trainee's RequestDTO and ResponseDTO")
public enum TraineeDTO {
    ;

    private interface Username {
        String getUsername();
    }

    private interface Password {
        String getPassword();
    }

    private interface FirstName {
        String getFirstName();
    }

    private interface LastName {
        String getLastName();
    }

    private interface DateOfBirth {
        LocalDate getDateOfBirth();
    }

    private interface Address {
        String getAddress();
    }

    private interface Active {
        boolean isActive();
    }

    private interface Trainers {
        Set<TrainerDTO.Response.TrainerSummury> getTrainers();
    }

    private interface TrainersUsernames {
        Set<TrainerDTO.Response.TrainerUsername> getTrainersUsernames();
    }
    @Schema(description = "Enum DTO contains details about Trainee's RequestDTO")
    public enum Request {
        ;

        @Data
        @Schema(description = "Request DTO contains details about the Trainee")
        public static class TraineeRegistration implements FirstName, LastName, DateOfBirth, Address {
            @NotBlank(message = "First name is required")
            @Schema(description = "First name of the Trainee", example = "Olga")
            String firstName;
            @NotBlank(message = "Last name is required")
            @Schema(description = "Last name of the Trainee", example = "Kurilenko")
            String lastName;
            @Schema(description = "Date of birth of the Trainee", example = "1990-01-02")
            LocalDate dateOfBirth;
            @Schema(description = "Address of the Trainee", example = "02055, California, Mulholland Drive")
            String address;
        }

        @Data
        @Schema(description = "Request DTO contains details about the Trainee")
        public static class TraineeUpdate implements Username, Password, FirstName, LastName, DateOfBirth, Address, Active {
            @NotBlank(message = "{username.required}")
            @Schema(description = "Username of the Trainee", example = "Olga.Kurilenko")
            String username;
            @NotBlank(message = "{password.required}")
            @Schema(description = "Password of the Trainee", example = "WRqqRQMsoy")
            String password;
            @NotBlank(message = "{first.name.required}")
            @Schema(description = "First name of the Trainee", example = "Olga")
            String firstName;
            @NotBlank(message = "{last.name.required}")
            @Schema(description = "Last name of the Trainee", example = "Kurilenko")
            String lastName;
            @Schema(description = "Date of birth of the Trainee", example = "1990-01-02")
            LocalDate dateOfBirth;
            @Schema(description = "Address of the Trainee", example = "02055, California, Mulholland Drive")
            String address;
            @NotNull(message = "Trainee's status is required")
            @Schema(description = "Activity status of the Trainee", example = "true")
            boolean active;
        }

        @Data
        @Schema(description = "Request DTO contains details about the Trainee")
        public static class UpdateTrainers implements Username, Password, TrainersUsernames {
            @NotBlank(message = "{username.required}")
            @Schema(description = "Username of the Trainee", example = "Olga.Kurilenko")
            String username;
            @NotBlank(message = "{password.required}")
            @Schema(description = "Password of the Trainee", example = "WRqqRQMsoy")
            String password;
            @NotNull(message = "Trainer's set is required")
            @Schema(description = "List trainers usernames of the Trainee", example = "[Monica.Dobs,Wallace.Tim]")
            Set<TrainerDTO.Response.TrainerUsername> trainersUsernames;
        }
    }
    @Schema(description = "Enum DTO contains details about Trainee's ResponseDTO")
    public enum Response {
        ;

        @Data
        @AllArgsConstructor
        @Schema(description = "Response DTO contains details about the Trainee(profile)")
        public static class TraineeProfile implements FirstName, LastName, DateOfBirth, Address, Active, Trainers {
            @Schema(description = "First name of the Trainee", example = "Olga")
            String firstName;
            @Schema(description = "Last name of the Trainee", example = "Kurilenko")
            String lastName;
            @Schema(description = "Date of birth of the Trainee", example = "1990-01-02")
            LocalDate dateOfBirth;
            @Schema(description = "Address of the Trainee", example = "02055, California, Mulholland Drive")
            String address;
            @Schema(description = "Activity status of the Trainee", example = "true")
            boolean active;
            Set<TrainerDTO.Response.TrainerSummury> trainers;
        }

        @Data
        @AllArgsConstructor
        @Schema(description = "Response DTO contains details about the Trainee(profile full)")
        public static class TraineeProfileFull implements Username, FirstName, LastName, DateOfBirth, Address, Active, Trainers {
            @Schema(description = "Username of the Trainee", example = "Olga.Kurilenko")
            String username;
            @Schema(description = "First name of the Trainee", example = "Olga")
            String firstName;
            @Schema(description = "Last name of the Trainee", example = "Kurilenko")
            String lastName;
            @Schema(description = "Date of birth of the Trainee", example = "1990-01-02")
            LocalDate dateOfBirth;
            @Schema(description = "Address of the Trainee", example = "02055, California, Mulholland Drive")
            String address;
            @Schema(description = "Activity status of the Trainee", example = "true")
            boolean active;
            Set<TrainerDTO.Response.TrainerSummury> trainers;
        }

        @Data
        @AllArgsConstructor
        public static class TraineeSummury implements Username, FirstName, LastName {
            @Schema(description = "Username of the Trainee", example = "Olga.Kurilenko")
            String username;
            @Schema(description = "First name of the Trainee", example = "Olga")
            String firstName;
            @Schema(description = "Last name of the Trainee", example = "Kurilenko")
            String lastName;
        }
    }
}
