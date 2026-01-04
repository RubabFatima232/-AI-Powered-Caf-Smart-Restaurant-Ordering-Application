package com.aicafe.model;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import java.util.Objects;

public class MenuItem {
    private int id;
    private String name;
    private double price;
    private String imageUrl;
    private String description;

    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
    public String getDescription() { return description; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuItem menuItem = (MenuItem) o;
        return id == menuItem.id &&
                Double.compare(menuItem.price, price) == 0 &&
                Objects.equals(name, menuItem.name) &&
                Objects.equals(imageUrl, menuItem.imageUrl) &&
                Objects.equals(description, menuItem.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, imageUrl, description);
    }

    public static final DiffUtil.ItemCallback<MenuItem> DIFF = new DiffUtil.ItemCallback<MenuItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull MenuItem oldItem, @NonNull MenuItem newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull MenuItem oldItem, @NonNull MenuItem newItem) {
            return oldItem.equals(newItem);
        }
    };
}