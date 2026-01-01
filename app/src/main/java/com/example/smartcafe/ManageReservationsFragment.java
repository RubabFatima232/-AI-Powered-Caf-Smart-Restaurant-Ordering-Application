package com.example.smartcafe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ManageReservationsFragment extends Fragment implements ReservationAdapter.OnReservationActionListener {

    private ReservationAdapter adapter;
    private List<Reservation> reservationList;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_reservations, container, false);

        db = FirebaseFirestore.getInstance();
        RecyclerView recyclerView = view.findViewById(R.id.reservations_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        reservationList = new ArrayList<>();
        adapter = new ReservationAdapter(getContext(), reservationList, this);
        recyclerView.setAdapter(adapter);

        loadReservations();

        return view;
    }

    private void loadReservations() {
        db.collection("reservations").orderBy("timestamp", Query.Direction.DESCENDING).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        reservationList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Reservation reservation = document.toObject(Reservation.class);
                            // You might want to store the document ID if you need to update it
                            // reservation.setDocumentId(document.getId());
                            reservationList.add(reservation);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "Error loading reservations.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onApprove(Reservation reservation) {
        updateReservationStatus(reservation, "approved");
    }

    @Override
    public void onReject(Reservation reservation) {
        updateReservationStatus(reservation, "rejected");
    }

    private void updateReservationStatus(Reservation reservation, String status) {
        // To update, we need the document ID. This requires a small change to how we load the data.
        // Let's reload and find the document for simplicity, though a more optimized app would store the ID.
        db.collection("reservations")
                .whereEqualTo("userId", reservation.getUserId())
                .whereEqualTo("timestamp", reservation.getTimestamp())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        String documentId = queryDocumentSnapshots.getDocuments().get(0).getId();
                        db.collection("reservations").document(documentId)
                                .update("status", status)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(getContext(), "Reservation " + status, Toast.LENGTH_SHORT).show();
                                    loadReservations(); // Refresh the list
                                })
                                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error updating reservation", Toast.LENGTH_SHORT).show());
                    }
                });
    }
}
