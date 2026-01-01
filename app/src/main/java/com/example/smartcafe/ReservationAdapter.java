package com.example.smartcafe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder> {

    public interface OnReservationActionListener {
        void onApprove(Reservation reservation);
        void onReject(Reservation reservation);
    }

    private final List<Reservation> reservations;
    private final OnReservationActionListener listener;
    private final Context context;

    public ReservationAdapter(Context context, List<Reservation> reservations, OnReservationActionListener listener) {
        this.context = context;
        this.reservations = reservations;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_reservation, parent, false);
        return new ReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Reservation reservation = reservations.get(position);
        holder.detailsText.setText(String.format(Locale.getDefault(), "Reservation for %d people on %s at %s", reservation.getSeats(), reservation.getDate(), reservation.getTime()));
        holder.statusText.setText(String.format(Locale.getDefault(), "Status: %s", reservation.getStatus()));

        switch (reservation.getStatus()) {
            case "approved":
                holder.statusText.setTextColor(ContextCompat.getColor(context, R.color.green));
                holder.actionButtonsLayout.setVisibility(View.GONE);
                break;
            case "rejected":
                holder.statusText.setTextColor(ContextCompat.getColor(context, R.color.red));
                holder.actionButtonsLayout.setVisibility(View.GONE);
                break;
            default: // pending
                holder.statusText.setTextColor(ContextCompat.getColor(context, R.color.orange));
                holder.actionButtonsLayout.setVisibility(View.VISIBLE);
                break;
        }

        holder.approveButton.setOnClickListener(v -> listener.onApprove(reservation));
        holder.rejectButton.setOnClickListener(v -> listener.onReject(reservation));
    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    public static class ReservationViewHolder extends RecyclerView.ViewHolder {
        TextView detailsText;
        TextView statusText;
        View actionButtonsLayout;
        Button approveButton;
        Button rejectButton;

        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            detailsText = itemView.findViewById(R.id.reservation_details_text);
            statusText = itemView.findViewById(R.id.reservation_status_text);
            actionButtonsLayout = itemView.findViewById(R.id.action_buttons_layout);
            approveButton = itemView.findViewById(R.id.approve_button);
            rejectButton = itemView.findViewById(R.id.reject_button);
        }
    }
}
