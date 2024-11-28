package org.example.gym.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;

@Schema(description = "Enum DTO contains details about User's RequestDTO and ResponseDTO")
public enum UserDTO {
    ;

    private interface Username {
        String getUsername();
    }

    private interface Password {
        String getPassword();
    }

    private interface NewPassword {
        String getNewPassword();
    }

    private interface Active {
        boolean isActive();
    }

    public enum Request {
        ;

        @Data
        @AllArgsConstructor
        @Schema(description = "Request DTO contains details about the User")
        public static class UserLogin implements Username, Password {
            @NotBlank(message = "{username.required}")
            @Schema(description = "Username of the User", example = "Olga.Kurilenko")
            String username;
            @NotBlank(message = "{password.required}")
            @Schema(description = "Password of the User", example = "WRqqRQMsoy")
            String password;
        }

        @Data
        @Schema(description = "Request DTO contains details about the User")
        public static class Use implements Username {
            @NotBlank(message = "{username.required}")
            @Schema(description = "Username of the User", example = "Olga.Kurilenko")
            String username;
        }

        @Data
        @Schema(description = "Request DTO contains details about the User")
        public static class ChangeLogin implements Username, Password, NewPassword {
            @NotBlank(message = "{username.required}")
            @Schema(description = "Username of the User", example = "Olga.Kurilenko")
            String username;
            @NotBlank(message = "{password.required}")
            @Schema(description = "Old password of the User", example = "WRqqRQMsoy")
            String password;
            @NotBlank(message = "New password is required")
            @Schema(description = "New password of the User", example = "12345")
            String newPassword;
        }

        @Data
        @AllArgsConstructor
        @Schema(description = "Request DTO contains details about the User")
        public static class ActivateOrDeactivate implements Username, Active {
            @NotBlank(message = "{username.required}")
            @Schema(description = "Username of the User", example = "Olga.Kurilenko")
            String username;
            @NotBlank(message = "{password.required}")
            @Schema(description = "Password of the User", example = "WRqqRQMsoy")
            String password;
            @NotNull(message = "Active status is required")
            @Schema(description = "Activity status of the User", example = "true")
            boolean active;
        }
    }

    public enum Response {
        ;

        @Value
        @Schema(description = "Response DTO contains details about the User - username and password")
        public static class Login implements Username, Password {
            @Schema(description = "Username of the User", example = "Olga.Kurilenko")
            String username;
            @Schema(description = "Password of the User", example = "WRqqRQMsoy")
            String password;
        }
    }
}
