package com.aicafe.model;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.util.Objects;

public class MenuItem {
    private int id;
    private String name;
    private String description;
    private double price;
    private String imageUrl;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuItem menuItem = (MenuItem) o;
        return id == menuItem.id && Double.compare(menuItem.price, price) == 0 && Objects.equals(name, menuItem.name) && Objects.equals(description, menuItem.description) && Objects.equals(imageUrl, menuItem.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, price, imageUrl);
    }

    public static final DiffUtil.ItemCallback<MenuItem> DIFF = new DiffUtil.ItemCallback<MenuItem>() {
        @Override public boolean areItemsTheSame(@NonNull MenuItem old, @NonNull MenuItem now) { return old.id == now.id; }
        @Override public boolean areContentsTheSame(@NonNull MenuItem old, @NonNull MenuItem now) { return old.equals(now); }
    };
}
