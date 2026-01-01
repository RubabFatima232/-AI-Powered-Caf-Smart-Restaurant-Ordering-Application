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

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartFragment extends Fragment implements CartAdapter.OnCartActionListener, CartManager.CartChangedListener {

    private CartAdapter cartAdapter;
    private TextView totalBillTextView;
    private FirebaseFirestore db;
    private List<CartItem> cartItems;
    private CartManager cartManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        db = FirebaseFirestore.getInstance();
        cartManager = CartManager.getInstance();

        RecyclerView cartRecyclerView = view.findViewById(R.id.cart_recycler_view);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        cartItems = cartManager.getCartItems();
        cartAdapter = new CartAdapter(cartItems, this);
        cartRecyclerView.setAdapter(cartAdapter);

        totalBillTextView = view.findViewById(R.id.total_bill_text_view);

        Button placeOrderButton = view.findViewById(R.id.place_order_button);
        placeOrderButton.setOnClickListener(v -> placeOrder());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        cartManager.setListener(this);
        // Sync UI on resume
        cartAdapter.notifyDataSetChanged();
        updateTotalBill();
    }

    @Override
    public void onPause() {
        super.onPause();
        cartManager.setListener(null); // Avoid memory leaks
    }

    private void updateTotalBill() {
        double total = 0;
        for (CartItem item : cartItems) {
            try {
                double price = Double.parseDouble(item.getPrice().replace("$", ""));
                total += price * item.getQuantity();
            } catch (NumberFormatException e) {
                // Handle case where price is not a valid number
            }
        }
        totalBillTextView.setText(String.format(Locale.getDefault(), "Total: $%.2f", total));
    }

    private void placeOrder() {
        if (cartItems.isEmpty()) {
            Toast.makeText(requireContext(), "Your cart is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = "guest_user"; // Placeholder
        List<OrderItem> orderItems = new ArrayList<>();
        double totalPrice = 0;

        for (CartItem cartItem : cartItems) {
            orderItems.add(new OrderItem(cartItem.getDishId(), cartItem.getName(), cartItem.getPrice(), cartItem.getQuantity()));
            try {
                totalPrice += Double.parseDouble(cartItem.getPrice().replace("$", "")) * cartItem.getQuantity();
            } catch (NumberFormatException e) {
                // Handle error
            }
        }

        Order order = new Order(userId, orderItems, totalPrice);

        db.collection("orders").add(order)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(requireContext(), "Order placed successfully!", Toast.LENGTH_SHORT).show();
                    cartManager.clearCart();
                })
                .addOnFailureListener(e -> Toast.makeText(requireContext(), "Error placing order", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onIncreaseQuantity(int position) {
        cartManager.updateQuantity(position, cartItems.get(position).getQuantity() + 1);
    }

    @Override
    public void onDecreaseQuantity(int position) {
        cartManager.updateQuantity(position, cartItems.get(position).getQuantity() - 1);
    }

    @Override
    public void onCartItemAdded(int position) {
        cartAdapter.notifyItemInserted(position);
        updateTotalBill();
    }

    @Override
    public void onCartItemRemoved(int position) {
        cartAdapter.notifyItemRemoved(position);
        updateTotalBill();
    }

    @Override
    public void onCartItemChanged(int position) {
        cartAdapter.notifyItemChanged(position);
        updateTotalBill();
    }

    @Override
    public void onCartCleared() {
        cartAdapter.notifyDataSetChanged();
        updateTotalBill();
    }
}
