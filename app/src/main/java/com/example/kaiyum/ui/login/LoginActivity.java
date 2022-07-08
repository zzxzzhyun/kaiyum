package com.example.kaiyum.ui.login;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.kaiyum.MainActivity;
import com.example.kaiyum.R;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.common.model.ClientErrorCause;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.Account;
import com.kakao.sdk.user.model.User;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG="사용자";
    private ImageButton btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //signInKakao();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
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
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        if (throwable != null) {
            Log.i("[카카오] 로그인", "실패");
            Log.e("signInKakao()", throwable.getLocalizedMessage());
        } return null;
    };

    private void updateKakaoLogin() {
        UserApiClient.getInstance().me((user, throwable) -> {
            if (user != null) {
                Log.i("[카카오] 로그인 정보", user.toString());
                long userId = user.getId();
                Log.i("[카카오] 로그인 정보", userId + "");
            } else {
                // 로그인 실패
            }
            return null;
        });
    }

}