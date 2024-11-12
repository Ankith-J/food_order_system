package com.example.food_ordering.entities;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;  // Import Jackson annotation

@Entity
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "menu_item_id")
    private FoodItem menuItem;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    @JsonBackReference  // Add this annotation to avoid recursive serialization
    private Cart cart;

    private Integer quantity;

    private Double price;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public FoodItem getMenuItem() {
        return menuItem;
    }

    public Cart getCart() {
        return cart;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMenuItem(FoodItem menuItem) {
        this.menuItem = menuItem;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}