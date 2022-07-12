package com.example.kaiyum.ui.restaurant.detail;

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
import com.example.kaiyum.data.model.Review;

import java.util.ArrayList;

public class RestaurantDetailReviewRecyclerAdapter extends RecyclerView.Adapter<RestaurantDetailReviewRecyclerAdapter.ItemViewHolder>{
    private ArrayList<Review> listData = new ArrayList<>();
    View view;

    @NonNull
    @Override
    public RestaurantDetailReviewRecyclerAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_restaurantdetail_recyclerview, parent, false);
        return new RestaurantDetailReviewRecyclerAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantDetailReviewRecyclerAdapter.ItemViewHolder holder, int position) {
        Review review = listData.get(position);

        holder.userName.setText("" + review.getUserName());
        holder.text.setText("" + review.getText());
        holder.timestamp.setText("" + review.getTimestamp().substring(0,10));

        // 사진이 있으면 url로 보여주고, 없으면 imageView를 숨김
        if(review.getImgUrl() == null){
            holder.reviewImg.setVisibility(View.GONE);
        }else{
            Log.d("img", review.getImgUrl());
            Glide.with(view.getContext()).load(review.getImgUrl()).into(holder.reviewImg);
        }

        for(int i=0;i<review.getScore();i++){
            holder.stars[i].setImageResource(R.drawable.star_on);
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

    public void addItem(Review data){
        listData.add(data);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView userName;
        private TextView text;
        private ImageView reviewImg;
        private TextView timestamp;
        ImageView[] stars = new ImageView[5];

        ItemViewHolder(View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.restaurantDetail_userName);
            text = itemView.findViewById(R.id.restaurantDetail_userReview);
            reviewImg = itemView.findViewById(R.id.restaurantDetail_reviewImg);
            stars[0] = itemView.findViewById(R.id.restaurantDetail_reviewStar1);
            stars[1] = itemView.findViewById(R.id.restaurantDetail_reviewStar2);
            stars[2] = itemView.findViewById(R.id.restaurantDetail_reviewStar3);
            stars[3] = itemView.findViewById(R.id.restaurantDetail_reviewStar4);
            stars[4] = itemView.findViewById(R.id.restaurantDetail_reviewStar5);
            timestamp = itemView.findViewById(R.id.review_timestamp);
        }

    }
}
