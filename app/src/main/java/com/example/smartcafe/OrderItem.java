package com.example.smartcafe;

public class OrderItem {
    private String dishId;
    private String name;
    private String price;
    private int quantity;

    public OrderItem() {
        // Needed for Firestore
    }

    public OrderItem(String dishId, String name, String price, int quantity) {
        this.dishId = dishId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getDishId() {
        return dishId;
    }

    public void setDishId(String dishId) {
        this.dishId = dishId;
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

    public void setPrice(String price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
