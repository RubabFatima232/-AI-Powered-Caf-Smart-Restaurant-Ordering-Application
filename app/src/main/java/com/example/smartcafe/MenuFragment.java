package com.example.smartcafe;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;

public class MenuFragment extends Fragment {

    private static final String TAG = "MenuFragment";
    private FirebaseFirestore db;
    private MenuAdapter menuAdapter;
    private List<Dish> dishList;
    private ListenerRegistration menuListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();

        RecyclerView menuRecyclerView = view.findViewById(R.id.menu_recycler_view);
        menuRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        dishList = new ArrayList<>();
        menuAdapter = new MenuAdapter(requireContext(), dishList);
        menuRecyclerView.setAdapter(menuAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        loadMenu();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (menuListener != null) {
            menuListener.remove();
        }
    }

    private void loadMenu() {
        menuListener = db.collection("menu")
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        if (isAdded()) {
                           Toast.makeText(requireContext(), "Error loading menu.", Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }

                    if (snapshots != null && isAdded()) {
                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            try {
                                Dish dish = dc.getDocument().toObject(Dish.class);
                                dish.setDocumentId(dc.getDocument().getId());

                                switch (dc.getType()) {
                                    case ADDED:
                                        dishList.add(dc.getNewIndex(), dish);
                                        menuAdapter.notifyItemInserted(dc.getNewIndex());
                                        break;
                                    case MODIFIED:
                                        if (dc.getOldIndex() == dc.getNewIndex()) {
                                            // Item has been modified, but its position is unchanged
                                            dishList.set(dc.getNewIndex(), dish);
                                            menuAdapter.notifyItemChanged(dc.getNewIndex());
                                        } else {
                                            // Item has been modified and has moved
                                            dishList.remove(dc.getOldIndex());
                                            dishList.add(dc.getNewIndex(), dish);
                                            menuAdapter.notifyItemMoved(dc.getOldIndex(), dc.getNewIndex());
                                            menuAdapter.notifyItemChanged(dc.getNewIndex()); // To update the contents
                                        }
                                        break;
                                    case REMOVED:
                                        dishList.remove(dc.getOldIndex());
                                        menuAdapter.notifyItemRemoved(dc.getOldIndex());
                                        break;
                                }
                            } catch (Exception parseException) {
                                Log.e(TAG, "Error parsing dish document: " + dc.getDocument().getId(), parseException);
                            }
                        }
                    }
                });
    }
}
