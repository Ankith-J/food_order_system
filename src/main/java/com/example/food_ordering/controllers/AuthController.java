package com.example.food_ordering.controllers;

import com.example.food_ordering.entities.User;
import com.example.food_ordering.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Registration
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody User user) {
        logger.info("Attempting to register user with username: {}", user.getUsername());

        Optional<User> existingUser = userService.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            logger.warn("Username '{}' is already taken", user.getUsername());
            return ResponseEntity.badRequest().body("Username is already taken");
        }

        userService.saveUser(user);
        logger.info("User '{}' registered successfully", user.getUsername());
        return ResponseEntity.ok("User registered successfully");
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody User loginRequest) {
        logger.info("User attempting to log in with username: {}", loginRequest.getUsername());

        Optional<User> user = userService.findByUsername(loginRequest.getUsername());
        if (user.isPresent() && passwordEncoder.matches(loginRequest.getPassword(), user.get().getPassword())) {
            logger.info("Login successful for user: {}", loginRequest.getUsername());
            return ResponseEntity.ok("Login successful");
        } else {
            logger.error("Invalid login attempt for username: {}", loginRequest.getUsername());
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }
    
    
 // Delete User by Username
    @DeleteMapping("/delete/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        logger.info("Attempting to delete user with username: {}", username);

        userService.deleteUserByUsername(username);
        return ResponseEntity.ok("User deleted successfully (if existed)");
    }
}

