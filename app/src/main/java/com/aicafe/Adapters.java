package com.aicafe;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import coil.Coil;
import coil.request.ImageRequest;
import com.aicafe.databinding.ItemCartBinding;
import com.aicafe.databinding.ItemCategoryChipBinding;
import com.aicafe.databinding.ItemFeaturedDishBinding;
import com.aicafe.databinding.ItemRecommendedBinding;
import com.aicafe.model.MenuItem;
import com.google.android.material.chip.Chip;
import java.util.Arrays;
import java.util.function.Consumer;

public final class Adapters {

    public interface OnClick { void add(MenuItem item); }
    public interface OnRemove { void remove(MenuItem item); }

    private static void loadImage(ImageView imageView, String url) {
        ImageRequest request = new ImageRequest.Builder(imageView.getContext())
                .data(url)
                .placeholder(R.drawable.ic_placeholder_food)
                .error(R.drawable.ic_placeholder_food)
                .target(imageView)
                .build();
        Coil.imageLoader(imageView.getContext()).enqueue(request);
    }

    public static class RecommendedAdapter extends ListAdapter<MenuItem, RecommendedAdapter.Holder> {
        private final OnClick click;
        public RecommendedAdapter(OnClick click) {
            super(DIFF);
            this.click = click;
        }
        @NonNull @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup p, int type) {
            return new Holder(ItemRecommendedBinding.inflate(LayoutInflater.from(p.getContext()), p, false));
        }
        @Override
        public void onBindViewHolder(@NonNull Holder h, int position) {
            MenuItem item = getItem(position);
            h.binding.tvName.setText(item.getName());
            h.binding.tvPrice.setText("$" + item.getPrice());
            loadImage(h.binding.ivDish, item.getImageUrl());
            h.itemView.setOnClickListener(v -> click.add(item));
        }
        static class Holder extends RecyclerView.ViewHolder {
            private final ItemRecommendedBinding binding;
            Holder(ItemRecommendedBinding b) { super(b.getRoot()); binding = b; }
        }
    }

    public static class FeaturedAdapter extends ListAdapter<MenuItem, FeaturedAdapter.Holder> {
        private final OnClick click;
        public FeaturedAdapter(OnClick click) {
            super(DIFF);
            this.click = click;
        }
        @NonNull @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup p, int type) {
            return new Holder(ItemFeaturedDishBinding.inflate(LayoutInflater.from(p.getContext()), p, false));
        }
        @Override
        public void onBindViewHolder(@NonNull Holder h, int position) {
            MenuItem item = getItem(position);
            h.binding.tvFeaturedName.setText(item.getName());
            h.binding.tvFeaturedDesc.setText(item.getDescription());
            h.binding.tvFeaturedPrice.setText("$" + item.getPrice());
            loadImage(h.binding.ivFeatured, item.getImageUrl());
            h.binding.btnAddFeatured.setOnClickListener(v -> click.add(item));
        }
        static class Holder extends RecyclerView.ViewHolder {
            private final ItemFeaturedDishBinding binding;
            Holder(ItemFeaturedDishBinding b) { super(b.getRoot()); binding = b; }
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
        @NonNull @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup p, int type) {
            ItemCategoryChipBinding b = ItemCategoryChipBinding.inflate(LayoutInflater.from(p.getContext()), p, false);
            return new Holder(b);
        }
        @Override
        public void onBindViewHolder(@NonNull Holder h, int position) {
            String cat = getItem(position);
            h.chip.setText(cat);
            h.chip.setOnClickListener(v -> click.accept(cat));
        }
        static class Holder extends RecyclerView.ViewHolder {
            private final Chip chip;
            Holder(ItemCategoryChipBinding b) { super(b.getRoot()); chip = b.getRoot(); }
        }
    }

    public static class CartAdapter extends ListAdapter<MenuItem, CartAdapter.Holder> {
        private final Consumer<MenuItem> remove;
        public CartAdapter(Consumer<MenuItem> remove) {
            super(DIFF);
            this.remove = remove;
        }
        @NonNull @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup p, int type) {
            return new Holder(ItemCartBinding.inflate(LayoutInflater.from(p.getContext()), p, false));
        }
        @Override
        public void onBindViewHolder(@NonNull Holder h, int position) {
            MenuItem item = getItem(position);
            h.binding.tvCartName.setText(item.getName());
            h.binding.tvCartPrice.setText("$" + item.getPrice());
            loadImage(h.binding.ivCartItem, item.getImageUrl());
            h.binding.btnDelete.setOnClickListener(v -> remove.accept(item));
        }
        static class Holder extends RecyclerView.ViewHolder {
            private final ItemCartBinding binding;
            Holder(ItemCartBinding b) { super(b.getRoot()); binding = b; }
        }
    }

    public static final DiffUtil.ItemCallback<MenuItem> DIFF = new DiffUtil.ItemCallback<MenuItem>() {
        @Override public boolean areItemsTheSame(@NonNull MenuItem old, @NonNull MenuItem now) { return old.getId() == now.getId(); }
        @Override public boolean areContentsTheSame(@NonNull MenuItem old, @NonNull MenuItem now) { return old.equals(now); }
    };
}