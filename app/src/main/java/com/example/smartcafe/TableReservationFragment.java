package com.example.smartcafe;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Locale;

public class TableReservationFragment extends Fragment {

    private TextInputEditText dateEditText;
    private TextInputEditText timeEditText;
    private TextInputEditText seatsEditText;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_table_reservation, container, false);

        db = FirebaseFirestore.getInstance();

        dateEditText = view.findViewById(R.id.date_edit_text);
        timeEditText = view.findViewById(R.id.time_edit_text);
        seatsEditText = view.findViewById(R.id.seats_edit_text);
        Button submitButton = view.findViewById(R.id.submit_reservation_button);

        dateEditText.setOnClickListener(v -> showDatePicker());
        timeEditText.setOnClickListener(v -> showTimePicker());
        submitButton.setOnClickListener(v -> submitReservation());

        return view;
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(requireContext(), (view, year1, month1, dayOfMonth) ->
                dateEditText.setText(String.format(Locale.getDefault(), "%d-%02d-%02d", year1, month1 + 1, dayOfMonth)), year, month, day).show();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        new TimePickerDialog(requireContext(), (view, hourOfDay, minute1) ->
                timeEditText.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute1)), hour, minute, true).show();
    }

    private void submitReservation() {
        Editable dateEditable = dateEditText.getText();
        Editable timeEditable = timeEditText.getText();
        Editable seatsEditable = seatsEditText.getText();

        if (dateEditable == null || timeEditable == null || seatsEditable == null) {
            Toast.makeText(requireContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String date = dateEditable.toString().trim();
        String time = timeEditable.toString().trim();
        String seatsStr = seatsEditable.toString().trim();

        if (date.isEmpty() || time.isEmpty() || seatsStr.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int seats = Integer.parseInt(seatsStr);
        String userId = "guest_user"; // Use a placeholder for guest reservations

        Reservation reservation = new Reservation(userId, date, time, seats, "pending");

        db.collection("reservations").add(reservation)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(requireContext(), "Reservation submitted successfully!", Toast.LENGTH_SHORT).show();
                    dateEditText.setText("");
                    timeEditText.setText("");
                    seatsEditText.setText("");
                })
                .addOnFailureListener(e -> Toast.makeText(requireContext(), "Error submitting reservation", Toast.LENGTH_SHORT).show());
    }
}
