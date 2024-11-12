package com.example.food_ordering.controllers;

import com.example.food_ordering.entities.User;
import com.example.food_ordering.services.UserService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

public class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void registerUser_UserAlreadyExists_ReturnsBadRequest() throws Exception {
        // Arrange
        User user = new User();
        user.setUsername("john_doe");
        user.setPassword("password123");

        when(userService.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                .contentType("application/json")
                .content("{ \"username\": \"john_doe\", \"password\": \"password123\" }"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Username is already taken"));
    }

    @Test
    void registerUser_SuccessfulRegistration_ReturnsOk() throws Exception {
        // Arrange
        User user = new User();
        user.setUsername("john_doe");
        user.setPassword("password123");

        when(userService.findByUsername(user.getUsername())).thenReturn(Optional.empty());
//        doNothing().when(userService).saveUser(user);
        when(userService.saveUser(user)).thenReturn(user);


        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                .contentType("application/json")
                .content("{ \"username\": \"john_doe\", \"password\": \"password123\" }"))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));
    }

    @Test
    void loginUser_InvalidCredentials_ReturnsUnauthorized() throws Exception {
        // Arrange
        User user = new User();
        user.setUsername("john_doe");
        user.setPassword("password123");

        when(userService.findByUsername("john_doe")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpassword", user.getPassword())).thenReturn(false);

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType("application/json")
                .content("{ \"username\": \"john_doe\", \"password\": \"wrongpassword\" }"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid username or password"));
    }

    @Test
    void loginUser_SuccessfulLogin_ReturnsOk() throws Exception {
        // Arrange
        User user = new User();
        user.setUsername("john_doe");
        user.setPassword("password123");

        when(userService.findByUsername("john_doe")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", user.getPassword())).thenReturn(true);

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType("application/json")
                .content("{ \"username\": \"john_doe\", \"password\": \"password123\" }"))
                .andExpect(status().isOk())
                .andExpect(content().string("Login successful"));
    }
}

