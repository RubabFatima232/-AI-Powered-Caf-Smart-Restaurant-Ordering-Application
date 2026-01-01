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

    @SuppressWarnings("unused")
    public String getUserId() {
        return userId;
    }

    @SuppressWarnings("unused")
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @SuppressWarnings("unused")
    public String getDishId() {
        return dishId;
    }

    @SuppressWarnings("unused")
    public void setDishId(String dishId) {
        this.dishId = dishId;
    }

    @SuppressWarnings("unused")
    public float getRating() {
        return rating;
    }

    @SuppressWarnings("unused")
    public void setRating(float rating) {
        this.rating = rating;
    }

    @SuppressWarnings("unused")
    public String getComment() {
        return comment;
    }

    @SuppressWarnings("unused")
    public void setComment(String comment) {
        this.comment = comment;
    }
}
