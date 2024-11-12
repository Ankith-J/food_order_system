package com.example.food_ordering.controllers;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.food_ordering.entities.Cart;
import com.example.food_ordering.entities.Order;
import com.example.food_ordering.services.CartService;
import com.example.food_ordering.services.OrderService;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;
    
    @Autowired
    private OrderService orderService;
    
    // Add item to cart
    @PostMapping
    public ResponseEntity<Cart> addToCart(@RequestBody Map<String, Object> requestBody) {
        try {
            Long userId = Long.valueOf(requestBody.get("userId").toString());
            Long foodItemId = Long.valueOf(requestBody.get("foodItemId").toString());
            Integer quantity = Integer.valueOf(requestBody.get("quantity").toString());

            Cart cart = cartService.addToCart(userId, foodItemId, quantity);
            return ResponseEntity.status(201).body(cart); // 201 Created
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(null); // Bad request for invalid data
        }
    }

    // Get cart by userId
    @GetMapping("/show/{userId}")
    public ResponseEntity<Cart> getCart(@PathVariable Long userId) {
        try {
            Cart cart = cartService.getCartByUserId(userId);
            return ResponseEntity.ok(cart);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    // Clear the user's cart
//    @PostMapping("/clear/{userId}")
//    public ResponseEntity<String> clearCart(@PathVariable Long userId) {
//        try {
//            cartService.clearCart(userId);
//            return ResponseEntity.ok("Cart cleared successfully");
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(404).body("Cart not found for user"); // Not found if cart doesn't exist
//        }
//    }
    
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> clearCart(@PathVariable Long userId) {
        try {
            cartService.clearCart(userId);
            return ResponseEntity.ok("Cart cleared successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Cart not found for user"); // Not found if cart doesn't exist
        }
    }
    
    // Checkout and create order
    @PostMapping("/checkout/{userId}")
    public ResponseEntity<Order> checkout(@PathVariable Long userId) {
        try {
            Order order = cartService.checkoutAndCreateOrder(userId); // Process checkout and create order
            return ResponseEntity.status(201).body(order);  // Return the created order
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(null);  // Return 400 if an error occurs
        }
    }
}