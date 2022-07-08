package com.example.kaiyum.ui.campus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.kaiyum.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CampusDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus_detail);

        TextView title = findViewById(R.id.campus_detail_title);
        TextView content1 = findViewById(R.id.campus_detail_content1);
        TextView content2 = findViewById(R.id.campus_detail_content2);
        TextView content3 = findViewById(R.id.campus_detail_content3);

        String name = getIntent().getStringExtra("name");
        title.setText(findName(name));

        RetrofitService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
        Call<JsonObject> call = service.getCampus(name);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonArray menu = response.body().getAsJsonArray("name");
                content1.setText(menu.get(0).toString().replace("\"",""));
                content2.setText(menu.get(1).toString().replace("\"",""));
                content3.setText(menu.get(2).toString().replace("\"",""));
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("jsonerror", t.getMessage());
            }
        });

    }

    public String findName (String name){
        String title;
        switch (name) {
            case "fclt": title = "카이마루(북측카페테리아)";
                break;
            case "west": title = "서맛골(서측식당)";
                break;
            case "east1": title = "동맛골(동측학생식당)";
                break;
            case "east2": title = "동맛골(동측 교직원식당)";
                break;
            default: title = "Invalid cafeteria";
                break;
        }

        return title;
    }


}