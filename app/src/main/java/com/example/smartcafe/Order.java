package com.example.smartcafe;

import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;
import java.util.List;

public class Order {
    private String userId;
    private List<OrderItem> items;
    private double totalPrice;
    @ServerTimestamp
    private Date timestamp;

    @SuppressWarnings("unused")
    public Order() {
        // Needed for Firestore
    }

    public Order(String userId, List<OrderItem> items, double totalPrice) {
        this.userId = userId;
        this.items = items;
        this.totalPrice = totalPrice;
    }

    @SuppressWarnings("unused")
    public String getUserId() {
        return userId;
    }

    @SuppressWarnings("unused")
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @SuppressWarnings("unused")
    public List<OrderItem> getItems() {
        return items;
    }

    @SuppressWarnings("unused")
    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    @SuppressWarnings("unused")
    public double getTotalPrice() {
        return totalPrice;
    }

    @SuppressWarnings("unused")
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @SuppressWarnings("unused")
    public Date getTimestamp() {
        return timestamp;
    }

    @SuppressWarnings("unused")
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
