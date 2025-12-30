package com.example.ai_poweredcafsmartrestaurantorderingapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FeaturedDishAdapter extends RecyclerView.Adapter<FeaturedDishAdapter.FeaturedDishViewHolder> {

    private List<Dish> dishes;
    private Context context;

    public FeaturedDishAdapter(Context context, List<Dish> dishes) {
        this.context = context;
        this.dishes = dishes;
    }

    @NonNull
    @Override
    public FeaturedDishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_featured_dish, parent, false);
        return new FeaturedDishViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedDishViewHolder holder, int position) {
        Dish dish = dishes.get(position);
        holder.dishName.setText(dish.getName());
        holder.dishPrice.setText(dish.getPrice());
        holder.dishImage.setImageResource(dish.getImageResId());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DishDetailsActivity.class);
            // Pass dish details to the activity
            // intent.putExtra("dish_name", dish.getName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return dishes.size();
    }

    static class FeaturedDishViewHolder extends RecyclerView.ViewHolder {
        ImageView dishImage;
        TextView dishName;
        TextView dishPrice;

        public FeaturedDishViewHolder(@NonNull View itemView) {
            super(itemView);
            dishImage = itemView.findViewById(R.id.dish_image);
            dishName = itemView.findViewById(R.id.dish_name);
            dishPrice = itemView.findViewById(R.id.dish_price);
        }
    }
}
