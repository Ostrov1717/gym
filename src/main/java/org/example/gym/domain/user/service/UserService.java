package org.example.gym.domain.user.service;

import lombok.extern.slf4j.Slf4j;
import org.example.gym.domain.user.entity.User;
import org.example.gym.common.exception.AuthenticationException;
import org.example.gym.common.exception.UserNotFoundException;
import org.example.gym.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;


import java.util.Random;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void authenticate(String username, String password) {
        log.info("Attempting to authenticate Trainee with username: {}", username);
        boolean authenticated = userRepository.findByUsernameAndPassword(username, password).isPresent();
        if (!authenticated) {
            log.warn("Authentication failed for username: {}", username);
            throw new AuthenticationException("Invalid username or password");
        }
        log.info("Authentication successful for username: {}", username);
    }

    @Transactional
    public void changePassword(String username, String oldPassword, String newPassword) {
        authenticate(username, oldPassword);
        log.info("Changing password of User with username: {}", username);
        User user = findUserByUsername(username);
        user.setPassword(newPassword);
        log.info("Password successfully changed");
    }

    @Transactional
    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User with username: " + username + " not found."));
    }

    @Transactional
    private boolean updateActiveStatus(String username, boolean isActive) {
        User user = findUserByUsername(username);
        user.setActive(isActive);
        return true;
    }

    @Transactional
    public boolean deactivate(String username, String password) {
        authenticate(username, password);
        log.info("Deactivating User with username: {}", username);
        boolean deactivated = updateActiveStatus(username, false);
        log.info("User with username: {} deactivated", username);
        return deactivated;
    }

    @Transactional
    public boolean activate(String username, String password) {
        authenticate(username, password);
        log.info("Activating User with username: {}", username);
        boolean activated = updateActiveStatus(username, true);
        log.info("User with username: {} activated", username);
        return activated;
    }

    public String generatePassword() {
        Random random = new Random();
        return random.ints(33, 122)
                .filter(Character::isLetter)
                .limit(10)
                .mapToObj(c -> String.valueOf((char) c))
                .collect(Collectors.joining());
    }

    @Transactional
    public String generateUserName(String firstName, String lastName) {
        String baseUserName = firstName + "." + lastName;
        long count = userRepository.findAll().stream()
                .filter(tr -> tr.getFirstName().equals(firstName) && tr.getLastName().equals(lastName))
                .count();
        return count > 0 ? baseUserName + count : baseUserName;
    }
}
