package com.example.kaiyum.ui.campus;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kaiyum.R;

import java.util.List;


public class CampusListAdapater extends RecyclerView.Adapter<CampusListAdapater.MyViewHolder> {
    private Context mContext ;
    private List<String> mData;

    public CampusListAdapater(Context mContext, List<String> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView campus_cafeteria;

        public MyViewHolder(View itemView) {
            super(itemView);
            campus_cafeteria = (TextView) itemView.findViewById(R.id.foodlist_item_id);

        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.foodlist_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.campus_cafeteria.setText(mData.get(position));
        holder.campus_cafeteria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CampusDetailActivity.class);
                intent.putExtra("name", mData.get(position));
                ((Activity)mContext).finish();
                mContext.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }


}


