package com.example.smartcafe;

import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;

public class Reservation {
    private String userId;
    private String date;
    private String time;
    private int seats;
    private String status; // e.g., "pending", "approved", "rejected"
    @ServerTimestamp
    private Date timestamp;

    public Reservation() {
        // Needed for Firestore
    }

    public Reservation(String userId, String date, String time, int seats, String status) {
        this.userId = userId;
        this.date = date;
        this.time = time;
        this.seats = seats;
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
