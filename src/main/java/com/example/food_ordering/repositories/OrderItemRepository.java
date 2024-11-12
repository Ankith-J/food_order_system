package com.example.food_ordering.repositories;

import com.example.food_ordering.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    // Custom queries can be added here if needed
}
