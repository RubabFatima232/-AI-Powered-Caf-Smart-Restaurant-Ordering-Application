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
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        reservationList = new ArrayList<>();
        adapter = new ReservationAdapter(requireContext(), reservationList, this);
        recyclerView.setAdapter(adapter);

        loadReservations();

        return view;
    }

    private void loadReservations() {
        db.collection("reservations").orderBy("timestamp", Query.Direction.DESCENDING).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int oldSize = reservationList.size();
                        reservationList.clear();
                        adapter.notifyItemRangeRemoved(0, oldSize);
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Reservation reservation = document.toObject(Reservation.class);
                            reservation.setDocumentId(document.getId()); // Store the ID
                            reservationList.add(reservation);
                        }
                        adapter.notifyItemRangeInserted(0, reservationList.size());
                    } else {
                        Toast.makeText(requireContext(), "Error loading reservations.", Toast.LENGTH_SHORT).show();
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
        if (reservation.getDocumentId() == null) {
            Toast.makeText(requireContext(), "Cannot update: Missing reservation ID.", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("reservations").document(reservation.getDocumentId())
                .update("status", status)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(requireContext(), "Reservation " + status, Toast.LENGTH_SHORT).show();
                    int position = reservationList.indexOf(reservation);
                    if (position != -1) {
                        reservation.setStatus(status);
                        adapter.notifyItemChanged(position);
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(requireContext(), "Error updating reservation", Toast.LENGTH_SHORT).show());
    }
}
