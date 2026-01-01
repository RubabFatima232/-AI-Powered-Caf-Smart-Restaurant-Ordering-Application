package com.example.smartcafe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    public interface OnCartActionListener {
        void onIncreaseQuantity(int position);
        void onDecreaseQuantity(int position);
    }

    private final List<CartItem> cartItems;
    private final OnCartActionListener listener;

    public CartAdapter(List<CartItem> cartItems, OnCartActionListener listener) {
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);
        holder.dishName.setText(cartItem.getName());
        holder.dishPrice.setText(cartItem.getPrice());
        holder.quantityTextView.setText(String.format(Locale.getDefault(), "%d", cartItem.getQuantity()));

        holder.increaseQuantityButton.setOnClickListener(v -> {
            if (listener != null) {
                int adapterPosition = holder.getBindingAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onIncreaseQuantity(adapterPosition);
                }
            }
        });

        holder.decreaseQuantityButton.setOnClickListener(v -> {
            if (listener != null) {
                int adapterPosition = holder.getBindingAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onDecreaseQuantity(adapterPosition);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView dishName;
        TextView dishPrice;
        TextView quantityTextView;
        Button increaseQuantityButton;
        Button decreaseQuantityButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            dishName = itemView.findViewById(R.id.dish_name);
            dishPrice = itemView.findViewById(R.id.dish_price);
            quantityTextView = itemView.findViewById(R.id.quantity_text_view);
            increaseQuantityButton = itemView.findViewById(R.id.increase_quantity_button);
            decreaseQuantityButton = itemView.findViewById(R.id.decrease_quantity_button);
        }
    }
}
