package com.example.food_ordering.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.food_ordering.entities.Cart;
import com.example.food_ordering.entities.CartItem;
import com.example.food_ordering.entities.FoodItem;
import com.example.food_ordering.entities.Order;
import com.example.food_ordering.entities.OrderItem;
import com.example.food_ordering.entities.User;
import com.example.food_ordering.repositories.CartItemRepository;
import com.example.food_ordering.repositories.CartRepository;
import com.example.food_ordering.repositories.FoodItemRepository;
import com.example.food_ordering.repositories.OrderRepository;
import com.example.food_ordering.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private FoodItemRepository menuRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private OrderRepository orderRepository;

    public Cart addToCart(Long userId, Long menuItemId, Integer quantity) {
        // Fetch the user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Fetch the menu item
        FoodItem menuItem = menuRepository.findById(menuItemId)
                .orElseThrow(() -> new RuntimeException("Menu item not found"));

        // Fetch or create the cart for the user
        Cart cart = cartRepository.findByUser(user)
                .orElse(new Cart());
        
        // Initialize cartItems if it's null
        if (cart.getCartItems() == null) {
            cart.setCartItems(new ArrayList<>()); // Initialize the list
        }

        // Set the user for the cart
        cart.setUser(user);

        // Calculate the price for the quantity of the item
        double price = menuItem.getPrice() * quantity;

        // Create a new CartItem and set its properties
        CartItem cartItem = new CartItem();
        cartItem.setMenuItem(menuItem);
        cartItem.setCart(cart);
        cartItem.setQuantity(quantity);
        cartItem.setPrice(price);

        // Add the cartItem to the cart's items
        cart.getCartItems().add(cartItem);
        
        // Update the total price of the cart
        cart.setTotalPrice(cart.getTotalPrice() != null ? cart.getTotalPrice() + price : price);

        // Save the cart in the repository
        cartRepository.save(cart);

        return cart;
    }
    
    public Order checkoutAndCreateOrder(Long userId) {
        // Fetch the user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Fetch the cart of the user
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        // Create an Order from the cart
        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(cart.getTotalPrice());

        // Convert cart items to order items
        List<OrderItem> orderItems = cart.getCartItems().stream().map(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setFoodItem(cartItem.getMenuItem());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice());
            orderItem.setOrder(order); // Link order to order item
            return orderItem;
        }).collect(Collectors.toList()); // Ensure we are not returning a stream directly

        // Set the list of order items to the order
        order.setOrderItems(orderItems);

        // Save the order (this will automatically save the linked order items)
        orderRepository.save(order);

        // Clear the cart after creating the order
        cart.getCartItems().clear();
        cart.setTotalPrice(0.0);
        cartRepository.save(cart);

        return order;
    }
    
 public Cart getCartByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    }

    public void clearCart(Long userId) {
        Cart cart = getCartByUserId(userId);
        cart.getCartItems().clear();
        cart.setTotalPrice(0.0);
        cartRepository.save(cart);
    }
    
    
}