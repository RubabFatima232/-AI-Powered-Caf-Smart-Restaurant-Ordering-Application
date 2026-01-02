package com.aicafe.network;

import com.aicafe.model.MenuItem;
import com.aicafe.model.OrderRequest;
import com.aicafe.model.OrderResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @GET("menu")
    Call<List<MenuItem>> getMenu();

    @POST("order")
    Call<OrderResponse> placeOrder(@Body OrderRequest order);
}