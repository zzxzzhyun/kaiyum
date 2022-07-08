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

import java.lang.reflect.Array;
import java.util.ArrayList;

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

        ArrayList<Restaurant> restaurantList = getRestaurantList();
        getData(restaurantList);


        return v;
    }

    private ArrayList<Restaurant> getRestaurantList(){
        ArrayList<Restaurant> restaurantList = new ArrayList<>();

        // TODO : API통신으로 식당 리스트 받아와야 함.

        // HardCoded
        Restaurant r1 = new Restaurant();
        Restaurant r2 = new Restaurant();

        r1.setId(1);
        r2.setId(2);
        r1.setReviewCount(10);
        r2.setReviewCount(15);
        r1.setScore(5.0f);
        r2.setScore(3.0f);
        r1.setName("대박맛집");
        r2.setName("완전맛집");
        r1.setLocation("어은동");
        r2.setLocation("궁동");

        restaurantList.add(r1);
        restaurantList.add(r2);

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

            Log.d("check", data.toString());
            adapter.addItem(data);
        }

        adapter.notifyDataSetChanged();
    }
}