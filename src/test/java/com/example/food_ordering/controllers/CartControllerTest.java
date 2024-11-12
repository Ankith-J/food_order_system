package com.example.food_ordering.controllers;

import com.example.food_ordering.entities.Cart;
import com.example.food_ordering.entities.Order;
import com.example.food_ordering.services.CartService;
import com.example.food_ordering.services.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)  // Disable security filters for testing
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @MockBean
    private OrderService orderService;

    @Test
    void addToCart_Success_ReturnsCreated() throws Exception {
        // Arrange
        Cart cart = new Cart();
        cart.setId(1L);

        when(cartService.addToCart(anyLong(), anyLong(), any())).thenReturn(cart);

        // Act & Assert
        mockMvc.perform(post("/api/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\": 1, \"foodItemId\": 2, \"quantity\": 1}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void getCart_Success_ReturnsCart() throws Exception {
        // Arrange
        Cart cart = new Cart();
        cart.setId(1L);

        when(cartService.getCartByUserId(anyLong())).thenReturn(cart);

        // Act & Assert
        mockMvc.perform(get("/api/cart/show/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void clearCart_Success_ReturnsOk() throws Exception {
        // Arrange
        Mockito.doNothing().when(cartService).clearCart(anyLong());

        // Act & Assert
        mockMvc.perform(delete("/api/cart/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Cart cleared successfully"));
    }

    @Test
    void checkout_Success_ReturnsOrder() throws Exception {
        // Arrange
        Order order = new Order();
        order.setId(1L);

        when(cartService.checkoutAndCreateOrder(anyLong())).thenReturn(order);

        // Act & Assert
        mockMvc.perform(post("/api/cart/checkout/1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void addToCart_InvalidData_ReturnsBadRequest() throws Exception {
        // Simulate invalid data by omitting required fields or providing wrong data type
        mockMvc.perform(post("/api/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\": \"invalid\", \"foodItemId\": \"invalid\", \"quantity\": -1}"))
                .andExpect(status().isBadRequest());
    }
}
