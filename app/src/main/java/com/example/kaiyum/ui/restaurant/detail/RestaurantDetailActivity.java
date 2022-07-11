package com.example.kaiyum.ui.restaurant.detail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kaiyum.R;
import com.example.kaiyum.data.model.Restaurant;
import com.example.kaiyum.data.model.Review;
import com.example.kaiyum.ui.campus.RetrofitClientInstance;
import com.example.kaiyum.ui.campus.RetrofitService;
import com.example.kaiyum.ui.review.ReviewActivity;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantDetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    private MapView mapView;
    public static NaverMap naverMap;
    private Marker marker = new Marker();

    private int rid;

    // for Intent
    String restaurantName;

    double latitude;
    double longitude;

    public RestaurantDetailReviewRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        //네이버 지도
        mapView = (MapView) findViewById(R.id.restaurantDetail_map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        Intent intent = getIntent();
        rid = Integer.parseInt(intent.getStringExtra("rid"));
        getRestaurant(rid);

        adapter = new RestaurantDetailReviewRecyclerAdapter();

        RecyclerView recyclerView = findViewById(R.id.restaurantDetail_review_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        getReviewList();
        handleWriteReview();
    }

    private void getRestaurant(int rid) {
        RetrofitService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
        Call<JsonObject> call = service.getRestaurantByRID(rid);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject menu = response.body();

                Restaurant r = new Restaurant();

                restaurantName = menu.get("name").getAsString();

                r.setId(rid);
                r.setName(menu.get("name").getAsString());
                r.setLocation(menu.get("location").getAsString());
                r.setScore(menu.get("score").getAsFloat());
                r.setReviewCount(menu.get("review_count").getAsInt());
                r.setX(Double.parseDouble(menu.get("x").getAsString()));
                r.setY(Double.parseDouble(menu.get("y").getAsString()));

                if (menu.get("img") == JsonNull.INSTANCE) {
                    r.setImageURL("");
                } else {
                    r.setImageURL(menu.get("img").getAsString());
                }

                bindRestaurant(r);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("jsonerror", t.getMessage());
            }
        });
    }

    private void bindRestaurant(Restaurant r) {
        ImageView img = findViewById(R.id.restaurantDetail_mainImage);
        TextView name = findViewById(R.id.restaurantDetail_name);
        TextView location = findViewById(R.id.restaurantDetail_location);
        TextView score = findViewById(R.id.restaurantDetail_score);
        TextView reviewCount = findViewById(R.id.restaurantDetail_reviewCount);

        name.setText(r.getName());
        location.setText(r.getLocation());
        score.setText(String.format("%.1f", r.getScore()));
        reviewCount.setText("" + r.getReviewCount());

        // imageURL이 빈 문자열이다 == 미리보기 이미지가 없다.
        if (!r.getImageURL().equals("")) {
            Glide.with(getApplicationContext()).load(r.getImageURL()).into(img);
        }

        // TODO : 위도와 경도를 서버에서 받아서 설정하는 코드 추가
        latitude = r.getY();
        longitude = r.getX();
    }

    public static void setMarker(Marker marker, double lat, double lng, int resourceID, int zIndex) {
        marker.setIconPerspectiveEnabled(true);
        marker.setIcon(OverlayImage.fromResource(resourceID));
        marker.setAlpha(0.8f);
        marker.setPosition(new LatLng(lat, lng));
        marker.setZIndex(zIndex);
        marker.setMap(naverMap);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;

        //배경 지도 선택
        naverMap.setMapType(NaverMap.MapType.Navi);

        //건물 표시
        naverMap.setLayerGroupEnabled(naverMap.LAYER_GROUP_BUILDING, true);

        //위치 및 각도 조정
        CameraPosition cameraPosition = new CameraPosition(
                new LatLng(latitude, longitude),   // 위치 지정
                17,                                     // 줌 레벨
                0,                                       // 기울임 각도
                0                                     // 방향
        );
        naverMap.setCameraPosition(cameraPosition);

        setMarker(marker, latitude, longitude, R.drawable.ic_baseline_location_on_24, 1);
    }

    private ArrayList<Review> getReviewList() {
        ArrayList<Review> reviewList = new ArrayList<>();

        RetrofitService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
        Call<JsonArray> call = service.getReviewsByRID(rid);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                JsonArray reviews = response.body();

                for (JsonElement object : reviews) {
                    Review r = new Review();

                    r.setUserName(object.getAsJsonObject().get("nickname").getAsString());
                    r.setText(object.getAsJsonObject().get("text").getAsString());
                    r.setScore(object.getAsJsonObject().get("score").getAsInt());

                    if(!object.getAsJsonObject().get("img").isJsonNull()){
                        r.setImgUrl(object.getAsJsonObject().get("img").getAsString());
                    }

                    reviewList.add(r);
                }

                getData(reviewList);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e("jsonerror", t.getMessage());
            }
        });

        return reviewList;
    }

    private void getData(ArrayList<Review> list) {
        for (Review r : list) {
            Review data = new Review();

            data.setUserName(r.getUserName());
            data.setScore(r.getScore());
            data.setText(r.getText());
            data.setImgUrl(r.getImgUrl());
            adapter.addItem(data);
        }

        adapter.notifyDataSetChanged();
    }

    private void handleWriteReview(){
        Button writeBtn = findViewById(R.id.restaurantDetail_reviewBtn);

        writeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RestaurantDetailActivity.this, ReviewActivity.class);
                intent.putExtra("restaurantName", restaurantName);
                intent.putExtra("rid", rid);

                startActivity(intent);
            }
        });
    }
}