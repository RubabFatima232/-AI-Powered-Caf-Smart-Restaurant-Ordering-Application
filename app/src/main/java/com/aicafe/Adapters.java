package com.aicafe;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.aicafe.databinding.ItemCartBinding;
import com.aicafe.databinding.ItemCategoryChipBinding;
import com.aicafe.databinding.ItemFeaturedDishBinding;
import com.aicafe.databinding.ItemRecommendedBinding;
import com.aicafe.model.MenuItem;
import com.google.android.material.chip.Chip;
import java.util.Arrays;
import java.util.function.Consumer;

public final class Adapters {
    public static class RecommendedAdapter extends ListAdapter<MenuItem, RecommendedAdapter.Holder> {
        private final OnClick click;
        public RecommendedAdapter(OnClick click) {
            super(MenuItem.DIFF);
            this.click = click;
        }
        @NonNull @Override public Holder onCreateViewHolder(@NonNull ViewGroup p, int type) {
            ItemRecommendedBinding b = ItemRecommendedBinding.inflate(LayoutInflater.from(p.getContext()), p, false);
            return new Holder(b);
        }
        @Override public void onBindViewHolder(@NonNull Holder h, int position) {
            MenuItem item = getItem(position);
            h.binding.tvName.setText(item.getName());
            h.binding.tvPrice.setText("$" + item.getPrice());
            h.binding.getRoot().setOnClickListener(v -> click.add(item));
        }
        static class Holder extends RecyclerView.ViewHolder {
            private final ItemRecommendedBinding binding;
            Holder(ItemRecommendedBinding binding) { super(binding.getRoot()); this.binding = binding; }
        }
    }

    public static class FeaturedAdapter extends ListAdapter<MenuItem, FeaturedAdapter.Holder> {
        private final OnClick click;
        public FeaturedAdapter(OnClick click) {
            super(MenuItem.DIFF);
            this.click = click;
        }
        @NonNull @Override public Holder onCreateViewHolder(@NonNull ViewGroup p, int type) {
            ItemFeaturedDishBinding b = ItemFeaturedDishBinding.inflate(LayoutInflater.from(p.getContext()), p, false);
            return new Holder(b);
        }
        @Override public void onBindViewHolder(@NonNull Holder h, int position) {
            MenuItem item = getItem(position);
            h.binding.tvFeaturedName.setText(item.getName());
            h.binding.tvFeaturedDesc.setText("Chef’s special");
            h.binding.tvFeaturedPrice.setText("$" + item.getPrice());
            h.binding.btnAddFeatured.setOnClickListener(v -> click.add(item));
        }
        static class Holder extends RecyclerView.ViewHolder {
            private final ItemFeaturedDishBinding binding;
            Holder(ItemFeaturedDishBinding binding) { super(binding.getRoot()); this.binding = binding; }
        }
    }

    public static class CategoryAdapter extends ListAdapter<String, CategoryAdapter.Holder> {
        private final Consumer<String> click;
        public CategoryAdapter(Consumer<String> click) {
            super(new DiffUtil.ItemCallback<String>() {
                public boolean areItemsTheSame(@NonNull String old, @NonNull String now) { return old.equals(now); }
                public boolean areContentsTheSame(@NonNull String old, @NonNull String now) { return old.equals(now); }
            });
            this.click = click;
            submitList(Arrays.asList("Beverages", "Fast Food", "Desserts", "Salads"));
        }
        @NonNull @Override public Holder onCreateViewHolder(@NonNull ViewGroup p, int type) {
            ItemCategoryChipBinding b = ItemCategoryChipBinding.inflate(LayoutInflater.from(p.getContext()), p, false);
            return new Holder(b);
        }
        @Override public void onBindViewHolder(@NonNull Holder h, int position) {
            String cat = getItem(position);
            h.chip.setText(cat);
            h.chip.setOnClickListener(v -> click.accept(cat));
        }
        static class Holder extends RecyclerView.ViewHolder {
            private final Chip chip;
            Holder(ItemCategoryChipBinding binding) { super(binding.getRoot()); chip = binding.getRoot(); }
        }
    }

    public static class CartAdapter extends ListAdapter<MenuItem, CartAdapter.Holder> {
        private final Consumer<MenuItem> remove;
        public CartAdapter(Consumer<MenuItem> remove) {
            super(MenuItem.DIFF);
            this.remove = remove;
        }
        @NonNull @Override public Holder onCreateViewHolder(@NonNull ViewGroup p, int type) {
            ItemCartBinding b = ItemCartBinding.inflate(LayoutInflater.from(p.getContext()), p, false);
            return new Holder(b);
        }
        @Override public void onBindViewHolder(@NonNull Holder h, int position) {
            MenuItem item = getItem(position);
            h.binding.tvCartName.setText(item.getName());
            h.binding.tvCartPrice.setText("$" + item.getPrice());
            h.binding.btnDelete.setOnClickListener(v -> remove.accept(item));
        }
        static class Holder extends RecyclerView.ViewHolder {
            private final ItemCartBinding binding;
            Holder(ItemCartBinding binding) { super(binding.getRoot()); this.binding = binding; }
        }
    }

    public interface OnClick { void add(MenuItem item); }
}