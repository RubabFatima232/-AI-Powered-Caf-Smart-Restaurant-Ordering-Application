package com.example.smartcafe;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

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

        if (dish.getImageUrl() != null && !dish.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(dish.getImageUrl())
                    .placeholder(R.drawable.ic_placeholder_dish)
                    .into(holder.dishImage);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DishDetailsActivity.class);
            intent.putExtra(DishDetailsActivity.EXTRA_DISH_ID, dish.getDocumentId());
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
