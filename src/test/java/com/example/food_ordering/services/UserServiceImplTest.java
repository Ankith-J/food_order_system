package com.example.food_ordering.services;

import com.example.food_ordering.entities.User;
import com.example.food_ordering.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        // Initialize a User object for testing
        user = new User();
        user.setId(1L);
        user.setUsername("john_doe");
        user.setPassword("password123");
    }

    @Test
    void testSaveUser_Success() {
        // Mock password encoding
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword123");
        
        // Mock saving the user
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Call the service method
        User savedUser = userService.saveUser(user);

        // Verify the password was encoded
        assertEquals("encodedPassword123", savedUser.getPassword());
        assertEquals("john_doe", savedUser.getUsername());

        // Verify interactions with the mocks
        verify(passwordEncoder, times(1)).encode("password123");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testFindByUsername_UserExists() {
        // Mock finding the user by username
        when(userRepository.findByUsername("john_doe")).thenReturn(Optional.of(user));

        // Call the service method
        Optional<User> foundUser = userService.findByUsername("john_doe");

        // Assertions
        assertTrue(foundUser.isPresent());
        assertEquals("john_doe", foundUser.get().getUsername());

        // Verify interaction with the repository
        verify(userRepository, times(1)).findByUsername("john_doe");
    }

    @Test
    void testFindByUsername_UserDoesNotExist() {
        // Mock finding the user by username (user not found)
        when(userRepository.findByUsername("jane_doe")).thenReturn(Optional.empty());

        // Call the service method
        Optional<User> foundUser = userService.findByUsername("jane_doe");

        // Assertions
        assertFalse(foundUser.isPresent());

        // Verify interaction with the repository
        verify(userRepository, times(1)).findByUsername("jane_doe");
    }
}

