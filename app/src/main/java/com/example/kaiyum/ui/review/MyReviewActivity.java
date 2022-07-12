package com.example.kaiyum.ui.review;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kaiyum.R;
import com.example.kaiyum.data.model.Review;
import com.example.kaiyum.ui.campus.RetrofitClientInstance;
import com.example.kaiyum.ui.campus.RetrofitService;
import com.example.kaiyum.ui.login.LoginActivity;
import com.example.kaiyum.ui.restaurant.detail.RestaurantDetailReviewRecyclerAdapter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.kakao.sdk.user.UserApiClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyReviewActivity extends AppCompatActivity {
    long unid;
    RestaurantDetailReviewRecyclerAdapter adapter = new RestaurantDetailReviewRecyclerAdapter();
    ArrayList<Review> reviewList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_review);

        init();
    }

    private void init(){
        RecyclerView recyclerView = findViewById(R.id.MyReview_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        getUserInfo();
    }

    private void getUserInfo(){
        UserApiClient.getInstance().me((user, throwable) -> {
            if (user != null) {
                unid = user.getId();
                getReviews(unid);
            } else {
                Toast.makeText(getApplicationContext(), "로그인 정보를 찾을 수 없습니다.", Toast.LENGTH_SHORT);

                Intent intent = new Intent(MyReviewActivity.this, LoginActivity.class);
                startActivity(intent);
            }
            return null;
        });
    }

    private void getReviews(long unid){
        RetrofitService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
        Call<JsonArray> call = service.getReviewsByUNID(unid);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                JsonArray reviews = response.body();

                // 내가 쓴 리뷰가 있다면 넙죽이가 안 보이게 하기
                if(reviews.size() == 0){
                    ImageView imgView = findViewById(R.id.MyReview_Neopjuk);
                    TextView textView = findViewById(R.id.MyReview_noReviewText);

                    imgView.bringToFront();
                    textView.bringToFront();
                }

                for(JsonElement object : reviews){
                    Review r = new Review();

                    r.setUserName(object.getAsJsonObject().get("nickname").getAsString());
                    r.setText(object.getAsJsonObject().get("text").getAsString());
                    r.setRestaurantName(object.getAsJsonObject().get("name").getAsString());
                    r.setScore(object.getAsJsonObject().get("score").getAsInt());

                    if(object.getAsJsonObject().get("img") == JsonNull.INSTANCE){
                        r.setImgUrl("");
                    }else{
                        r.setImgUrl(object.getAsJsonObject().get("img").getAsString());
                    }

                    reviewList.add(r);
                }

                getData(reviewList);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "리뷰 불러오기 실패 ㅠㅠ", Toast.LENGTH_SHORT);
            }
        });
    }

    private void getData(ArrayList<Review> list){
        for(Review r : list){
            Review data = new Review();

            data.setUserName(r.getUserName());
            data.setRestaurantName(r.getRestaurantName());
            data.setScore(r.getScore());
            data.setText(r.getText());
            data.setImgUrl(r.getImgUrl());

            adapter.addItem(data);
        }

        adapter.notifyDataSetChanged();
    }
}