package com.example.smartcafe;

import com.google.firebase.firestore.Exclude;

public class Dish {
    private String documentId;
    private String name;
    private String price;
    private String category;
    private String description;
    private String imageUrl;

    public Dish() {
        // Needed for Firestore
    }

    public Dish(String name, String price, String category, String description, String imageUrl) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
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

    @SuppressWarnings("unused")
    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    @SuppressWarnings("unused")
    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    @SuppressWarnings("unused")
    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @SuppressWarnings("unused")
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
