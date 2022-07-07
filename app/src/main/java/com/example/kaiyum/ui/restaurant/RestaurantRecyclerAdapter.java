package com.example.kaiyum.ui.restaurant;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kaiyum.R;
import com.example.kaiyum.data.model.Restaurant;
import com.example.kaiyum.ui.restaurant.detail.RestaurantDetailActivity;


import java.util.ArrayList;

public class RestaurantRecyclerAdapter extends RecyclerView.Adapter<RestaurantRecyclerAdapter.ItemViewHolder>{
    private ArrayList<Restaurant> listData = new ArrayList<>();

    @NonNull
    @Override
    public RestaurantRecyclerAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_restaurant_recycler_adapter, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantRecyclerAdapter.ItemViewHolder holder, int position) {
        Restaurant restaurant = listData.get(position);

        holder.id.setText("" + restaurant.getId());
        holder.name.setText(restaurant.getName());
        holder.reviewCount.setText("" + restaurant.getReviewCount());
        holder.score.setText("" + restaurant.getScore());
        holder.location.setText(restaurant.getLocation());
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void addItem(Restaurant data){
        listData.add(data);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView id;
        private TextView name;
        private TextView location;
        private TextView reviewCount;
        private TextView score;

        ItemViewHolder(View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.restaurant_id);
            name = itemView.findViewById(R.id.restaurant_name);
            location = itemView.findViewById(R.id.restaurant_location);
            reviewCount = itemView.findViewById(R.id.restaurant_review);
            score = itemView.findViewById(R.id.restaurant_score);

            // 각 아이템마다 클릭이벤트 생성
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO : 클릭 이벤트 발생 시 가게 상세페이지로 이동시킴
                    Intent detailIntent = new Intent(v.getContext(), RestaurantDetailActivity.class);

                    detailIntent.putExtra("id", id.getText());

                    v.getContext().startActivity(detailIntent);
                }
            });
        }

    }
}
