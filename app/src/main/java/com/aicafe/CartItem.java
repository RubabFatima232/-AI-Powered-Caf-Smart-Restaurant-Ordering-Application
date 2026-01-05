package com.aicafe;

public class CartItem {
    private final MenuItem menuItem;
    private int quantity;

    public CartItem(MenuItem menuItem) {
        this.menuItem = menuItem;
        this.quantity = 1;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public void increment() {
        quantity++;
    }

    public void decrement() {
        if (quantity > 0) {
            quantity--;
        }
    }

    public double getSubtotal() {
        return menuItem.getPrice() * quantity;
    }
}
