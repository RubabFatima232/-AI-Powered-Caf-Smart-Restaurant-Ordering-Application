package com.example.smartcafe;

public class CartItem {
    private final String dishId;
    private String name;
    private final String price;
    private int quantity;

    public CartItem(String dishId, String name, String price, int quantity) {
        this.dishId = dishId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters
    public String getDishId() {
        return dishId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
