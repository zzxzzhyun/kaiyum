package com.example.kaiyum.ui.restaurant.detail;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kaiyum.R;
import com.example.kaiyum.data.model.Review;

import java.util.ArrayList;

public class RestaurantDetailReviewRecyclerAdapter extends RecyclerView.Adapter<RestaurantDetailReviewRecyclerAdapter.ItemViewHolder>{
    private ArrayList<Review> listData = new ArrayList<>();

    @NonNull
    @Override
    public RestaurantDetailReviewRecyclerAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_restaurantdetail_recyclerview, parent, false);
        return new RestaurantDetailReviewRecyclerAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantDetailReviewRecyclerAdapter.ItemViewHolder holder, int position) {
        Review restaurant = listData.get(position);

        holder.userName.setText("" + restaurant.getUserName());
        holder.score.setText("" + restaurant.getScore());
        holder.text.setText("" + restaurant.getText());
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void addItem(Review data){
        listData.add(data);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView userName;
        private TextView score;
        private TextView text;

        ItemViewHolder(View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.restaurantDetail_userName);
            score = itemView.findViewById(R.id.restaurantDetail_userScore);
            text = itemView.findViewById(R.id.restaurantDetail_userReview);
        }

    }
}
