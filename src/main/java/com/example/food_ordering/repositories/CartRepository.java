package com.example.food_ordering.repositories;

import com.example.food_ordering.entities.Cart;
import com.example.food_ordering.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}

