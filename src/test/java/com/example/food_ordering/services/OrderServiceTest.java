package com.example.food_ordering.services;

import com.example.food_ordering.entities.*;
import com.example.food_ordering.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FoodItemRepository foodItemRepository;

    @InjectMocks
    private OrderService orderService;

    private User user;
    private Cart cart;
    private FoodItem foodItem;
    private CartItem cartItem;
    private Order order;
    private OrderItem orderItem;

    @BeforeEach
    void setUp() {
        // Setup User
        user = new User();
        user.setId(1L);
        user.setUsername("John Doe");

        // Setup FoodItem
        foodItem = new FoodItem();
        foodItem.setId(1L);
        foodItem.setName("Burger");
        foodItem.setPrice(5.0);

        // Setup CartItem
        cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setMenuItem(foodItem);
        cartItem.setQuantity(2);
        cartItem.setPrice(10.0);

        // Setup Cart
        cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        cart.setTotalPrice(20.0);
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(cartItem);
        cart.setCartItems(cartItems);

        // Setup OrderItem
        orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setFoodItem(foodItem);
        orderItem.setQuantity(2);
        orderItem.setPrice(10.0);

        // Setup Order
        order = new Order();
        order.setId(1L);
        order.setUser(user);
        order.setTotalAmount(20.0);
        order.setOrderItems(List.of(orderItem));
    }

    @Test
    void testProcessOrder_Success() {
        // Mocking the repositories
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // Call the service method
        Order processedOrder = orderService.processOrder(user.getId());

        // Assertions
        assertNotNull(processedOrder);
        assertEquals(user.getId(), processedOrder.getUser().getId());
        assertEquals(20.0, processedOrder.getTotalAmount());
        assertEquals(1, processedOrder.getOrderItems().size());
        assertEquals("Burger", processedOrder.getOrderItems().get(0).getFoodItem().getName());

        // Verify interactions with the repositories
        verify(userRepository, times(1)).findById(user.getId());
        verify(cartRepository, times(1)).findByUser(user);
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void testProcessOrder_UserNotFound() {
        // Mocking the repositories
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        // Call the service method and expect an exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.processOrder(2L);
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findById(2L);
    }

    @Test
    void testProcessOrder_CartNotFound() {
        // Mocking the repositories
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.empty());

        // Call the service method and expect an exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.processOrder(user.getId());
        });

        assertEquals("Cart not found", exception.getMessage());
        verify(userRepository, times(1)).findById(user.getId());
        verify(cartRepository, times(1)).findByUser(user);
    }

    @Test
    void testGetOrder_Success() {
        // Mocking the repositories
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        // Call the service method
        Order fetchedOrder = orderService.getOrder(order.getId());

        // Assertions
        assertNotNull(fetchedOrder);
        assertEquals(order.getId(), fetchedOrder.getId());
        assertEquals(user.getId(), fetchedOrder.getUser().getId());

        verify(orderRepository, times(1)).findById(order.getId());
    }

    @Test
    void testGetOrder_NotFound() {
        // Mocking the repositories
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        // Call the service method and expect an exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.getOrder(999L);
        });

        assertEquals("Order not found", exception.getMessage());
        verify(orderRepository, times(1)).findById(999L);
    }
}
