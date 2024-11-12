package com.example.food_ordering.services;

import com.example.food_ordering.entities.FoodItem;
import com.example.food_ordering.repositories.FoodItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FoodItemService {

    private final FoodItemRepository foodItemRepository;

    @Autowired
    public FoodItemService(FoodItemRepository foodItemRepository) {
        this.foodItemRepository = foodItemRepository;
    }

    // Add a new food item
    public FoodItem addFoodItem(FoodItem foodItem) {
        return foodItemRepository.save(foodItem);
    }

    // Get all food items
    public List<FoodItem> getAllFoodItems() {
        return foodItemRepository.findAll();
    }

    // Get a specific food item by ID
    public Optional<FoodItem> getFoodItemById(Long id) {
        return foodItemRepository.findById(id);
    }

    // Update an existing food item
    public FoodItem updateFoodItem(Long id, FoodItem foodItem) {
        if (foodItemRepository.existsById(id)) {
            foodItem.setId(id);
            return foodItemRepository.save(foodItem);
        }
        return null;
    }

    // Delete a food item
    public void deleteFoodItem(Long id) {
        foodItemRepository.deleteById(id);
    }
}
