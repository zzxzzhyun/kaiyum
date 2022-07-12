package com.example.kaiyum.ui.restaurant;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.kaiyum.R;
import com.example.kaiyum.data.model.Restaurant;
import com.example.kaiyum.ui.campus.RetrofitClientInstance;
import com.example.kaiyum.ui.campus.RetrofitService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantListFragment extends Fragment {
    private RestaurantRecyclerAdapter adapter;
    private ArrayList<Restaurant> restaurantList;
    HashMap<String, Boolean> selectedLocation = new HashMap<>();

    private String[] LOCATION_KEYS = {
            "ueon",
            "ugoong",
            "goong",
            "bongmyung"
    };

    private final String LOCATION_ALL = "all";

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

        handleSearch(v);
        handleLocation(v);

        RecyclerView recyclerView = v.findViewById(R.id.restaurant_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        restaurantList = getRestaurantList(v);

        return v;
    }

    private ArrayList<Restaurant> getRestaurantList(View v) {
        ArrayList<Restaurant> restaurantList = new ArrayList<>();

        RetrofitService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
        Call<JsonArray> call = service.getRestaurants(LOCATION_ALL);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                JsonArray menu = response.body();

                for (JsonElement object : menu) {
                    Restaurant r = new Restaurant();

                    r.setId(object.getAsJsonObject().get("rid").getAsInt());
                    r.setReviewCount(object.getAsJsonObject().get("review_count").getAsInt());
                    r.setScore(object.getAsJsonObject().get("score").getAsFloat());
                    r.setName(object.getAsJsonObject().get("name").getAsString());
                    r.setLocation(object.getAsJsonObject().get("location").getAsString());

                    if (object.getAsJsonObject().get("img") == JsonNull.INSTANCE) {
                        r.setImageURL("");
                    } else {
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

    private void getData(ArrayList<Restaurant> list) {
        // 초기화
        adapter.deleteAllItem();

        for (Restaurant r : list) {
            if (!selectedLocation.get(r.getLocation())) {
                continue;
            }

            Restaurant data = new Restaurant();

            data.setId(r.getId());
            data.setLocation(r.getLocation());
            data.setName(r.getName());
            data.setScore(r.getScore());
            data.setReviewCount(r.getReviewCount());
            data.setImageURL(r.getImageURL());

            adapter.addItem(data);
        }

        adapter.notifyDataSetChanged();
    }

    private ArrayList<Restaurant> searchList(String key) {
        ArrayList<Restaurant> ret = new ArrayList<>();

        for (Restaurant r : restaurantList) {
            if (r.getName().contains(key)) {
                ret.add(r);
            }
        }

        return ret;
    }

    private void handleSearch(View v) {
        SearchView searchView = v.findViewById(R.id.restaurant_searchView);
        searchView.isSubmitButtonEnabled();

        for (String location : LOCATION_KEYS) {
            selectedLocation.put(location, true);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                getData(searchList(s));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                getData(searchList(s));
                return true;
            }
        });
    }

    private void handleLocation(View v) {
        AppCompatButton[] btns = {
                v.findViewById(R.id.restaurant_ueonBtn),
                v.findViewById(R.id.restaurant_ugoongBtn),
                v.findViewById(R.id.restaurant_goongBtn),
                v.findViewById(R.id.restaurant_bongmyungBtn)
        };

        // location map initialize
        for (String location : LOCATION_KEYS) {
            selectedLocation.put(location, false);
        }
        selectedLocation.put(LOCATION_KEYS[0], true);


        for (int i = 0; i < btns.length; i++) {
            int index = i;
            btns[i].setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View view) {
                    // for toggle
                    boolean isSelectedNow = selectedLocation.get(LOCATION_KEYS[index]);

                    if(isSelectedNow){
                        btns[index].setBackground(getResources().getDrawable(R.drawable.round_button_primary));
                    }else{
                        btns[index].setBackground(getResources().getDrawable(R.drawable.round_button));
                    }

                    selectedLocation.put(LOCATION_KEYS[index], !isSelectedNow);

                    getData(restaurantList);
                }
            });
        }
    }
}