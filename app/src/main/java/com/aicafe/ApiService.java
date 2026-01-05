package com.aicafe;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("menu.json")
    Call<List<MenuItem>> getMenu();
}
