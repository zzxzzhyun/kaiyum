package com.example.kaiyum.ui.restaurant;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kaiyum.R;
import com.example.kaiyum.data.model.Restaurant;
import com.example.kaiyum.ui.restaurant.detail.RestaurantDetailActivity;


import java.util.ArrayList;

public class RestaurantRecyclerAdapter extends RecyclerView.Adapter<RestaurantRecyclerAdapter.ItemViewHolder>{
    private ArrayList<Restaurant> listData = new ArrayList<>();
    View view;

    @NonNull
    @Override
    public RestaurantRecyclerAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_restaurant_recycler_adapter, parent, false);
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

        // Image URL로 사진 교체
        if(restaurant.getImageURL().equals("")){
            holder.thumbnail.setImageResource(R.drawable.restauranticon);
        }else{
            Glide.with(view.getContext()).load(restaurant.getImageURL()).into(holder.thumbnail);
        }
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

    public void deleteAllItem() { listData.clear(); }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView id;
        private TextView name;
        private TextView location;
        private TextView reviewCount;
        private TextView score;
        private ImageView thumbnail;

        ItemViewHolder(View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.restaurant_id);
            name = itemView.findViewById(R.id.restaurant_name);
            location = itemView.findViewById(R.id.restaurant_location);
            reviewCount = itemView.findViewById(R.id.restaurant_review);
            score = itemView.findViewById(R.id.restaurant_score);
            thumbnail = itemView.findViewById(R.id.restaurant_recyclerView_thumbnail);

            // 각 아이템마다 클릭이벤트 생성
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent detailIntent = new Intent(v.getContext(), RestaurantDetailActivity.class);

                    detailIntent.putExtra("rid", id.getText());

                    v.getContext().startActivity(detailIntent);
                }
            });
        }

    }
}
