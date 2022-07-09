package com.example.kaiyum.ui.login;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.kaiyum.MainActivity;
import com.example.kaiyum.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import com.example.kaiyum.ui.campus.RetrofitClientInstance;
import com.example.kaiyum.ui.campus.RetrofitService;
import com.google.gson.JsonObject;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.common.KakaoSdk;
import com.kakao.sdk.common.model.ClientErrorCause;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.Account;
import com.kakao.sdk.user.model.User;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "사용자";
    private ImageButton btn_login;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText popup_nickname;
    private Button popup_save;
    private Long userId;
    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        KakaoSdk.init(this, getString(R.string.kakao_native_key));

        btn_login = findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInKakao();
            }
        });
    }

    private void signInKakao() {
        // @brief : 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(LoginActivity.this))
            UserApiClient.getInstance().loginWithKakaoTalk(LoginActivity.this, callback);
        else UserApiClient.getInstance().loginWithKakaoAccount(LoginActivity.this, callback);
    }

    Function2<OAuthToken, Throwable, Unit> callback = (oAuthToken, throwable) -> {
        if (oAuthToken != null) {
            Log.i("[카카오] 로그인", "성공");
            updateKakaoLogin();
        }
        if (throwable != null) {
            Log.i("[카카오] 로그인", "실패");
            Log.e("signInKakao()", throwable.getLocalizedMessage());
        }
        return null;
    };

    private void checkExistingUser() {
        RetrofitService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
        Call<JsonObject> call = service.getUser(userId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("unid", userId);
                intent.putExtra("nickname", response.body().get("nickname").getAsString());
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                createNicknameDialog();
            }
        });
    }


    private void updateKakaoLogin() {
        UserApiClient.getInstance().me((user, throwable) -> {
            if (user != null) {
                Log.i("[카카오] 로그인 정보", user.toString());
                userId = user.getId();
                Log.i("[카카오] 로그인 정보", userId + "");
                checkExistingUser();
            } else {
                // 로그인 실패
            }
            return null;
        });
    }

    public void createNicknameDialog() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View nicknamePopupView = getLayoutInflater().inflate(R.layout.popup, null);
        popup_nickname = (EditText) nicknamePopupView.findViewById(R.id.popup_nickname_content);

        popup_save = (Button) nicknamePopupView.findViewById(R.id.popup_save);

        dialogBuilder.setView(nicknamePopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        popup_save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                saveOrNot();
            }

        });

    }

    public void saveOrNot() {
        if (popup_nickname.getText() != null) {
            RetrofitService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
            Call<JsonObject> call = service.addUser(userId, popup_nickname.getText().toString());
            call.enqueue(new Callback<JsonObject>() {

                 @Override
                 public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                     result = response.body().get("message").getAsString();
                     if (result.equals("success")) {
                         dialog.dismiss();
                         Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                         intent.putExtra("unid", userId);
                         intent.putExtra("nickname", popup_nickname.getText());
                         startActivity(intent);
                     } else if (result.equals("fail")) {
                         Toast.makeText(LoginActivity.this, "중복되는 닉네임입니다.", Toast.LENGTH_LONG).show();
                         popup_nickname.setText(null);
                     } else {
                         dialog.dismiss();
                     }
                 }

                 @Override
                 public void onFailure(Call<JsonObject> call, Throwable t) {
                     result = "FAIL";
                 }
             }
            );
        }
    }


}