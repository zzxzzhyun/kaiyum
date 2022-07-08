package com.example.kaiyum.ui.campus;

import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kaiyum.R;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CampusFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    // TODO: Temporary open/close list
    List<String> allArr = Arrays.asList(new String[]{"fclt", "west", "east1", "east2"});
    List<String> openArr = new ArrayList<>();
    List<String> closeArr = new ArrayList<>();
    List<String> day = Arrays.asList(new String[]{"morning", "lunch", "dinner"});

    public CampusFragment() {
        // Required empty public constructor
    }


    public static CampusFragment newInstance(String param1, String param2) {
        CampusFragment fragment = new CampusFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_campus, container, false);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        AssetManager assetManager= getContext().getAssets();

        try {
            InputStream is = assetManager.open("campusTime.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            JSONObject jsonObject = new JSONObject(json);

            Calendar cal = Calendar.getInstance();
            cal.set(1970, 0, 1);
            Date current = cal.getTime();

            for (String temp: this.allArr){
                for (String time: this.day){
                    String time1 = jsonObject.getJSONObject(temp).getJSONObject(time).getString("open");
                    String time2 = jsonObject.getJSONObject(temp).getJSONObject(time).getString("close");
                    Date date1 = timeFormat.parse(time1);
                    Date date2 = timeFormat.parse(time2);
                    if (date1.getTime()-current.getTime()<=0 && date2.getTime()-current.getTime()>=0){
                       this.openArr.add(temp);
                       break;
                    }
                }
                if (openArr!=null && !openArr.contains(temp)){
                    this.closeArr.add(temp);
                }
            }

        } catch (IOException | JSONException | ParseException e) {
            e.printStackTrace();
        }

        RecyclerView openrv = root.findViewById(R.id.open_campus_list);
        if (!openArr.isEmpty()){
        CampusListAdapater openAdapter = new CampusListAdapater(requireContext(), openArr);
        openrv.setAdapter(openAdapter);
        openrv.setLayoutManager(new LinearLayoutManager(requireContext()));}

        RecyclerView closerv = root.findViewById(R.id.close_campus_list);
        if (!closeArr.isEmpty()){
        CampusListAdapater closeAdapter = new CampusListAdapater(requireContext(), closeArr);
        closerv.setAdapter(closeAdapter);
        closerv.setLayoutManager(new LinearLayoutManager(requireContext()));}

        return root;
    }


}