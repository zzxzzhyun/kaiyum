package com.example.kaiyum.ui.restaurant;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kaiyum.R;
import com.example.kaiyum.data.model.Restaurant;
import com.example.kaiyum.ui.campus.RetrofitClientInstance;
import com.example.kaiyum.ui.campus.RetrofitService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantListFragment extends Fragment {
    private RestaurantRecyclerAdapter adapter;

    public RestaurantListFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static RestaurantListFragment newInstance(String param1, String param2) {
        RestaurantListFragment fragment = new RestaurantListFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_restaurant_list, container, false);
        adapter = new RestaurantRecyclerAdapter();

        RecyclerView recyclerView = v.findViewById(R.id.restaurant_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        getRestaurantList(v);

        return v;
    }

    private ArrayList<Restaurant> getRestaurantList(View v){
        ArrayList<Restaurant> restaurantList = new ArrayList<>();

        RetrofitService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
        Call<JsonArray> call = service.getRestaurants();
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                JsonArray menu = response.body();

                for(JsonElement object : menu){
                    Restaurant r = new Restaurant();

                    r.setId(object.getAsJsonObject().get("rid").getAsInt());
                    r.setReviewCount(object.getAsJsonObject().get("review_count").getAsInt());
                    r.setScore(object.getAsJsonObject().get("score").getAsFloat());
                    r.setName(object.getAsJsonObject().get("name").getAsString());
                    r.setLocation(object.getAsJsonObject().get("location").getAsString());

                    if(object.getAsJsonObject().get("img") == JsonNull.INSTANCE){
                        r.setImageURL("");
                    }else{
                        r.setImageURL(object.getAsJsonObject().get("img").getAsString());
                    }

                    restaurantList.add(r);
                }

                getData(restaurantList);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e("jsonerror", t.getMessage());
            }
        });

        return restaurantList;
    }

    private void getData(ArrayList<Restaurant> list){
        for(Restaurant r : list){
            Restaurant data = new Restaurant();

            data.setId(r.getId());
            data.setLocation(r.getLocation());
            data.setName(r.getName());
            data.setScore(r.getScore());
            data.setReviewCount(r.getReviewCount());
            data.setImageURL(r.getImageURL());

            Log.d("check", data.toString());
            adapter.addItem(data);
        }

        adapter.notifyDataSetChanged();
    }
}