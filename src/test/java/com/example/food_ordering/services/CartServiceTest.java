package com.example.food_ordering.services;

import com.example.food_ordering.entities.*;
import com.example.food_ordering.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private FoodItemRepository menuRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private CartService cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addToCart_Success() {
        // Arrange
        Long userId = 1L;
        Long menuItemId = 1L;
        Integer quantity = 2;
        User user = new User();
        user.setId(userId);
        FoodItem foodItem = new FoodItem();
        foodItem.setId(menuItemId);
        foodItem.setPrice(10.0);
        Cart cart = new Cart();
        cart.setUser(user);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(menuRepository.findById(menuItemId)).thenReturn(Optional.of(foodItem));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Cart result = cartService.addToCart(userId, menuItemId, quantity);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getCartItems().size());
        assertEquals(20.0, result.getTotalPrice());  // Price should be 10.0 * 2
    }

    @Test
    void checkoutAndCreateOrder_Success() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setTotalPrice(50.0);
        List<CartItem> cartItems = new ArrayList<>();
        CartItem cartItem = new CartItem();
        cartItem.setMenuItem(new FoodItem());
        cartItem.setQuantity(2);
        cartItem.setPrice(25.0);
        cartItems.add(cartItem);
        cart.setCartItems(cartItems);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Order result = cartService.checkoutAndCreateOrder(userId);

        // Assert
        assertNotNull(result);
        assertEquals(50.0, result.getTotalAmount());
        assertEquals(1, result.getOrderItems().size());
        assertTrue(cart.getCartItems().isEmpty());
        assertEquals(0.0, cart.getTotalPrice());
    }

    @Test
    void getCartByUserId_Success() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        Cart cart = new Cart();
        cart.setUser(user);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        // Act
        Cart result = cartService.getCartByUserId(userId);

        // Assert
        assertNotNull(result);
        assertEquals(user, result.getUser());
    }

    @Test
    void clearCart_Success() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setCartItems(new ArrayList<>());
        cart.getCartItems().add(new CartItem());
        cart.setTotalPrice(50.0);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        cartService.clearCart(userId);

        // Assert
        assertTrue(cart.getCartItems().isEmpty());
        assertEquals(0.0, cart.getTotalPrice());
    }
}
