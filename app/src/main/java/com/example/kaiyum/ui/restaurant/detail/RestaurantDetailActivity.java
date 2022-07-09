package com.example.kaiyum.ui.restaurant.detail;

import static com.example.kaiyum.ui.restaurant.detail.MapFragment.naverMap;
import static com.example.kaiyum.ui.restaurant.detail.MapFragment.setMarker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kaiyum.R;
import com.example.kaiyum.data.model.Restaurant;
import com.example.kaiyum.ui.campus.RetrofitClientInstance;
import com.example.kaiyum.ui.campus.RetrofitService;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantDetailActivity extends AppCompatActivity implements OnMapReadyCallback{
    private MapView mapView;
    public static NaverMap naverMap;
    private Marker marker = new Marker();

    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        //네이버 지도
        mapView = (MapView) findViewById(R.id.restaurantDetail_map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        Intent intent = getIntent();
        getRestaurant(Integer.parseInt(intent.getStringExtra("rid")));
    }

    private void getRestaurant(int rid) {
        RetrofitService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
        Call<JsonObject> call = service.getRestaurantByRID(rid);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject menu = response.body();

                Restaurant r = new Restaurant();

                r.setId(rid);
                r.setName(menu.get("name").getAsString());
                r.setLocation(menu.get("location").getAsString());
                r.setScore(menu.get("score").getAsFloat());
                r.setReviewCount(5);

                if(menu.get("img") == JsonNull.INSTANCE) {
                    r.setImageURL("");
                }else{
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
        score.setText("" + r.getScore());
        reviewCount.setText("" + r.getReviewCount());

        // imageURL이 빈 문자열이다 == 미리보기 이미지가 없다.
        if(!r.getImageURL().equals("")){
            Glide.with(getApplicationContext()).load(r.getImageURL()).into(img);
        }

        // TODO : 위도와 경도를 서버에서 받아서 설정하는 코드 추가
        latitude = 36.3628152;
        longitude = 127.3588209;
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
                9,                                     // 줌 레벨
                0,                                       // 기울임 각도
                0                                     // 방향
        );
        naverMap.setCameraPosition(cameraPosition);

        setMarker(marker, latitude, longitude, R.drawable.restauranticon, 1);
    }
}