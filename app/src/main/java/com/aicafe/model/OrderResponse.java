package com.aicafe.model;

import com.google.gson.annotations.SerializedName;

public class OrderResponse {
    @SerializedName("orderId")
    private String orderId;

    @SerializedName("status")
    private String status;

    public String getOrderId() { return orderId; }
    public String getStatus() { return status; }
}