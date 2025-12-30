package com.example.ai_poweredcafsmartrestaurantorderingapplication;

public class Dish {
    private String name;
    private String price;
    private int imageResId;

    public Dish(String name, String price, int imageResId) {
        this.name = name;
        this.price = price;
        this.imageResId = imageResId;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public int getImageResId() {
        return imageResId;
    }
}
