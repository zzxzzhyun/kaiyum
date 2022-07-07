package com.example.kaiyum.ui.campus;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kaiyum.R;

import java.util.Arrays;
import java.util.List;

public class CampusFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    // TODO: Temporary open/close list
    List<String> openArr = Arrays.asList(new String[]{"open 1", "open 2", "open 3", "open 4", "open 5"});
    List<String> closeArr = Arrays.asList(new String[]{"close 1", "close 2", "close 3", "close 4", "close 5"});


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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_campus, container, false);

        RecyclerView openrv = root.findViewById(R.id.open_campus_list);
        CampusListAdapater openAdapter = new CampusListAdapater(requireContext(), openArr);
        openrv.setAdapter(openAdapter);
        openrv.setLayoutManager(new LinearLayoutManager(requireContext()));

        RecyclerView closerv = root.findViewById(R.id.close_campus_list);
        CampusListAdapater closeAdapter = new CampusListAdapater(requireContext(), closeArr);
        closerv.setAdapter(closeAdapter);
        closerv.setLayoutManager(new LinearLayoutManager(requireContext()));

        return root;
    }
}