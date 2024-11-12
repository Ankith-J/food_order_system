package com.example.food_ordering.services;

import java.util.Optional;

import com.example.food_ordering.entities.User;

public interface UserService {
    // Method to save a user
    User saveUser(User user);

    // Method to find a user by username
    Optional<User> findByUsername(String username);
    
    //Method to delete user
    void deleteUserByUsername(String username);

}
