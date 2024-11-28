package org.example.gym.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gym.domain.user.dto.UserDTO;
import org.example.gym.domain.user.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.example.gym.common.ApiUrls.*;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
@RequestMapping(USER_BASE)
public class UserAPIController {
    private final UserService userService;

    //  3. Login (GET method)
    @GetMapping(USER_LOGIN)
    public void login(@Valid @ModelAttribute UserDTO.Request.UserLogin dto) {
        log.info("GET request to /login with username={}", dto.getUsername());
        userService.authenticate(dto.getUsername(), dto.getPassword());
        log.info("GET request successful. Username authenticated.");
    }

    //    4. Change Login (PUT method)
    @PutMapping(USER_CHANGE_LOGIN)
    public void changeLogin(@Valid @RequestBody UserDTO.Request.ChangeLogin dto) {
        log.info("PUT request to /change-login username={} with new password.", dto.getUsername());
        userService.changePassword(dto.getUsername(), dto.getPassword(), dto.getNewPassword());
    }
}
