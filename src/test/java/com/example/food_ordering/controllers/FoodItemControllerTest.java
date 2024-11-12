package com.example.food_ordering.controllers;

import com.example.food_ordering.entities.FoodItem;
import com.example.food_ordering.services.FoodItemService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)  // Disable security filters for testing
public class FoodItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FoodItemService foodItemService;

    @Test
    void getAllFoodItems_ReturnsListOfFoodItems() throws Exception {
        // Arrange
        FoodItem foodItem1 = new FoodItem();
        foodItem1.setId(1L);
        foodItem1.setName("Pizza");

        FoodItem foodItem2 = new FoodItem();
        foodItem2.setId(2L);
        foodItem2.setName("Burger");

        when(foodItemService.getAllFoodItems()).thenReturn(Arrays.asList(foodItem1, foodItem2));

        // Act & Assert
        mockMvc.perform(get("/api/food-items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Pizza"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Burger"));
    }

    @Test
    void getFoodItemById_Found_ReturnsFoodItem() throws Exception {
        // Arrange
        FoodItem foodItem = new FoodItem();
        foodItem.setId(1L);
        foodItem.setName("Pizza");

        when(foodItemService.getFoodItemById(1L)).thenReturn(Optional.of(foodItem));

        // Act & Assert
        mockMvc.perform(get("/api/food-items/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Pizza"));
    }

    @Test
    void getFoodItemById_NotFound_ReturnsNotFound() throws Exception {
        // Arrange
        when(foodItemService.getFoodItemById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/food-items/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void addFoodItem_Success_ReturnsCreatedFoodItem() throws Exception {
        // Arrange
        FoodItem foodItem = new FoodItem();
        foodItem.setId(1L);
        foodItem.setName("Pizza");

        when(foodItemService.addFoodItem(any(FoodItem.class))).thenReturn(foodItem);

        // Act & Assert
        mockMvc.perform(post("/api/food-items")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Pizza\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Pizza"));
    }

    @Test
    void updateFoodItem_Success_ReturnsUpdatedFoodItem() throws Exception {
        // Arrange
        FoodItem foodItem = new FoodItem();
        foodItem.setId(1L);
        foodItem.setName("Updated Pizza");

        when(foodItemService.updateFoodItem(anyLong(), any(FoodItem.class))).thenReturn(foodItem);

        // Act & Assert
        mockMvc.perform(put("/api/food-items/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Updated Pizza\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Updated Pizza"));
    }

    @Test
    void updateFoodItem_NotFound_ReturnsNotFound() throws Exception {
        // Arrange
        when(foodItemService.updateFoodItem(anyLong(), any(FoodItem.class))).thenReturn(null);

        // Act & Assert
        mockMvc.perform(put("/api/food-items/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Updated Pizza\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteFoodItem_Success_ReturnsOk() throws Exception {
        // Arrange
        Mockito.doNothing().when(foodItemService).deleteFoodItem(anyLong());

        // Act & Assert
        mockMvc.perform(delete("/api/food-items/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("delete success"));
    }
}

