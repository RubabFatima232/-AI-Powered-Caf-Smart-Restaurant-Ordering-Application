package com.aicafe;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

public class Repository {
    private final ApiService apiService;

    public interface Callback<T> {
        void onSuccess(T items);
        void onError(Exception e);
    }

    public Repository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://firebasestorage.googleapis.com/v0/b/aicafe-f0b3e.appspot.com/o/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    public void getMenuItems(final Callback<List<MenuItem>> callback) {
        apiService.getMenu().enqueue(new retrofit2.Callback<List<MenuItem>>() {
            @Override
            public void onResponse(Call<List<MenuItem>> call, Response<List<MenuItem>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(new Exception("Failed to load menu"));
                }
            }

            @Override
            public void onFailure(Call<List<MenuItem>> call, Throwable t) {
                callback.onError((Exception) t);
            }
        });
    }
}
