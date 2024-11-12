package com.example.food_ordering.controllers;

import com.example.food_ordering.entities.Order;
import com.example.food_ordering.services.OrderService;
import com.example.food_ordering.repositories.OrderRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private OrderRepository orderRepository;

    @Test
    void processOrder_Success_ReturnsOrder() throws Exception {
        // Arrange
        Order order = new Order();
        order.setId(1L);

        when(orderService.processOrder(anyLong())).thenReturn(order);

        // Act & Assert
        mockMvc.perform(post("/api/orders/1/process"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void processOrder_Failure_ReturnsBadRequest() throws Exception {
        // Arrange
        when(orderService.processOrder(anyLong())).thenThrow(new RuntimeException("Order cannot be processed"));

        // Act & Assert
        mockMvc.perform(post("/api/orders/1/process"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getOrder_Found_ReturnsOrder() throws Exception {
        // Arrange
        Order order = new Order();
        order.setId(1L);

        when(orderService.getOrder(1L)).thenReturn(order);

        // Act & Assert
        mockMvc.perform(get("/api/orders/show/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void getOrder_NotFound_ReturnsNotFound() throws Exception {
        // Arrange
        when(orderService.getOrder(anyLong())).thenThrow(new RuntimeException("Order not found"));

        // Act & Assert
        mockMvc.perform(get("/api/orders/show/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteOrder_Found_ReturnsSuccessMessage() throws Exception {
        // Arrange
        Order order = new Order();
        order.setId(1L);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        Mockito.doNothing().when(orderRepository).delete(order);

        // Act & Assert
        mockMvc.perform(delete("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Order deleted successfully"));
    }

    @Test
    void deleteOrder_NotFound_ReturnsNotFound() throws Exception {
        // Arrange
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(delete("/api/orders/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Order not found"));
    }
}
