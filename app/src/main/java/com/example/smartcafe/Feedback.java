package com.example.smartcafe;

public class Feedback {
    private String userId;
    private String dishId;
    private float rating;
    private String comment;

    public Feedback() {
        // Needed for Firestore
    }

    public Feedback(String userId, String dishId, float rating, String comment) {
        this.userId = userId;
        this.dishId = dishId;
        this.rating = rating;
        this.comment = comment;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDishId() {
        return dishId;
    }

    public void setDishId(String dishId) {
        this.dishId = dishId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
