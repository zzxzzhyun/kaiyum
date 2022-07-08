package com.example.kaiyum.ui.campus;

import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface RetrofitService {

    @GET("campus") // url을 제외한 End Point
    Call<JsonObject> getCampus(@Query("name") String body); // get방식

}