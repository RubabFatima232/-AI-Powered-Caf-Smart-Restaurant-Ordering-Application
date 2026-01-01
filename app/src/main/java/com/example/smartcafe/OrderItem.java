package com.example.smartcafe;

public class OrderItem {
    private String dishId;
    private String name;
    private String price;
    private int quantity;

    @SuppressWarnings("unused")
    public OrderItem() {
        // Needed for Firestore
    }

    public OrderItem(String dishId, String name, String price, int quantity) {
        this.dishId = dishId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    @SuppressWarnings("unused")
    public String getDishId() {
        return dishId;
    }

    @SuppressWarnings("unused")
    public void setDishId(String dishId) {
        this.dishId = dishId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @SuppressWarnings("unused")
    public String getPrice() {
        return price;
    }

    @SuppressWarnings("unused")
    public void setPrice(String price) {
        this.price = price;
    }

    @SuppressWarnings("unused")
    public int getQuantity() {
        return quantity;
    }

    @SuppressWarnings("unused")
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
