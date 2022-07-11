package com.example.kaiyum.ui.campus;

import com.example.kaiyum.data.model.Review;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface RetrofitService {

    @GET("user")
    Call<JsonObject> getUser(@Query("unid") Long id);

    @POST("user")
    Call<JsonObject> addUser(@Query("unid") Long id, @Query("nickname") String nickname);

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

    @GET("user")
    Call<JsonObject> getUserByUNID(@Query("unid") long unid);

    @POST("review")
    Call<JsonObject> addReview(@Body Review review);

    @Multipart
    @POST("review/image")
    Call<JsonObject> addReviewImage(@Part MultipartBody.Part file, @Query("reviewId") int review_id);
}