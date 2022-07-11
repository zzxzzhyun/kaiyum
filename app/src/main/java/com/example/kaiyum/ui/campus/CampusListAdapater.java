package com.example.kaiyum.ui.campus;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kaiyum.R;

import java.util.List;


public class CampusListAdapater extends RecyclerView.Adapter<CampusListAdapater.MyViewHolder> {
    private Context mContext;
    private List<String> mData;
    private Boolean status;

    public CampusListAdapater(Context mContext, List<String> mData, Boolean status) {
        this.mContext = mContext;
        this.mData = mData;
        this.status = status;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.campus_cafeteria.setText(findName(mData.get(position)));
        if (status) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.campus_cafeteria.setTextColor(mContext.getColor(R.color.green_dark));
            }
        } else {
            holder.campus_cafeteria.setTextColor(mContext.getColor(R.color.highlight_color));
        }
        holder.campus_cafeteria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CampusDetailActivity.class);
                intent.putExtra("name", mData.get(position));
                ((Activity) mContext).finish();
                mContext.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public String findName(String name) {
        String title;
        switch (name) {
            case "fclt":
                title = "카이마루(북측카페테리아)";
                break;
            case "west":
                title = "서맛골(서측식당)";
                break;
            case "east1":
                title = "동맛골(동측학생식당)";
                break;
            case "east2":
                title = "동맛골(동측 교직원식당)";
                break;
            default:
                title = "Invalid cafeteria";
                break;
        }

        return title;
    }


}


