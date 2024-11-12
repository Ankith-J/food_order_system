package com.example.food_ordering.controllers;

import com.example.food_ordering.entities.Order;
import com.example.food_ordering.repositories.OrderRepository;
import com.example.food_ordering.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;
    
    @Autowired
    private OrderRepository orderRepository;

    // Process the order (checkout)
    @PostMapping("/{userId}/process")
    public ResponseEntity<Order> processOrder(@PathVariable Long userId) {
        try {
            Order order = orderService.processOrder(userId);
            return ResponseEntity.ok(order);  // Return the created order
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(null);  // Bad request if the order cannot be processed
        }
    }

    // Get order details
    @GetMapping("/show/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable Long orderId) {
        try {
            Order order = orderService.getOrder(orderId);
            return ResponseEntity.ok(order);  // Return order details
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(null);  // Not found if the order doesn't exist
        }
    }
    
    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long orderId) {
        // Check if the order exists
        Order order = orderRepository.findById(orderId).orElse(null);

        if (order == null) {
            return ResponseEntity.status(404).body("Order not found");
        }

        // Proceed to delete the order
        orderRepository.delete(order);

        // Return a successful response
        return ResponseEntity.status(200).body("Order deleted successfully");
    }
}
