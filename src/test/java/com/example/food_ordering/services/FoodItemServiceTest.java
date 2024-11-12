package com.example.food_ordering.services;

import com.example.food_ordering.entities.FoodItem;
import com.example.food_ordering.repositories.FoodItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FoodItemServiceTest {

    @Mock
    private FoodItemRepository foodItemRepository;

    @InjectMocks
    private FoodItemService foodItemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addFoodItem_Success() {
        // Arrange
        FoodItem foodItem = new FoodItem();
        foodItem.setName("Burger");
        foodItem.setPrice(10.0);

        when(foodItemRepository.save(foodItem)).thenReturn(foodItem);

        // Act
        FoodItem result = foodItemService.addFoodItem(foodItem);

        // Assert
        assertNotNull(result);
        assertEquals("Burger", result.getName());
        assertEquals(10.0, result.getPrice());
    }

    @Test
    void getAllFoodItems_Success() {
        // Arrange
        FoodItem foodItem1 = new FoodItem();
        foodItem1.setName("Burger");
        FoodItem foodItem2 = new FoodItem();
        foodItem2.setName("Pizza");

        when(foodItemRepository.findAll()).thenReturn(Arrays.asList(foodItem1, foodItem2));

        // Act
        List<FoodItem> result = foodItemService.getAllFoodItems();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Burger", result.get(0).getName());
        assertEquals("Pizza", result.get(1).getName());
    }

    @Test
    void getFoodItemById_Found() {
        // Arrange
        Long foodItemId = 1L;
        FoodItem foodItem = new FoodItem();
        foodItem.setId(foodItemId);
        foodItem.setName("Burger");

        when(foodItemRepository.findById(foodItemId)).thenReturn(Optional.of(foodItem));

        // Act
        Optional<FoodItem> result = foodItemService.getFoodItemById(foodItemId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Burger", result.get().getName());
    }

    @Test
    void getFoodItemById_NotFound() {
        // Arrange
        Long foodItemId = 1L;
        when(foodItemRepository.findById(foodItemId)).thenReturn(Optional.empty());

        // Act
        Optional<FoodItem> result = foodItemService.getFoodItemById(foodItemId);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void updateFoodItem_ExistingItem() {
        // Arrange
        Long foodItemId = 1L;
        FoodItem foodItem = new FoodItem();
        foodItem.setName("Burger");
        foodItem.setPrice(12.0);

        when(foodItemRepository.existsById(foodItemId)).thenReturn(true);
        when(foodItemRepository.save(any(FoodItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        FoodItem updatedFoodItem = foodItemService.updateFoodItem(foodItemId, foodItem);

        // Assert
        assertNotNull(updatedFoodItem);
        assertEquals(foodItemId, updatedFoodItem.getId());
        assertEquals("Burger", updatedFoodItem.getName());
        assertEquals(12.0, updatedFoodItem.getPrice());
    }

    @Test
    void updateFoodItem_NonExistingItem() {
        // Arrange
        Long foodItemId = 1L;
        FoodItem foodItem = new FoodItem();
        foodItem.setName("Burger");

        when(foodItemRepository.existsById(foodItemId)).thenReturn(false);

        // Act
        FoodItem result = foodItemService.updateFoodItem(foodItemId, foodItem);

        // Assert
        assertNull(result);
    }

    @Test
    void deleteFoodItem_Success() {
        // Arrange
        Long foodItemId = 1L;

        doNothing().when(foodItemRepository).deleteById(foodItemId);

        // Act
        foodItemService.deleteFoodItem(foodItemId);

        // Assert
        verify(foodItemRepository, times(1)).deleteById(foodItemId);
    }
}
