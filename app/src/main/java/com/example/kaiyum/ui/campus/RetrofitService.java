package com.example.kaiyum.ui.campus;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface RetrofitService {

    @GET("campus") // url을 제외한 End Point
    Call<JsonObject> getCampus(@Query("name") String body); // get방식

    @GET("restaurant")
    Call<JsonArray> getRestaurants(@Query("location") String location);

    @GET("restaurant/detail/{rid}")
    Call<JsonObject> getRestaurantByRID(@Path("rid") int rid);

    @GET("restaurant/search")
    Call<JsonArray> getRestaurantsBySearch(@Query("key") String key);

    @GET("review/restaurant/{rid}")
    Call<JsonArray> getReviewsByRID(@Path("rid") int rid);
}