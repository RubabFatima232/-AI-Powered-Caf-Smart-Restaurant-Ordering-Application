package com.example.smartcafe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {

    private List<CartItem> cartItems;
    private CartAdapter cartAdapter;
    private TextView totalBillTextView;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        RecyclerView cartRecyclerView = view.findViewById(R.id.cart_recycler_view);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        cartItems = new ArrayList<>();
        // In a real app, you would load cart items from a local database or a remote server
        // For now, we will continue to use dummy data.
        cartItems.add(new CartItem("Cheese Pizza", "$8.99", R.drawable.ic_placeholder_dish, 1));
        cartItems.add(new CartItem("Chocolate Shake", "$4.50", R.drawable.ic_placeholder_dish, 2));

        cartAdapter = new CartAdapter(cartItems);
        cartRecyclerView.setAdapter(cartAdapter);

        totalBillTextView = view.findViewById(R.id.total_bill_text_view);
        updateTotalBill();

        cartAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                updateTotalBill();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);
                updateTotalBill();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
                super.onItemRangeChanged(positionStart, itemCount, payload);
                updateTotalBill();
            }
        });

        Button placeOrderButton = view.findViewById(R.id.place_order_button);
        placeOrderButton.setOnClickListener(v -> placeOrder());

        return view;
    }

    private void updateTotalBill() {
        double total = 0;
        for (CartItem item : cartItems) {
            double price = Double.parseDouble(item.getPrice().substring(1));
            total += price * item.getQuantity();
        }
        totalBillTextView.setText(String.format("Total: $%.2f", total));
    }

    private void placeOrder() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(getContext(), "You must be logged in to place an order", Toast.LENGTH_SHORT).show();
            return;
        }

        if (cartItems.isEmpty()) {
            Toast.makeText(getContext(), "Your cart is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = currentUser.getUid();
        List<OrderItem> orderItems = new ArrayList<>();
        double totalPrice = 0;

        for (CartItem cartItem : cartItems) {
            // In a real app, you would use the actual dishId
            orderItems.add(new OrderItem("placeholder_dish_id", cartItem.getName(), cartItem.getPrice(), cartItem.getQuantity()));
            totalPrice += Double.parseDouble(cartItem.getPrice().substring(1)) * cartItem.getQuantity();
        }

        Order order = new Order(userId, orderItems, totalPrice);

        db.collection("orders").add(order)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getContext(), "Order placed successfully!", Toast.LENGTH_SHORT).show();
                    cartItems.clear();
                    cartAdapter.notifyDataSetChanged();
                    updateTotalBill();
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error placing order", Toast.LENGTH_SHORT).show());
    }
}
