package com.aicafe;

import com.aicafe.model.MenuItem;
import com.aicafe.model.Order;
import java.util.List;
import java.util.UUID;
import retrofit2.Call;
import retrofit2.Response;

public class Repository {
    private final ApiService api = RetrofitClient.getRetrofitInstance().create(ApiService.class);

    public interface Callback {
        void onSuccess(List<MenuItem> items);
        void onError(Exception e);
    }

    public void getMenuItems(Callback callback) {
        api.getMenu().enqueue(new retrofit2.Callback<List<MenuItem>>() {
            @Override
            public void onResponse(Call<List<MenuItem>> call, Response<List<MenuItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(new Exception("Failed to fetch menu"));
                }
            }

            @Override
            public void onFailure(Call<List<MenuItem>> call, Throwable t) {
                callback.onError(new Exception(t));
            }
        });
    }

    public Order placeOrder(List<Integer> itemIds) {
        // In a real app, this would be an async call
        return new Order(UUID.randomUUID().toString());
    }
}