package com.example.kaiyum.data.model;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitService {
    @GET("campus") // url을 제외한 End Point
    Call<Result> getResults(@Query("name")String name); // get방식

}
