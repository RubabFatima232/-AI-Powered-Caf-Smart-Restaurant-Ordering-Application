package com.aicafe;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import java.util.Locale;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import coil.Coil;
import coil.ImageLoader;
import coil.request.ErrorResult;
import coil.request.ImageRequest;

class Adapters {

    private static final DiffUtil.ItemCallback<MenuItem> MENU_ITEM_CALLBACK = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull MenuItem oldItem, @NonNull MenuItem newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull MenuItem oldItem, @NonNull MenuItem newItem) {
            return oldItem.equals(newItem);
        }
    };

    private static final DiffUtil.ItemCallback<CartItem> CART_ITEM_CALLBACK = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull CartItem oldItem, @NonNull CartItem newItem) {
            return oldItem.getMenuItem().getId() == newItem.getMenuItem().getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull CartItem oldItem, @NonNull CartItem newItem) {
            return oldItem.getQuantity() == newItem.getQuantity();
        }
    };

    private static final DiffUtil.ItemCallback<String> CATEGORY_CALLBACK = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull String oldItem, @NonNull String newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull String oldItem, @NonNull String newItem) {
            return oldItem.equals(newItem);
        }
    };

    private static final DiffUtil.ItemCallback<Order> ORDER_CALLBACK = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull Order oldItem, @NonNull Order newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Order oldItem, @NonNull Order newItem) {
            return oldItem.equals(newItem);
        }
    };

    static class AdminAdapter extends ListAdapter<MenuItem, AdminAdapter.ViewHolder> {
        private final Consumer<MenuItem> onDelete;

        AdminAdapter(Consumer<MenuItem> onDelete) {
            super(MENU_ITEM_CALLBACK);
            this.onDelete = onDelete;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            MenuItem item = getItem(position);
            holder.name.setText(item.getName());
            holder.price.setText(String.format(Locale.getDefault(), "$%.2f", item.getPrice()));
            ImageLoader imageLoader = Coil.imageLoader(holder.itemView.getContext());
            ImageRequest request = new ImageRequest.Builder(holder.itemView.getContext())
                    .data(item.getImageUrl())
                    .placeholder(R.drawable.ic_placeholder_food)
                    .error(R.drawable.ic_placeholder_food)
                    .listener(new ImageRequest.Listener() {
                        @Override
                        public void onError(@NonNull ImageRequest request, @NonNull ErrorResult result) {
                            Log.e("AdminAdapter", "Coil error: ", result.getThrowable());
                        }
                    })
                    .target(holder.image)
                    .build();
            imageLoader.enqueue(request);
            holder.deleteButton.setOnClickListener(v -> onDelete.accept(item));
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            final ImageView image;
            final TextView name;
            final TextView price;
            final ImageButton deleteButton;

            ViewHolder(View view) {
                super(view);
                image = view.findViewById(R.id.ivAdmin);
                name = view.findViewById(R.id.tvAdminName);
                price = view.findViewById(R.id.tvAdminPrice);
                deleteButton = view.findViewById(R.id.btnDeleteAdmin);
            }
        }
    }

    static class RecommendedAdapter extends ListAdapter<MenuItem, RecommendedAdapter.ViewHolder> {
        private final Consumer<MenuItem> onAdd;
        RecommendedAdapter(Consumer<MenuItem> onAdd) {
            super(MENU_ITEM_CALLBACK);
            this.onAdd = onAdd;
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommended, parent, false);
            return new ViewHolder(view);
        }
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            MenuItem item = getItem(position);
            holder.name.setText(item.getName());
            holder.price.setText(String.format(Locale.getDefault(), "$%.2f", item.getPrice()));
            ImageLoader imageLoader = Coil.imageLoader(holder.itemView.getContext());
            ImageRequest request = new ImageRequest.Builder(holder.itemView.getContext())
                    .data(item.getImageUrl())
                    .placeholder(R.drawable.ic_placeholder_food)
                    .target(holder.image)
                    .build();
            imageLoader.enqueue(request);
            holder.itemView.setOnClickListener(v -> onAdd.accept(item));
        }
        static class ViewHolder extends RecyclerView.ViewHolder {
            final ImageView image;
            final TextView name;
            final TextView price;
            ViewHolder(View view) {
                super(view);
                image = view.findViewById(R.id.ivDish);
                name = view.findViewById(R.id.tvName);
                price = view.findViewById(R.id.tvPrice);
            }
        }
    }

    static class FeaturedAdapter extends ListAdapter<MenuItem, FeaturedAdapter.ViewHolder> {
        private final Consumer<MenuItem> onAdd;
        FeaturedAdapter(Consumer<MenuItem> onAdd) {
            super(MENU_ITEM_CALLBACK);
            this.onAdd = onAdd;
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_featured_dish, parent, false);
            return new ViewHolder(view);
        }
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            MenuItem item = getItem(position);
            holder.name.setText(item.getName());
            holder.desc.setText(item.getDescription());
            holder.price.setText(String.format(Locale.getDefault(), "$%.2f", item.getPrice()));
            ImageLoader imageLoader = Coil.imageLoader(holder.itemView.getContext());
            ImageRequest request = new ImageRequest.Builder(holder.itemView.getContext())
                    .data(item.getImageUrl())
                    .placeholder(R.drawable.ic_placeholder_food)
                    .target(holder.image)
                    .build();
            imageLoader.enqueue(request);
            holder.addButton.setOnClickListener(v -> onAdd.accept(item));
        }
        static class ViewHolder extends RecyclerView.ViewHolder {
            final ImageView image;
            final TextView name;
            final TextView desc;
            final TextView price;
            final ImageButton addButton;
            ViewHolder(View view) {
                super(view);
                image = view.findViewById(R.id.ivFeatured);
                name = view.findViewById(R.id.tvFeaturedName);
                desc = view.findViewById(R.id.tvFeaturedDesc);
                price = view.findViewById(R.id.tvFeaturedPrice);
                addButton = view.findViewById(R.id.btnAddFeatured);
            }
        }
    }

    static class CategoryAdapter extends ListAdapter<String, CategoryAdapter.ViewHolder> {
        private final Consumer<String> onClick;
        CategoryAdapter(Consumer<String> onClick) {
            super(CATEGORY_CALLBACK);
            this.onClick = onClick;
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_chip, parent, false);
            return new ViewHolder(view);
        }
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String category = getItem(position);
            holder.chip.setText(category);
            holder.chip.setOnClickListener(v -> onClick.accept(category));
        }
        static class ViewHolder extends RecyclerView.ViewHolder {
            final Chip chip;
            ViewHolder(View view) {
                super(view);
                chip = (Chip) view;
            }
        }
    }

    static class CartAdapter extends ListAdapter<CartItem, CartAdapter.ViewHolder> {
        private final CartManager cartManager;

        CartAdapter(CartManager cartManager) {
            super(CART_ITEM_CALLBACK);
            this.cartManager = cartManager;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            CartItem cartItem = getItem(position);
            MenuItem item = cartItem.getMenuItem();
            holder.name.setText(item.getName());
            holder.price.setText(String.format(Locale.getDefault(), "$%.2f", cartItem.getSubtotal()));
            holder.quantity.setText(String.valueOf(cartItem.getQuantity()));

            ImageLoader imageLoader = Coil.imageLoader(holder.itemView.getContext());
            ImageRequest request = new ImageRequest.Builder(holder.itemView.getContext())
                    .data(item.getImageUrl())
                    .placeholder(R.drawable.ic_placeholder_food)
                    .target(holder.image)
                    .build();
            imageLoader.enqueue(request);

            holder.addButton.setOnClickListener(v -> cartManager.add(item));
            holder.removeButton.setOnClickListener(v -> cartManager.remove(item));
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            final ImageView image;
            final TextView name;
            final TextView price;
            final TextView quantity;
            final ImageButton addButton;
            final ImageButton removeButton;

            ViewHolder(View view) {
                super(view);
                image = view.findViewById(R.id.ivCartItem);
                name = view.findViewById(R.id.tvCartName);
                price = view.findViewById(R.id.tvCartPrice);
                quantity = view.findViewById(R.id.tv_quantity);
                addButton = view.findViewById(R.id.btn_add);
                removeButton = view.findViewById(R.id.btn_remove);
            }
        }
    }

    static class HistoryAdapter extends ListAdapter<Order, HistoryAdapter.ViewHolder> {

        HistoryAdapter() {
            super(ORDER_CALLBACK);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Order order = getItem(position);
            holder.orderId.setText(String.format("#%s", order.getId()));
            holder.date.setText(order.getDate());
            holder.total.setText(String.format(Locale.getDefault(), "Total: $%.2f", order.getTotal()));

            String itemsStr = "";
            if (order.getItems() != null) {
                itemsStr = order.getItems().stream()
                        .map(MenuItem::getName)
                        .collect(Collectors.joining(", "));
            }
            holder.items.setText(itemsStr);
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            final TextView orderId;
            final TextView date;
            final TextView items;
            final TextView total;

            ViewHolder(View view) {
                super(view);
                orderId = view.findViewById(R.id.tvOrderId);
                date = view.findViewById(R.id.tvOrderDate);
                items = view.findViewById(R.id.tvOrderItems);
                total = view.findViewById(R.id.tvOrderTotal);
            }
        }
    }
}
