package com.example.smartcafe;

import java.util.ArrayList;
import java.util.List;

public class CartManager {

    public interface CartChangedListener {
        void onCartItemAdded(int position);
        void onCartItemRemoved(int position);
        void onCartItemChanged(int position);
        void onCartCleared();
    }

    private static CartManager instance;
    private final List<CartItem> cartItems = new ArrayList<>();
    private CartChangedListener listener;

    private CartManager() {}

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void setListener(CartChangedListener listener) {
        this.listener = listener;
    }

    public void addToCart(Dish dish) {
        for (int i = 0; i < cartItems.size(); i++) {
            CartItem item = cartItems.get(i);
            if (item.getDishId().equals(dish.getDocumentId())) {
                item.setQuantity(item.getQuantity() + 1);
                if (listener != null) {
                    listener.onCartItemChanged(i);
                }
                return;
            }
        }
        cartItems.add(new CartItem(dish.getDocumentId(), dish.getName(), dish.getPrice(), 1));
        if (listener != null) {
            listener.onCartItemAdded(cartItems.size() - 1);
        }
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void clearCart() {
        cartItems.clear();
        if (listener != null) {
            listener.onCartCleared();
        }
    }

    public void removeItem(int position) {
        if (position >= 0 && position < cartItems.size()) {
            cartItems.remove(position);
            if (listener != null) {
                listener.onCartItemRemoved(position);
            }
        }
    }

    public void updateQuantity(int position, int newQuantity) {
        if (position >= 0 && position < cartItems.size()) {
            CartItem item = cartItems.get(position);
            if (newQuantity > 0) {
                item.setQuantity(newQuantity);
                if (listener != null) {
                    listener.onCartItemChanged(position);
                }
            } else {
                removeItem(position);
            }
        }
    }
}
