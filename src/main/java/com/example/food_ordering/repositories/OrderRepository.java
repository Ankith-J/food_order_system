package com.example.food_ordering.repositories;

import com.example.food_ordering.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}

