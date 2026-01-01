package com.example.smartcafe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class AdminMenuAdapter extends RecyclerView.Adapter<AdminMenuAdapter.AdminMenuViewHolder> {

    public interface OnDishActionListener {
        void onEditDish(Dish dish);
        void onDeleteDish(Dish dish, int position);
    }

    private List<Dish> menuItems;
    private OnDishActionListener listener;
    private Context context;

    public AdminMenuAdapter(Context context, List<Dish> menuItems, OnDishActionListener listener) {
        this.context = context;
        this.menuItems = menuItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdminMenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_admin_menu, parent, false);
        return new AdminMenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminMenuViewHolder holder, int position) {
        Dish dish = menuItems.get(position);
        holder.itemName.setText(dish.getName());
        holder.itemPrice.setText(dish.getPrice());

        if (dish.getImageUrl() != null && !dish.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(dish.getImageUrl())
                    .placeholder(R.drawable.ic_placeholder_dish)
                    .into(holder.itemImage);
        }

        holder.editButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditDish(dish);
            }
        });

        holder.deleteButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteDish(dish, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    static class AdminMenuViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemName;
        TextView itemPrice;
        Button editButton;
        Button deleteButton;

        public AdminMenuViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.menu_item_image);
            itemName = itemView.findViewById(R.id.menu_item_name);
            itemPrice = itemView.findViewById(R.id.menu_item_price);
            editButton = itemView.findViewById(R.id.edit_button);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }
}
