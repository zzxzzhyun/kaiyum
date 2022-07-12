package com.example.kaiyum.ui.more;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kaiyum.R;
import com.example.kaiyum.ui.campus.RetrofitClientInstance;
import com.example.kaiyum.ui.campus.RetrofitService;
import com.example.kaiyum.ui.login.LoginActivity;
import com.example.kaiyum.ui.review.MyReviewActivity;
import com.google.gson.JsonObject;
import com.kakao.sdk.user.UserApiClient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoreFragment extends Fragment {
    boolean canChangeNickName = false;
    long unid;
    Context context;


    public MoreFragment() {
        // Required empty public constructor
    }


    public static MoreFragment newInstance(String param1, String param2) {
        MoreFragment fragment = new MoreFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_more, container, false);

        updateKakaoLogin();
        handleLogout(root);
        handleMyReviewBtn(root);
        handleChangeNickname(root);

        return root;
    }

    private void handleMyReviewBtn(View v){
        TextView myReviewBtn = v.findViewById(R.id.more_myReviewBtn);

        myReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MyReviewActivity.class);
                startActivity(intent);
            }
        });
    }

    private void handleChangeNickname(View v){
        TextView changeNicknameBtn = v.findViewById(R.id.more_changeNicknameBtn);
        changeNicknameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(canChangeNickName) {
                    createNicknameDialog();
                }else{
                    Toast.makeText(getContext(), "닉네임은 1회만 변경 가능합니다.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean checkIsChanged(long unid){
        RetrofitService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
        Call<JsonObject> call = service.getUser(unid);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject result = response.body();

                if(result.get("changed_nickname").getAsInt() == 0){
                    canChangeNickName = true;
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
        return true;
    }

    private void handleLogout(View v){
        TextView logout_btn = v.findViewById(R.id.btn_logout);
        logout_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                UserApiClient.getInstance().logout(error -> {
                    if (error != null) {
                        Log.e(TAG, "로그아웃 실패, SDK에서 토큰 삭제됨", error);
                    }else{
                        Log.e(TAG, "로그아웃 성공, SDK에서 토큰 삭제됨");
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                    return null;
                });
            }
        });
    }

    private void updateKakaoLogin() {
        UserApiClient.getInstance().me((user, throwable) -> {
            if (user != null) {
                unid = user.getId();
                checkIsChanged(unid);
            }
            return null;
        });
    }

    private void createNicknameDialog() {
        AlertDialog.Builder dialogBuilder;
        AlertDialog dialog;
        EditText popup_nickname;
        Button popup_save;

        dialogBuilder = new AlertDialog.Builder(getContext());
        final View nicknamePopupView = getLayoutInflater().inflate(R.layout.popup, null);
        popup_nickname = (EditText) nicknamePopupView.findViewById(R.id.popup_nickname_content);

        popup_save = (Button) nicknamePopupView.findViewById(R.id.popup_save);

        dialogBuilder.setView(nicknamePopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        popup_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                putNewNickname(popup_nickname);
                dialog.dismiss();
            }
        });
    }

    private void putNewNickname(TextView newNickname){
        String nickname = newNickname.getText().toString();
        try {
            String encodedNickname = URLEncoder.encode(nickname, "UTF-8");
            encodedNickname = encodedNickname.replaceAll("\\+", "%20");

            RetrofitService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
            Call<JsonObject> call = service.updateNickname(unid, encodedNickname);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Toast.makeText(getActivity(), "닉네임 변경 완료!", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Toast.makeText(getActivity(), "닉네임은 1회만 변경 가능합니다.", Toast.LENGTH_LONG).show();
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "닉네임 변경 오류. 다시 시도해주시기 바랍니다.", Toast.LENGTH_SHORT).show();
        }
    }
}