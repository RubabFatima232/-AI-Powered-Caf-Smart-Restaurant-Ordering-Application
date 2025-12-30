package com.example.ai_poweredcafsmartrestaurantorderingapplication;

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

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {

    private List<CartItem> cartItems;
    private CartAdapter cartAdapter;
    private TextView totalBillTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        RecyclerView cartRecyclerView = view.findViewById(R.id.cart_recycler_view);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        cartItems = new ArrayList<>();
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
        placeOrderButton.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Order Placed!", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    private void updateTotalBill() {
        double total = 0;
        for (CartItem item : cartItems) {
            // Remove the '$' and parse the price
            double price = Double.parseDouble(item.getPrice().substring(1));
            total += price * item.getQuantity();
        }
        totalBillTextView.setText(String.format("$%.2f", total));
    }
}
