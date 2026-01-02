package com.aicafe;

import com.aicafe.model.Category;
import com.aicafe.model.MenuItem;
import com.aicafe.model.Order;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    @GET("recommended")
    Call<List<MenuItem>> getRecommendedDishes();

    @GET("featured")
    Call<List<MenuItem>> getFeaturedDishes();

    @GET("categories")
    Call<List<Category>> getCategories();

    @POST("order")
    Call<Order> placeOrder(@Body List<Integer> itemIds);
}
